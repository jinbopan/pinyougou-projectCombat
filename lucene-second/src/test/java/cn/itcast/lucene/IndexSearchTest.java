package cn.itcast.lucene;

import cn.itcast.lucene.dao.BookDao;
import cn.itcast.lucene.dao.impl.BookDaoImpl;
import cn.itcast.lucene.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IndexSearchTest {

    /**
     * 查询分词器
     *
     * @throws Exception
     */
    @Test
    public void queryParser() throws Exception {

        //创建查询分析器；如果分析的时候没有指定域名的话则该该默认的域名查询其词条
        QueryParser queryParser = new QueryParser("bookname", new StandardAnalyzer());

        Query query = queryParser.parse("bookname:action AND price:{80.0 TO 100.0]");

        System.out.println(query);

        search(query);
    }


    /**
     * 组合查询
     *
     * @throws Exception
     */
    @Test
    public void booleanQuery() throws Exception {
        //查询bookname包含action并且价格在{80 TO 100]
        BooleanQuery booleanQuery = new BooleanQuery();

        TermQuery query1 = new TermQuery(new Term("bookname", "action"));

        booleanQuery.add(query1, BooleanClause.Occur.MUST);

        Query query2 = NumericRangeQuery.newFloatRange("price", 80F, 100F, false, true);
        booleanQuery.add(query2, BooleanClause.Occur.MUST);

        System.out.println(booleanQuery);
        search(booleanQuery);
    }

    /**
     * 数值范围查询
     *
     * @throws Exception
     */
    @Test
    public void numericRangeQuery() throws Exception {
        /**
         * 参数1：查询的域名称
         * 参数2：查询数值范围的下限
         * 参数3：查询数值范围的上限
         * 参数4：查询数值范围是否包含下限
         * 参数5：查询数值范围是否包含上限
         * 80 < price <= 100
         */
        Query query = NumericRangeQuery.newFloatRange("price", 80F, 100F, false, true);

        search(query);
    }

    /**
     * 词条查询
     *
     * @throws Exception
     */
    @Test
    public void termQuery() throws Exception {
        TermQuery query = new TermQuery(new Term("bookname", "lucene"));

        search(query);
    }

    /**
     * 到指定的索引目录根据搜索关键字查询数据
     *
     * @throws Exception
     */
    public void search(Query query) throws Exception {
        //索引存放目录
        Directory directory = FSDirectory.open(new File("D:\\itcast\\test\\lucene"));
        //创建索引读入对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建索引搜索器
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建标准分词器
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();

        //搜索；符合查询条件的前10条；参数1：查询对象，参数2：查询的返回最大数
        TopDocs topDocs = indexSearcher.search(query, 10);

        System.out.println("符合本次查询的总文档数（命中数）为：" + topDocs.totalHits);
        //获取得分文档id集合
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < topDocs.totalHits; i++) {
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

}
