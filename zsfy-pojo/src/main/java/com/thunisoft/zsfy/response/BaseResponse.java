package com.thunisoft.zsfy.response;

import lombok.Data;
import lombok.ToString;

/**
 * @Author ZhPJ
 * @Date 2019/1/22 002217:45
 * @Version 1.0
 * @Description:
 */
@Data
@ToString
public class BaseResponse<T> {

    private Integer code;

    private T data;

    private String message;

    private boolean success;

}
