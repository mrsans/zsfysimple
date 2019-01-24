package com.thunisoft.zsfy.pojo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class TProUser implements java.io.Serializable {

    private static final long serialVersionUID = 8081977582545622783L;

    /**用户ID  */
    private String CId;
    /**类型  */
    private Integer NType;
    /**实体名称  */
    private String CName;
    /**登录名  */
    private String CLogin;
    /**密码  */
    private String CPassword;
    /**证件类型  */
    private String CZjlx;
    /**证件号码  */
    private String CZjhm;
    /**传真号码  */
    private String CCzhm;
    /**电子邮箱  */
    private String CDzyx;
    /**手机号码  */
    private String CPhone;
    /**联系地址  */
    private String CAddress;
    /**联系人  */
    private String CLxr;
    /**创建时间  */
    private Date DCjsj;
    /**是否实名认证  */
    private Integer NVerify;
    /**是否律师 1-是，2-否 */
    private Integer NLs;
    /**律师执业证号  */
    private String CLszyzh;
    /**律所名称  */
    private String CLsmc;
    /**是否有效 1-是，2-否 */
    private Integer NYx;
    /**更新时间  */
    private Timestamp DUpdate;
    /**创建方式 1-外网创建，2-律协系统 */
    private Integer NCjfs;
    /**律协中用户ID  */
    private String CLxUserId;
    /**律协中手机号码  */
    private String CLxSjhm;
    /**法院ID  */
    private String CFyId;
    /**是否黑名单 1-是，2-否 */
    private Integer NHmd;
    /**拉黑时间 */
    private Date DLhsj;
    /**单位名称  */
    private String CDwName;
    /**职务  */
    private String CZw;
    /**性别*/
    private Integer NXb;

    /**注册类型，通过什么注册的，用于统计*/
    private Integer NZclx;

    /**3.2.2新增字段 当事人类型 1：自然人 2：法人 3：非法人*/
    private Integer NDsrlx;

    /**3.3.1 账号使用人证件类型*/
    private Integer NSyrZjlx;

    /**3.3.1 账号使用人证件号码*/
    private String CSyrZjhm;

    /**3.3.1 账号使用人名称*/
    private String CSyrMc;

    /**3.3.1 新增字段  法定代表人名称*/
    private String CFddbrmc;

    /**3.3.1 新增字段 法定代表人职务/主要负责人职务*/
    private String CFddbrzw;

    /**律师是否关联过元典  */
    private Integer NYd;
    /** 邮箱验证过期时间*/
    private Long NOutTime;

    /**邮箱验证秘钥*/
    private String CValidataCode;

    private String CPath;

}

