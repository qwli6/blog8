package me.lqw.blog8.service;


import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.Category;
import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import me.lqw.blog8.model.vo.ArticlePageQueryParam;
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
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

    /**
     * 状态
     */
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

    /**
     * 构造函数
     */
    public ArticleIndexer()  {
        try {
            //索引目录不存在就创建索引目录
            if (!indexPath.toFile().exists()) {
                Files.createDirectories(indexPath);
            }

            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(createAnalyzer());
            this.indexWriter = new IndexWriter(createDirectory(), indexWriterConfig);
            this.searcherManager = new SearcherManager(indexWriter, new SearcherFactory());
        } catch (IOException ex){
            ex.printStackTrace();
            logger.error("文章索引类初始化失败! 异常: [{}]", ex.getMessage(), ex);
            throw new RuntimeException("文章索引类初始化失败", ex);
        }
    }


    /**
     * 删除文档
     *
     * @param ids ids
     * @throws IOException IOException
     */
    public void deleteDocument(int... ids) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (int id : ids) {
            builder.add(new TermQuery(new Term(ID, String.valueOf(id))), BooleanClause.Occur.SHOULD);
        }
        indexWriter.deleteDocuments(builder.build());
        indexWriter.commit();
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
            indexWriter.commit();
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
    public void addDocument(List<Article> articles) throws IOException {
        indexWriter.addDocuments(articles.stream().map(this::createDocument).collect(Collectors.toList()));
        indexWriter.commit();
        searcherManager.maybeRefresh();
    }


    /**
     * 获取文章 ids
     * @return List
     * @throws IOException IOException
     */
    public List<Integer> doSearchByLucene(String keyWord) throws IOException, ParseException {

          //旧方式，废弃
//        DirectoryReader directoryReader = DirectoryReader.open(directory);
//        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Analyzer analyzer = createAnalyzer();

        QueryParser titleParser = new QueryParser(TITLE, analyzer); //对标题进行分词
        Query titleQuery = titleParser.parse(keyWord);
        QueryParser contentParser = new QueryParser(CONTENT, analyzer);//对内容进行分词
        Query contentQuery = contentParser.parse(keyWord);
        QueryParser digestParser = new QueryParser(DIGEST, analyzer); //对摘要进行分词
        Query digestQuery = digestParser.parse(keyWord);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(titleQuery, BooleanClause.Occur.SHOULD);
        builder.add(contentQuery, BooleanClause.Occur.SHOULD); //SHOULD 或者
        builder.add(digestQuery, BooleanClause.Occur.SHOULD); //摘要
        builder.add(new TermQuery(new Term(ID)), BooleanClause.Occur.SHOULD); //不需要分词直接添加
        builder.add(new TermQuery(new Term(CATEGORY)), BooleanClause.Occur.SHOULD); //分类
        builder.add(new TermQuery(new Term(TAG)), BooleanClause.Occur.SHOULD); //标签
        builder.add(new TermQuery(new Term(URL_NAME)), BooleanClause.Occur.SHOULD); //别名

        IndexSearcher indexSearcher = searcherManager.acquire();

       //查找最近的 100 条，按得分进行排序，得分可以理解成相似度，相似度越高，排序越靠前
        TopDocs search = indexSearcher.search(builder.build(), Integer.MAX_VALUE);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        if(scoreDocs.length == 0) {
            return new ArrayList<>();
        }

//        Integer offset = queryParam.getOffset();
//        Integer pageSize = queryParam.getPageSize();
//        废弃使用 lucene 手动分页方式，变更查询 id，然后根据 id 去数据库查询
//        List<ScoreDoc> subScoreDocs = Arrays.stream(scoreDocs).skip(offset).limit(pageSize).collect(Collectors.toList());
//
//        Map<String, Object> searchResultMap = new HashMap<>(2);
//
//        searchResultMap.put("totalRow", scoreDocs.length);
        List<Integer> ids = new ArrayList<>();
        for(ScoreDoc scoreDoc : scoreDocs){
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            ids.add(Integer.parseInt(document.get(ID)));
        }
        //释放 indexSearcher
        searcherManager.release(indexSearcher);
        return ids;
    }

    /**
     * 创建文档
     * TextField 表示需要对字段分词，
     * StringField 表示不需要对字段进行分词，传进去的字段就是一个整体
     * Store.NO 表示不存储内容，只存储索引（存储内容一般是为了高亮显示内容，其实没必要，存储的内容越多，索引文件越大）
     *
     * @param article article
     * @return Document
     */
    private Document createDocument(Article article) {

        Document document = new Document();

        //文章ID，主键，不分词，不存储
        document.add(new StringField(ID, String.valueOf(article.getId()), Field.Store.YES));

        //文章标题，分词不存储
        document.add(new TextField(TITLE, article.getTitle(), Field.Store.NO));

        //文章内容，分词不存储
        document.add(new TextField(CONTENT, article.getContent(), Field.Store.NO));

        //文章状态，不分词，不存储
        document.add(new StringField(STATUS, article.getStatus().name(), Field.Store.NO));

        if (StringUtil.isNotBlank(article.getUrlName())) {
            //文章别名，不分词，不存储
            document.add(new StringField(URL_NAME, article.getUrlName(), Field.Store.NO));
        }

        if (StringUtil.isNotBlank(article.getDigest())) {
            //文章摘要，分词，不存储
            document.add(new StringField(DIGEST, article.getDigest(), Field.Store.NO));
        }

        //分类
        Category category = article.getCategory();
        if(category != null){
            document.add(new StringField(CATEGORY, category.getName(), Field.Store.NO));
        }

        //标签
        Set<Tag> tags = article.getTags();
        if(!CollectionUtils.isEmpty(tags)){
            for(Tag tag: tags){
                document.add(new StringField(TAG, tag.getTagName(), Field.Store.NO));
            }
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
        return MMapDirectory.open(indexPath);
    }

}
