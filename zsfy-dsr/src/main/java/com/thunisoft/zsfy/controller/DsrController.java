package com.thunisoft.zsfy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DsrController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/dsr")
    @ResponseBody
    public String dsrInfo (HttpServletRequest request) {
        return "11111";
    }


}
