package cn.itcode.onlineSystem.entity;

import java.io.Serializable;
import java.util.Date;


public class User implements Serializable {
    //用户编号
    private int USER_ID;
    private String USERNAME;
    private String PASSWORD;
    //+salt
    private String SALT;
    //用户邮件
    private String EMAIL;
    //用户类型
    private int USER_TYPE;
    //用户状态
    private int USER_STATUS;
    //激活码
//    private String ACTIVATION_CODE;
    //注册时间
    private Date CREATE_TIME;
    //注销时间
//    private Date cancellTime;
    //新密码
    private String NEW_PASSWORD;
    //确认密码
    private String CONFIRM_PASSWORD;
    //卡号
    private Long CUST_ACCT;
    //地址
    private String ADDRESS;
    //电话号码
    private String TELEPHONE;
    //登录次数
    private int SIGN_COUNT;
    //用户真实姓名
    private String REAL_NAME;

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getSALT() {
        return SALT;
    }

    public void setSALT(String SALT) {
        this.SALT = SALT;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public int getUSER_TYPE() {
        return USER_TYPE;
    }

    public void setUSER_TYPE(int USER_TYPE) {
        this.USER_TYPE = USER_TYPE;
    }

    public int getUSER_STATUS() {
        return USER_STATUS;
    }

    public void setUSER_STATUS(int USER_STATUS) {
        this.USER_STATUS = USER_STATUS;
    }

    public Date getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(Date CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public String getNEW_PASSWORD() {
        return NEW_PASSWORD;
    }

    public void setNEW_PASSWORD(String NEW_PASSWORD) {
        this.NEW_PASSWORD = NEW_PASSWORD;
    }

    public String getCONFIRM_PASSWORD() {
        return CONFIRM_PASSWORD;
    }

    public void setCONFIRM_PASSWORD(String CONFIRM_PASSWORD) {
        this.CONFIRM_PASSWORD = CONFIRM_PASSWORD;
    }

    public Long getCUST_ACCT() {
        return CUST_ACCT;
    }

    public void setCUST_ACCT(Long CUST_ACCT) {
        this.CUST_ACCT = CUST_ACCT;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getTELEPHONE() {
        return TELEPHONE;
    }

    public void setTELEPHONE(String TELEPHONE) {
        this.TELEPHONE = TELEPHONE;
    }

    public int getSIGN_COUNT() {
        return SIGN_COUNT;
    }

    public void setSIGN_COUNT(int SIGN_COUNT) {
        this.SIGN_COUNT = SIGN_COUNT;
    }

    public String getREAL_NAME() {
        return REAL_NAME;
    }

    public void setREAL_NAME(String REAL_NAME) {
        this.REAL_NAME = REAL_NAME;
    }
}
