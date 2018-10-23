package com.platon.browserweb.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Author: cxf
 * Date: 2018-07-11
 * Time: 16:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class BaseTest{
//    @Autowired
//    private WebApplicationContext wac;
//
//    protected MockMvc mvc;
//    protected MockHttpSession session;
//
//    @Before
//    public void setupMockMvc(){
//        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
//        session = new MockHttpSession();
////        User user =new User("root","root");
////        session.setAttribute("user",user); //拦截器那边会判断用户是否登录，所以这里注入一个用户
//    }
}
