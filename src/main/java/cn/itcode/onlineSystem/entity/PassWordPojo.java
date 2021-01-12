package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PassWordPojo implements Serializable {
    //原始密码
    private String oldpwd;
    //新密码
    private String newpwd;
    //确认密码
    private String confirmpwd;
}
