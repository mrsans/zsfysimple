package com.thunisoft.zsfy.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * @Author ZhPJ
 * @Date 2019/1/22 002217:17
 * @Version 1.0
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();   //构造MockMvc
    }
    @Test
    public void getCurr() throws Exception {
        final String content = mockMvc.perform(get("/getcurr").contentType(MediaType.ALL))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(content);
    }

    @Test
    public void relogin() throws Exception {
        final String content = mockMvc.perform(get("/relogin")
                .param("loginId", "wangyg")
                .param("userType","3")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(content);
    }
}