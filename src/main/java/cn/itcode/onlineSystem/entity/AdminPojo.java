package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminPojo implements Serializable {
    //管理员id
    private Integer id;
    //管理员用户名
    private String username;
    //管理员密码
    private String password;
}
