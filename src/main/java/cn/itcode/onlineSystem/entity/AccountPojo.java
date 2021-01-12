package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountPojo implements Serializable {
    //账户id
    private Integer accountid;
    //用户名
    private String username;
    //密码
    private	String password;
    private Double balance;
    //账户状态
    private StatusPojo status;
}
