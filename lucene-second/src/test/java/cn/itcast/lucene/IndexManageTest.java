package cn.itcast.lucene;

import cn.itcast.lucene.dao.BookDao;
import cn.itcast.lucene.dao.impl.BookDaoImpl;
import cn.itcast.lucene.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexManageTest {

    /**
     * 根据条件更新文档
     */
    @Test
    public void updateBoostByTerm() throws Exception {

        //设置索引库目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));

        //创建analyzer
        Analyzer analyzer = new IKAnalyzer();

        //创建索引编写器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //创建索引编写器
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //创建词条
        Term term = new Term("id", "5");

        //创建文档
        Document document = new Document();
        document.add(new StringField("id", "5", Field.Store.YES));

        //分词、索引、存储
        TextField textField = new TextField("bookname", "Lucene Java精华版", Field.Store.YES);
        //默认为1，权重越大则排序越靠前
        textField.setBoost(2.0f);
        document.add(textField);

        //分词、索引、不存储
        document.add(new TextField("bookdesc", "本书总结搜索引擎相关理论与实际解决方案，并给出了 Java 实现，其中利用了流行的开源项目Lucene和Solr，而且还包括原创的实现。本书主要包括总体介绍部分、爬虫部分、自然语言处理部分、全文检索部分以及相关案例分析。爬虫部分介绍了网页遍历方法和如何实现增量抓取，并介绍了从网页等各种格式的文档中提取主要内容的方法。自然语言处理部分从统计机器学习的原理出发，包括了中文分词与词性标注的理论与实现以及在搜索引擎中的实用等细节，同时对文档排重、文本分类、自动聚类、句法分析树、拼写检查等自然语言处理领域的经典问题进行了深入浅出的介绍并总结了实现方法。在全文检索部分，结合Lucene 3.0介绍了搜索引擎的原理与进展。用简单的例子介绍了Lucene的最新应用方法。本书包括完整的搜索实现过程：从完成索引到搜索用户界面的实现。本书还进一步介绍了实现准实时搜索的方法，展示了Solr 1.4版本的用法以及实现分布式搜索服务集群的方法。最后介绍了在地理信息系统领域和户外活动搜索领域的应用。", Field.Store.NO));

        //分词、索引、存储
        document.add(new FloatField("price", 80F, Field.Store.YES));

        //不分词、不索引、存储
        document.add(new StoredField("pic", "5.jpg"));

        //更新=将符合条件的所有记录删除，之后再新增一条文档
        indexWriter.updateDocument(term, document);

        //提交操作
        indexWriter.commit();

        //关闭资源
        indexWriter.close();
    }

    /**
     * 根据条件更新文档
     */
    @Test
    public void updateByTerm() throws Exception {

        //设置索引库目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));

        //创建analyzer
        Analyzer analyzer = new IKAnalyzer();

        //创建索引编写器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //创建索引编写器
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //创建词条
        Term term = new Term("bookname", "java");

        //创建文档
        Document document = new Document();
        document.add(new IntField("id", 123, Field.Store.YES));

        //分词、索引、存储
        document.add(new TextField("bookname", "mybatis spring", Field.Store.YES));

        //分词、索引、不存储
        document.add(new TextField("bookdesc", "desc", Field.Store.NO));

        //分词、索引、存储
        document.add(new FloatField("price", 123.2F, Field.Store.YES));

        //不分词、不索引、存储
        document.add(new StoredField("pic", "test.jpg"));

        //更新=将符合条件的所有记录删除，之后再新增一条文档
        indexWriter.updateDocument(term, document);

        //提交操作
        indexWriter.commit();

        //关闭资源
        indexWriter.close();
    }

    /**
     * 删除全部的文档和其对应的索引--慎用
     */
    @Test
    public void deleteAll() throws Exception {

        //设置索引库目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));

        //创建analyzer
        Analyzer analyzer = new IKAnalyzer();

        //创建索引编写器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //创建索引编写器
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //删除
        indexWriter.deleteAll();

        //提交操作
        indexWriter.commit();

        //关闭资源
        indexWriter.close();
    }

    /**
     * 根据词条删除索引
     */
    @Test
    public void deleteIndexByTerm() throws Exception {

        //设置索引库目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));

        //创建analyzer
        Analyzer analyzer = new IKAnalyzer();

        //创建索引编写器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //创建索引编写器
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //创建词条
        Term term = new Term("bookname", "lucene");

        //根据词条删除文档
        indexWriter.deleteDocuments(term);

        //提交操作
        indexWriter.commit();

        //关闭资源
        indexWriter.close();
    }

    /**
     * 到指定的索引目录根据搜索关键字查询数据
     * @throws Exception
     */
    @Test
    public void searchIndex()  throws Exception {
        //索引存放目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));
        //创建索引读入对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建索引搜索器
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建标准分词器
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();

        //创建查询分析器；参数1：域名，参数2：分词器
        QueryParser queryParser = new QueryParser("bookname", analyzer);

        //创建查询对象
        Query query = queryParser.parse("java");

        /**
         * 查询第1页，每页2条数据
         * mysql 分页---》limit 起始索引号，页大小
         */
        //页号
        int pageNo = 1;
        //页大小
        int pageSize = 2;

        //起始索引号 = (页号-1)*页大小
        int start = (pageNo-1)*pageSize;

        //结束索引号 = 起始索引号 + 页大小
        int end = start + pageSize;


        //搜索；符合查询条件的前10条；参数1：查询对象，参数2：查询的返回最大数
        TopDocs topDocs = indexSearcher.search(query, end+1);

        System.out.println("符合本次查询的总文档数（命中数）为：" + topDocs.totalHits);

        if (end > topDocs.totalHits) {
            //结束索引号大于总的总记录数
            end = topDocs.totalHits;
        }

        //获取得分文档id集合
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
/*

        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            System.out.println("-----------------------------");
            System.out.println("文档在lucene中的id为：" + scoreDoc.doc + "；得分为：" + scoreDoc.score);
            //根据文档id查询对应的文档并输出
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("id = " + document.get("id"));
            System.out.println("bookname = " + document.get("bookname"));
            System.out.println("pic = " + document.get("pic"));
            System.out.println("price = " + document.get("price"));
            System.out.println("bookdesc = " + document.get("bookdesc"));
        }
*/
        for (int i = start; i < end; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            System.out.println("-----------------------------");
            System.out.println("文档在lucene中的id为：" + scoreDoc.doc + "；得分为：" + scoreDoc.score);
            //根据文档id查询对应的文档并输出
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("id = " + document.get("id"));
            System.out.println("bookname = " + document.get("bookname"));
            System.out.println("pic = " + document.get("pic"));
            System.out.println("price = " + document.get("price"));
            System.out.println("bookdesc = " + document.get("bookdesc"));
        }
        //关闭资源
        indexReader.close();
    }

    /**
     * 根据数据库中查询到的数据将这些数据写入到索引中
     */
    @Test
    public void createIndex() throws Exception {
        //1、获取数据
        BookDao bookDao = new BookDaoImpl();
        List<Book> bookList = bookDao.queryBookList();
        //2、转换为lucene支持的文档
        List<Document> documentList = new ArrayList<Document>();
        Document document = null;
        for (Book book : bookList) {
            document = new Document();
            /**
             * TextField 文本域；参数1：域名，参数2：域值，参数3：是否要存储该域值
             *
             */
            //不分词，索引，存储
            document.add(new StringField("id", book.getId().toString(), Field.Store.YES));

            //分词、索引、存储
            document.add(new TextField("bookname", book.getBookname(), Field.Store.YES));

            //分词、索引、不存储
            document.add(new TextField("bookdesc", book.getBookdesc(), Field.Store.NO));

            //分词、索引、存储
            document.add(new FloatField("price", book.getPrice(), Field.Store.YES));

            //不分词、不索引、存储
            document.add(new StoredField("pic", book.getPic()));

            documentList.add(document);
        }
        //3、创建分词器；默认使用标准分词器
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        //4、创建索引编写配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
        //5、创建索引存放目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));
        //6、创建索引编写器对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        //7、利用索引编写器将文档一个个写入到索引目录
        for (Document doc : documentList) {
            indexWriter.addDocument(doc);
        }
        //8、关闭资源
        indexWriter.close();
    }

}
