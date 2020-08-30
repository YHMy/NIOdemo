package com.example.niodemo.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/loo")
public class testHtml {
    @RequestMapping("/hello")
    public   String login(){
     String str=   "lknkvjhcfgnvblknm;";
        String test02="ewrte5yuy";
        return "login";

    }



}
