package com.platon.browser.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("testController")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/test")
    public String test (){
        String sql = "SELECT 1 FROM DUAL" ;
        Integer countOne = jdbcTemplate.queryForObject(sql,Integer.class) ;
        log.info("countOne=="+countOne);
        return "success" ;
    }
}
