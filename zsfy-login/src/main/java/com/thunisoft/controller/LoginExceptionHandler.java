package com.thunisoft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LoginExceptionHandler {

    @ExceptionHandler({UnknownAccountException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String shiroUnkonwAccount() {
        return "UnkonwAccount";
    }

    @ExceptionHandler({JsonProcessingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String loginJsonParseError () {
        return "Json Processing";
    }


}
