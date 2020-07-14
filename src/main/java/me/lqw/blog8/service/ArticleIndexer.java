package me.lqw.blog8.service;


import me.lqw.blog8.model.Article;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件索引操作类
 * @author liqiwen
 * @version 1.0
 */
public class ArticleIndexer implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE = "title";
    private static final String DIGEST = "digest";
    private static final String CONTENT = "content";
    private static final String URL_NAME = "urlName";
    private static final String CATEGORY = "category";
    private static final String TAG = "tag";
    private static final String ID = "id";


    private final Path indexPath = Paths.get(System.getProperty("user.home")).resolve("blog/index");

    private final IndexWriter indexWriter;
    private final Directory directory;
    private final SearcherManager searcherManager;

    public ArticleIndexer() throws IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(createAnalyzer());
        this.directory = createDirectory();
        this.indexWriter = new IndexWriter(createDirectory(), indexWriterConfig);

        searcherManager = new SearcherManager(indexWriter, null);
    }



    public void deleteDocument(int ...ids) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for(int id : ids){
            builder.add(new TermQuery(new Term(ID,String.valueOf(id))), BooleanClause.Occur.SHOULD);
        }
        indexWriter.deleteDocuments(builder.build());
        searcherManager.maybeRefresh();
    }

    public void updateDocument(Article... articles) throws IOException {
        for (Article article : articles) {
            Document document = createDocument(article);
            indexWriter.updateDocument(new Term(ID, String.valueOf(article.getId())), document);
        }
        searcherManager.maybeRefresh();
    }

    public void rebuild(List<Article> articles) throws IOException {
        indexWriter.deleteAll();
        indexWriter.addDocuments(articles.stream().map(this::createDocument).collect(Collectors.toList()));
        indexWriter.commit();
        searcherManager.maybeRefreshBlocking();
    }


    public void addDocument(Article...articles) throws IOException {
        indexWriter.addDocuments(Arrays.stream(articles).map(this::createDocument).collect(Collectors.toList()));
        searcherManager.maybeRefresh();
    }


    public Document createDocument(Article article){
        Document document = new Document();

        document.add(new StringField(TITLE, article.getTitle(), Field.Store.NO));

        document.add(new TextField(CONTENT, article.getContent(), Field.Store.NO));

        if(!StringUtils.isEmpty(article.getUrlName())) {
            document.add(new StringField(URL_NAME, article.getUrlName(), Field.Store.NO));
        }

        if(!StringUtils.isEmpty(article.getDigest())) {
            document.add(new StringField(DIGEST, article.getDigest(), Field.Store.NO));
        }
        return document;
    }

    public Analyzer createAnalyzer(){
        return new SmartChineseAnalyzer();
    }


    public Directory createDirectory() throws IOException {
        return new MMapDirectory(indexPath);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("ArticleIndexer afterPropertiesSet()...");
    }
}
