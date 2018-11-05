package cn.itcast.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/mq")
@RestController
public class MQController {

    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @GetMapping("/send")
    public String sendMapMsg(){

        Map<String, Object> map = new HashMap<>();
        map.put("name", "i_am_ljb");
        map.put("address", "吉山村");

        //参数1：队列的名称 参数2：要发送的消息
        jmsMessagingTemplate.convertAndSend("spring.boot.map.queue", map);

        return "成功发送到spring.boot.map.queue队列中。";
    }
}
