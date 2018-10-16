package cn.itcast.lucene.dao.impl;

import cn.itcast.lucene.dao.BookDao;
import cn.itcast.lucene.pojo.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {

    public List<Book> queryBookList() {
        List<Book> bookList = new ArrayList<Book>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            //加载驱动类
            Class.forName("com.mysql.jdbc.Driver");

            //创建连接
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lucene_0630", "root", "root");

            //创建执行对象
            statement = connection.createStatement();

            //执行查询
            rs = statement.executeQuery("select * from book");

            //处理返回结果
            Book book = null;
            while (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setBookname(rs.getString("bookname"));
                book.setPic(rs.getString("pic"));
                book.setPrice(rs.getFloat("price"));
                book.setBookdesc(rs.getString("bookdesc"));

                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if(rs != null){
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return bookList;
    }

    public static void main(String[] args) {
        BookDao bookDao = new BookDaoImpl();
        List<Book> books = bookDao.queryBookList();
        for (Book book : books) {
            System.out.println(book);
        }
    }
}
