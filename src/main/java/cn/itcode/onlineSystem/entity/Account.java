package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户实体类
 */
@Data
public class Account implements Serializable {
    //账户账号
    private String acctId;
    //用户名
    private String custName;
    //密码
    private	String password;
    private int acctType;
    private String custNum;
    private String custAcct;
    private String acctSqnum;
    private String ccyCode;
    private String acctAttr;
    private int acctStatus;
    private String unitCode;
    private Date lastUpdate;
    private BigDecimal acctBalc;
    private Date opactDate;
    private String salt;
}
