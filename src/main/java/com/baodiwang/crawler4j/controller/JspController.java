package com.baodiwang.crawler4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Created by lizhou on 2018年08月22日 07时41分
 */
@Controller
public class JspController {

    @RequestMapping("jspTest")
    public String jspTest(){
        return "jspTest";
    }

}
