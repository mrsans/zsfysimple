package com.thunisoft.dao;

import com.thunisoft.bean.WxAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author ZhPJ
 * @Date 2019/1/17 001714:02
 * @Version 1.0
 * @Description:
 */
@Mapper
public interface WxAccountDao {

    @Select("select * from wxaccount where username=#{username}")
    WxAccount login(String username);

}
