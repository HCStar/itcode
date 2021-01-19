package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    //用户编号
    private int id;
    private String username;
    private String password;
    //+salt
    private String salt;
    //用户邮件
    private String email;
    //用户类型
    private int type;
    //用户状态
    private int status;
    //激活码
//    private String activationCode;
    //注册时间
    private Date createTime;
    //注销时间
//    private Date cancellTime;
    //新密码
    private String newpwd;
    //确认密码
    private String confirmpwd;
    //卡号
    private Long cardid;
    //地址
    private String address;
    //电话号码
    private String telephone;
    //登录次数
    private int signCount;
    //用户真实姓名
    private String realname;
}
