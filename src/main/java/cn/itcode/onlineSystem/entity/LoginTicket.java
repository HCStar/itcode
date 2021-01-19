package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登陆凭证的实体类
 */
@Data
public class LoginTicket implements Serializable {
    private int id;
    private int userId;
    //登陆门票
    private String ticket;
    //登陆状态
    private int status;
    //登陆凭证的过期时间
    private Date expired;
}
