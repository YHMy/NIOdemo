package com.example.niodemo.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/loo")
public class testHtml {
    @RequestMapping("/hello")
    public   String login(){
        return "login";
    }
    public void test02(){
        System.out.println("我在这也修改了数据");
    }

}
