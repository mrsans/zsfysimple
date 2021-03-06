package com.thunisoft.zsfy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thunisoft.zsfy.constant.UserType;
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
        return UserType.UNKONW.ofValue();
    }

    @ExceptionHandler({JsonProcessingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String loginJsonParseError () {
        return "Json Processing";
    }


}
