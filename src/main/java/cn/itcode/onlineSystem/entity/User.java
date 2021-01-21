package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    //用户编号
    private String userId;
    private String userName;
    private String password;
    //+salt
    private String salt;
    //用户邮件
    private String email;
    //用户类型
    private int userType;
    //用户状态
    private int userStatus;
    //注册时间
    private Date createTime;
    //新密码
    private String newPassword;
    //确认密码
    private String confirmPassword;
    //卡号
    private Long custAcct;
    //地址
    private String address;
    //电话号码
    private String teleNum;
    //用户真实姓名
    private String realName;
}
