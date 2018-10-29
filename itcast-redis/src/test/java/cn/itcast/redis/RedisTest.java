package cn.itcast.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    //字符串
    @Test
    public void testString(){
        redisTemplate.boundValueOps("str_key").set("传智播客i_am_ljb");
        Object obj = redisTemplate.boundValueOps("str_key").get();
        System.out.println(obj);
    }

    //散列
    @Test
    public void testHash(){
        //key名称；域名；域值
        redisTemplate.boundHashOps("h_key").put("f_1", "v_1");
        redisTemplate.boundHashOps("h_key").put("f_2", "v_2");
        redisTemplate.boundHashOps("h_key").put("f_3", "v_3");

        Object obj = redisTemplate.boundHashOps("h_key").get("f_1");
        System.out.println(obj);

        obj = redisTemplate.boundHashOps("h_key").values();
        System.out.println(obj);
    }

    //列表
    @Test
    public void testList(){
        redisTemplate.boundListOps("l_key").rightPush("c");
        redisTemplate.boundListOps("l_key").leftPush("b");
        redisTemplate.boundListOps("l_key").leftPush("a");
        redisTemplate.boundListOps("l_key").rightPush("d");

        Object obj = redisTemplate.boundListOps("l_key").range(0, -1);
        System.out.println(obj);
    }

    //集合
    @Test
    public void testSet(){
        redisTemplate.boundSetOps("s_key").add("a", "b", "c");
        Object obj = redisTemplate.boundSetOps("s_key").members();
        System.out.println(obj);
    }

    //有序集合；根据分值升序排序
    @Test
    public void testSortedSet(){
        redisTemplate.boundZSetOps("z_key").add("a", 20);
        redisTemplate.boundZSetOps("z_key").add("c", 5);
        redisTemplate.boundZSetOps("z_key").add("b", 10);
        Object obj = redisTemplate.boundZSetOps("z_key").range(0, -1);
        System.out.println(obj);
    }
}
