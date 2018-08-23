package com.baodiwang.crawler4j.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 17/6/16.
 */
@RestController
public class IndexController {

    private static final Logger log = LogManager.getLogger(IndexController.class);

    @GetMapping("/index")
    public Object index() {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        return "success";
    }
}