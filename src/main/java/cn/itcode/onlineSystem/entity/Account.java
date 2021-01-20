package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账户实体类
 */
@Data
public class Account implements Serializable {
    //账户账号
    private String accountNum;
    //用户名
    private String username;
    //密码
    private	String password;
    //账户金额
    private BigDecimal accountBalance;
    //账户状态
    private int status;
    //
}
