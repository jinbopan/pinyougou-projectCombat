package cn.itcast.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerTest {

    @Test
    public void test() throws Exception {

        //1、创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //1.1、设置模版路径
        configuration.setClassForTemplateLoading(FreemarkerTest.class, "/ftl");
        //1.2、文件编码
        configuration.setDefaultEncoding("utf-8");

        //2、获取模版
        Template template = configuration.getTemplate("test.ftl");

        //3、数据
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "传智播客");
        dataModel.put("msg", "can we chat");

        //4、输出；参数1：在模版中使用到的数据map；参数2：输出的媒介
        FileWriter fileWriter = new FileWriter("D:\\itcast\\test\\test.html");
        template.process(dataModel, fileWriter);

        fileWriter.close();
    }
}
