package cn.itcode.onlineSystem.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
public class PersonPojo implements Serializable {
    private Integer id;
    private Account account;
    private String realname;
    private Integer age;
    private String sex;
    private Long cardid;
    private String address;
    private String telephone;
}
