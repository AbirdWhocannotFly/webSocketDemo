package com.bj58.payment.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangguang14
 * @date 2019/10/9.
 */
@Controller
public class HelloController {

    @RequestMapping("/")
    @ResponseBody
    public String getHello() {
        return "hello";
    }
}
