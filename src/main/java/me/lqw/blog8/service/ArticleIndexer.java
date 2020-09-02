package me.lqw.blog8.service;


import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.Category;
import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import me.lqw.blog8.util.JsonUtil;
import me.lqw.blog8.util.JsoupUtil;
import me.lqw.blog8.util.StringUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 文件索引操作类
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class ArticleIndexer {

    /**
     * 标题
     */
    private static final String TITLE = "title";
    /**
     * 摘要
     */
    private static final String DIGEST = "digest";
    /**
     * 内容
     */
    private static final String CONTENT = "content";
    /**
     * 别名
     */
    private static final String URL_NAME = "urlName";
    /**
     * 分类
     */
    private static final String CATEGORY = "category";

    private static final String STATUS = "status";

    /**
     * 标签
     */
    private static final String TAG = "tag";
    /**
     * id
     */
    private static final String ID = "id";
    /**
     * 日志操作
     */
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    /**
     * 索引存放位置
     */
//    private final Path indexPath = Paths.get(System.getProperty("user.home")).resolve("blog/index");

    private final Path indexPath = Paths.get("/Users/liqiwen/Code/lucene");

    /**
     * 索引写入
     */
    private final IndexWriter indexWriter;

    /**
     * 搜索管理器
     */
    private final SearcherManager searcherManager;


//    private Directory dir;

    /**
     * 构造函数
     *
     * @throws IOException IOException
     */
    public ArticleIndexer() throws IOException {

        //索引目录不存在就创建索引目录
        if (!indexPath.toFile().exists()) {
            Files.createDirectories(indexPath);
        }

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(createAnalyzer());
        this.indexWriter = new IndexWriter(createDirectory(), indexWriterConfig);
        this.searcherManager = new SearcherManager(indexWriter, new SearcherFactory());
    }


    private IndexWriter getIndexWriter() throws IOException {

        //指定索引文件存放位置
        Directory dir = createDirectory();

        //指定分词器类型
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(createAnalyzer());

        //创建索引写入器
        return new IndexWriter(dir, indexWriterConfig);
    }




    public void addIndex(Article article) throws IOException {
        IndexWriter indexWriter = getIndexWriter();

        Document document = new Document();
        //StringField 整体，不进行分词，这里只把 id 存储进来，因为我们最终会根据 id 去数据库中查询内容
        //不存储其他内容主要是担心内容太大，导致索引库体积增大
        document.add(new StringField(ID, String.valueOf(article.getId()), Field.Store.YES));

        //TextField 整体，进行分词
        document.add(new TextField(TITLE, article.getTitle(), Field.Store.NO));
        document.add(new TextField(CONTENT, article.getContent(), Field.Store.NO));

        String digest = article.getDigest();
        if(StringUtil.isNotBlank(digest)){
            document.add(new TextField(DIGEST, article.getDigest(), Field.Store.NO));
        }

        ArticleStatusEnum status = article.getStatus();
        //根据用户的登录情况选择是否查找非发布状态的内容
        document.add(new StringField(STATUS, status.name(), Field.Store.NO));

        Category category = article.getCategory();
        if(category != null){
            document.add(new StringField(CATEGORY, category.getName(), Field.Store.NO));
        }

        Set<Tag> tags = article.getTags();
        if(!CollectionUtils.isEmpty(tags)){
            for(Tag tag: tags){
                document.add(new StringField(TAG, tag.getTagName(), Field.Store.NO));
            }
        }
        //写入索引
        indexWriter.addDocument(document);
        //关闭资源
        indexWriter.close();

    }




    /**
     * 删除文档
     *
     * @param ids ids
     * @throws IOException IOException
     */
    public void deleteDocument(int... ids) throws IOException {

        if (logger.isWarnEnabled()) {
            logger.debug("delete document ids:[{}]", ids);
        }

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (int id : ids) {
            builder.add(new TermQuery(new Term(ID, String.valueOf(id))), BooleanClause.Occur.SHOULD);
        }
        indexWriter.deleteDocuments(builder.build());
        searcherManager.maybeRefresh();
    }

    /**
     * 更新文档
     *
     * @param articles article
     * @throws IOException IOException
     */
    public void updateDocument(Article... articles) throws IOException {
        for (Article article : articles) {
            Document document = createDocument(article);
            indexWriter.updateDocument(new Term(ID, String.valueOf(article.getId())), document);
        }
        searcherManager.maybeRefresh();
    }

    /**
     * 重构整个内容的索引
     *
     * @param articles articles
     * @throws IOException IOException
     */
    public void rebuild(List<Article> articles) throws IOException {
        indexWriter.deleteAll();
        indexWriter.addDocuments(articles.stream().map(this::createDocument).collect(Collectors.toList()));
        indexWriter.commit();
        searcherManager.maybeRefreshBlocking();
    }

    /**
     * 添加文档
     *
     * @param articles articles
     * @throws IOException IOException
     */
    public void addDocument(Article... articles) throws IOException {
        indexWriter.addDocuments(Arrays.stream(articles).map(this::createDocument).collect(Collectors.toList()));
        searcherManager.maybeRefresh();
    }


    /**
     * 获取文章 ids
     *
     * @return List
     * @throws IOException IOException
     */
    public List<Integer> searchArticleIds(String keyWord) throws IOException {
        Directory directory = createDirectory();

        DirectoryReader directoryReader = DirectoryReader.open(directory);


        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Analyzer analyzer = createAnalyzer();

//        //对标题进行分词
//        QueryParser titleParser = new QueryParser(TITLE, analyzer);
//
//        Query titleQuery = titleParser.parse(keyWord);
//
//        //对内容进行分词
//        QueryParser contentParser = new QueryParser(CONTENT, analyzer);
//
//        Query contentQuery = contentParser.parse(keyWord);
//
//
//        BooleanQuery.Builder builder = new BooleanQuery.Builder();
//        builder.add(titleQuery, BooleanClause.Occur.SHOULD);
//        builder.add(contentQuery, BooleanClause.Occur.SHOULD); //SHOULD 或者
//
//
//        //查找最近的 100 条，按得分进行排序，得分可以理解成相似度，相似度越高，排序越靠前
//        TopDocs search = indexSearcher.search(builder.build(), 100);


        return new ArrayList<>();
    }

    /**
     * 创建文档
     *
     * @param article article
     * @return Document
     */
    private Document createDocument(Article article) {
        Document document = new Document();

        document.add(new StringField(TITLE, article.getTitle(), Field.Store.NO));

        document.add(new TextField(CONTENT, article.getContent(), Field.Store.NO));

        if (StringUtil.isNotBlank(article.getUrlName())) {
            document.add(new StringField(URL_NAME, article.getUrlName(), Field.Store.NO));
        }

        if (StringUtil.isNotBlank(article.getDigest())) {
            document.add(new StringField(DIGEST, article.getDigest(), Field.Store.NO));
        }
        return document;
    }

    /**
     * 创建索引分词器
     *
     * @return Analyzer
     */
    private Analyzer createAnalyzer() {
        return new SmartChineseAnalyzer();
    }

    /**
     * 创建索引目录
     *
     * @return Directory
     * @throws IOException IOException
     */
    private Directory createDirectory() throws IOException {
        return FSDirectory.open(indexPath);
    }

}
