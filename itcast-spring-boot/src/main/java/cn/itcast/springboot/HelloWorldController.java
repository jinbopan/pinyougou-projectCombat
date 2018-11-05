package cn.itcast.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class HelloWorldController {

    @GetMapping("/info")
    public String info(){
        return "Hello SpringBoot.";
    }
}