package cn.itcast.lucene;

import cn.itcast.lucene.dao.BookDao;
import cn.itcast.lucene.dao.impl.BookDaoImpl;
import cn.itcast.lucene.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
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
import java.util.ArrayList;
import java.util.List;

public class IndexManageTest {

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
             */
            document.add(new TextField("id", book.getId().toString(), Field.Store.YES));
            document.add(new TextField("bookname", book.getBookname(), Field.Store.YES));
            document.add(new TextField("bookdesc", book.getBookdesc(), Field.Store.YES));
            document.add(new TextField("price", book.getPrice().toString(), Field.Store.YES));
            document.add(new TextField("pic", book.getPic(), Field.Store.YES));

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
