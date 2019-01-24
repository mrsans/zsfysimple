package com.thunisoft.zsfy.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.basic.consts.others.ZsfyNumberConsts;
import com.thunisoft.summer.component.crypto.CryptConsts;
import com.thunisoft.summer.component.crypto.CryptUtil;
import com.thunisoft.zsfy.yyjh.utils.MD5Utils;

import net.sf.json.JSONObject;


/**
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author YULQ
 * @date 2016-12-20 下午1:14:15
 */
public class ZsfyHttpClientUtils {

    public static final String CHARSET_DEFAULT = "UTF-8";
    
    private static final String SYSTEM_ID = "zsfy";

    private static final String SECURITY_CODE = "iTf9oHN9s19u";
    
    private static final String SYSTEM_ID_VISTOR = "com.thunisoft.sysId.zsfy";
    
    private static final String AUTHCODE_VISTOR = "aec0ebd67f5f4590b91050edd561ef3e";

    /**
     * http发送post请求
     * 
     * @param url
     * @param jsonParam
     * @return
     */
    public static JSONObject httpPost(String url, List<NameValuePair> params) {
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = new JSONObject();
        HttpPost method = new HttpPost(url);
        method.setHeader("systemid", SYSTEM_ID);
        method.setHeader("authcode", getAuthcode());
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, CHARSET_DEFAULT);
            method.setEntity(entity);
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == ZsfyNumberConsts.NUM_200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
                    jsonResult.put("success", false);
                    jsonResult.put("message", "post请求提交失败");
                    logger.error(String.format("post请求提交失败:%s" , url), e);
                }
            }
        } catch (IOException e) {
            jsonResult.put("success", false);
            jsonResult.put("message", "post请求提交失败");
            logger.error(String.format("post请求提交失败:%s" , url), e);
        } finally {
            method.releaseConnection();
        }
        return jsonResult;
    }
    
    /**
     * http发送post请求
     * 
     * @param url
     * @param jsonParam
     * @return
     */
    public static JSONObject httpPost(String url, List<NameValuePair> params, String sid) {
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = new JSONObject();
        HttpPost method = new HttpPost(url);
        method.setHeader("sid", sid);
        method.setHeader("systemid", SYSTEM_ID);
        method.setHeader("authcode", getAuthcode());
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, CHARSET_DEFAULT);
            method.setEntity(entity);
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == ZsfyNumberConsts.NUM_200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
                    jsonResult.put("success", false);
                    jsonResult.put("message", "post请求提交失败");
                    logger.error(String.format("post请求提交失败:%s",url), e);
                }
            }
        } catch (IOException e) {
            jsonResult.put("success", false);
            jsonResult.put("message", "post请求提交失败");
            logger.error(String.format("post请求提交失败:%s" , url), e);
        } finally {
            method.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * 发送Post请求
     * 
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, org.apache.commons.httpclient.NameValuePair[] params) {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("systemid", SYSTEM_ID);
        postMethod.setRequestHeader("authcode", getAuthcode());
        postMethod.addParameters((org.apache.commons.httpclient.NameValuePair[]) params);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                return postMethod.getResponseBodyAsString();
            }
            if (ZsfyNumberConsts.NUM_404 == statusCode) {
                logger.error(String.format("404 not found %s", url));
                String result = getJsonString4Error(String.format("404 not found：%s" , url));
                return result;
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (Exception e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } finally {
            postMethod.releaseConnection();
        }
        return null;
    }
    
    /**
     * 发送Post请求到人脸识别
     * 
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String postToRlsb(String url, String paramsString) throws UnsupportedEncodingException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Content-Type", "application/json");
        postMethod.setRequestEntity(new StringRequestEntity(paramsString, "application/json", "utf-8"));
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                return postMethod.getResponseBodyAsString();
            }
            if (ZsfyNumberConsts.NUM_404 == statusCode) {
                logger.error(String.format("404 not found %s" , url));
                String result = getJsonString4Error(String.format("404 not found：%s" , url));
                return result;
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (Exception e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } finally {
            postMethod.releaseConnection();
        }
        return null;
    }

    
    /**
     * 发送Post请求(访客系统， 头部不同)
     * 
     * @param url
     * @param params
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String postForVistor(String url, org.apache.commons.httpclient.NameValuePair[] params) throws NoSuchAlgorithmException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        postMethod.setRequestHeader("x-request-id", uuid);
        postMethod.setRequestHeader("x-system-id", SYSTEM_ID_VISTOR);
        String unixTimeStamp = Long.toString(new Date().getTime());
        postMethod.setRequestHeader("date", unixTimeStamp);
        String realAuthcode = MD5Utils.encrypt(SYSTEM_ID_VISTOR + AUTHCODE_VISTOR + unixTimeStamp);
        postMethod.setRequestHeader("x-system-authcode", realAuthcode);
        postMethod.addParameters((org.apache.commons.httpclient.NameValuePair[]) params);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                return postMethod.getResponseBodyAsString();
            }
            if (ZsfyNumberConsts.NUM_404 == statusCode) {
                logger.error(String.format("404 not found %s" , url));
                String result = getJsonString4Error(String.format("404 not found：%s" , url));
                return result;
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (Exception e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } finally {
            postMethod.releaseConnection();
        }
        return null;
    }
    
    /**
     * 发送Post请求
     * 
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, org.apache.commons.httpclient.NameValuePair[] params, String sid) {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("sid", sid);
        postMethod.setRequestHeader("systemid", SYSTEM_ID);
        postMethod.setRequestHeader("authcode", getAuthcode());
        postMethod.addParameters((org.apache.commons.httpclient.NameValuePair[]) params);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                return postMethod.getResponseBodyAsString();
            }
            if (ZsfyNumberConsts.NUM_404 == statusCode) {
                StringBuffer sb = new StringBuffer();
                sb.append("404 not found ");
                sb.append(url);
                logger.error(sb.toString());
                String result = getJsonString4Error(String.format("404 not found：%s" , url));
                return result;
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (Exception e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        }finally {
            postMethod.releaseConnection();
        }
        return null;
    }

    /**
     * 发送Post请求
     * 
     * @param url
     * @param params
     * @return
     */
    public static String post(HttpClient httpClient, String url, org.apache.commons.httpclient.NameValuePair[] params,
        String sid) {
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("sid", sid);
        postMethod.setRequestHeader("systemid", SYSTEM_ID);
        postMethod.setRequestHeader("authcode", getAuthcode());
        postMethod.addParameters((org.apache.commons.httpclient.NameValuePair[]) params);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                return postMethod.getResponseBodyAsString();
            }
            if (ZsfyNumberConsts.NUM_404 == statusCode) {
                logger.error(String.format("404 not found %s" , url));
                String result = getJsonString4Error(String.format("404 not found：%s" , url));
                return result;
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } catch (Exception e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常！%s" , url));
            return result;
        } finally {
            postMethod.releaseConnection();
        }
        return null;
    }

    private static String getAuthcode() {
        String authcode = CryptUtil.encrypt(CryptConsts.ALGORITHM_AES, SYSTEM_ID, SECURITY_CODE);
        authcode = DigestUtils.md5Hex(authcode);
        return authcode;
    }

    /**
     * 发送Post请求
     * 
     * @param url
     * @param params
     * @return
     */
    public static InputStream postForGetInputStream(String url, org.apache.commons.httpclient.NameValuePair[] params) {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("systemid", SYSTEM_ID);
        postMethod.setRequestHeader("authcode", getAuthcode());
        postMethod.addParameters((org.apache.commons.httpclient.NameValuePair[]) params);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                return inputStream;
            }
            if (ZsfyNumberConsts.NUM_404 == statusCode) {
                logger.error(String.format("404 not found %s" , url));
                return null;
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            return null;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            return null;
        } catch (Exception e) {
            logger.error("http请求异常", e);
            return null;
        }
        return null;
    }
    
    /**
     * 发送GET请求
     * 
     * @param url
     * @param params
     * @return
     */
    public static InputStream getForGetInputStream(String url) {
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                InputStream inputStream = getMethod.getResponseBodyAsStream();
                return inputStream;
            }
            if (ZsfyNumberConsts.NUM_404 == statusCode) {
                logger.error(String.format("404 not found %s" , url));
                return null;
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            return null;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            return null;
        } catch (Exception e) {
            logger.error("http请求异常", e);
            return null;
        }
        return null;
    }
    
    /**
     * 发送Post请求
     * 
     * @param url
     * @param params
     * @return
     */
    public static InputStream postForGetInputStream(String url, org.apache.commons.httpclient.NameValuePair[] params,
        String sid) {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("systemid", SYSTEM_ID);
        postMethod.setRequestHeader("authcode", getAuthcode());
        postMethod.setRequestHeader("sid", sid);
        postMethod.addParameters((org.apache.commons.httpclient.NameValuePair[]) params);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                return inputStream;
            }
            if (ZsfyNumberConsts.NUM_404 == statusCode) {
                logger.error(String.format("404 not found %s" , url));
                return null;
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            return null;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            return null;
        } catch (Exception e) {
            logger.error("http请求异常", e);
            return null;
        }
        return null;
    }

    private static String getJsonString4Error(String message) {
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("success", false);
        jsonResult.put("message", message);
        return jsonResult.toString();

    }
    
    
    
    /**
     * 临时增加的httppost方法，主要用于现场环境无法访问服务器的问题
     * @param url
     * @param params
     * @return
     */
    public static String httpPost(String url,
            org.apache.commons.httpclient.NameValuePair[] params,String sid) {

        try {
            HttpPost postM = new HttpPost(url);
            postM.addHeader("systemid", SYSTEM_ID);
            postM.addHeader("authcode", getAuthcode());
            if(StringUtils.isNotBlank(sid)) {
                postM.addHeader("sid",sid);
            }
            
            List<org.apache.http.NameValuePair> paramsM = new ArrayList<>();
            for(org.apache.commons.httpclient.NameValuePair item:params) {
                org.apache.http.NameValuePair param = new BasicNameValuePair(item.getName(), item.getValue());
                paramsM.add(param);
            }
            postM.setEntity(new UrlEncodedFormEntity(paramsM, "UTF-8"));
            String resultStr = CoreHttpUtil.post(postM, new StringResponseHandler() {
                @Override
                public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
                    StatusLine line = response.getStatusLine();
                    int statusCode = line.getStatusCode();
                    if(ZsfyNumberConsts.NUM_404 == statusCode) {
                        return null;
                    }
                    return super.handleResponse(response);
                }
            });
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause());
        }
        return null;
    }
    
    
    
    /**
     * 发送Post请求
     * 使用指定的httpClient，用户保持session的一致
     * 
     * @param request
     * @param url
     * @param params
     * @return
     */
    public static String post(HttpClient httpClient, String url,
        org.apache.commons.httpclient.NameValuePair[] params) {
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("systemid", SYSTEM_ID);
        postMethod.setRequestHeader("authcode", getAuthcode());
        postMethod.addParameters((org.apache.commons.httpclient.NameValuePair[]) params);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (ZsfyNumberConsts.NUM_200 == statusCode) {
                return postMethod.getResponseBodyAsString();
            }
        } catch (HttpException e) {
            logger.error("http请求异常", e);
            String result = getJsonString4Error(String.format("请求异常：%s" , url));
            return result;
        } catch (IOException e) {
            logger.error("http请求IO异常", e);
            String result = getJsonString4Error(String.format("请求异常：%s" , url));
            return result;
        } catch (Exception e) {
            logger.error("http请求IO异常", e);
            String result = getJsonString4Error(String.format("请求异常：%s" , url));
            return result;
        } finally {
            postMethod.releaseConnection();
        }
        String result = getJsonString4Error(String.format("请求异常：%s" , url));
        return result;
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("关闭流时出错", e2);
            }
        }
        return result.toString();
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGetForVistor(String url, String param) {
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        try {
            String urlNameString = url + "/" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("x-system-id", SYSTEM_ID_VISTOR);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            connection.setRequestProperty("x-request-id", uuid);
            String unixTimeStamp = Long.toString(new Date().getTime());
            connection.setRequestProperty("date", unixTimeStamp);
            String realAuthcode = MD5Utils.encrypt(SYSTEM_ID_VISTOR + AUTHCODE_VISTOR + unixTimeStamp);
            connection.setRequestProperty("x-system-authcode", realAuthcode);
            connection.setRequestProperty("Content-Type","application/json");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("关闭流时出错", e2);
            }
        }
        return result.toString();
    }
}
