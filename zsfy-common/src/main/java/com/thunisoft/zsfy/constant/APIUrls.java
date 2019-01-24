package com.thunisoft.zsfy.constant;

public abstract class APIUrls {
    /**
     * 版本号
     */
    private final static String VERSION = "";
    /**
     * 基础路径
     */
    private final static String BASE_URL = "http://172.18.9.80:8080/wsfy-ww/";
//    private final static String BASE_URL = "http://172.18.40.102:8089/wsfy-ww/";

    /**
     * 当事人基础路径
     */
    private final static String BASE_DSR_URL = "";

    /**
     * 法官基础路径
     */
    private final static String BASE_FG_URL = "";

    /**
     * 公用的APIS
     */
    public interface PubAPIS {
        /**
         * 获取时间戳 用于登录
         */
        String GET_CURRENTTIME = BASE_URL + "/api/authentication/getCurrentTime.htm";
        /**
         * 用于登录
         */
        String AUTHENTICATION_OF_TEMPLOGIN = BASE_URL + "/api/authentication/doTempLogin.htm";

    }

    /**
     * 当事人请求的urls
     */
    public interface DsrUrls {

    }

    /**
     * 法官请求的urls
     */
    public interface FgUrls {

    }

}
