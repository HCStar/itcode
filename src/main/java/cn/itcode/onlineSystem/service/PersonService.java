package cn.itcode.onlineSystem.service;

import cn.itcode.onlineSystem.entity.PersonPojo;
import cn.itcode.onlineSystem.entity.StatusPojo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PersonService {
    //修改个人信息
    public boolean modifyPersoninfo(PersonPojo personinfo);

    //管理员根据账户状态查询用户信息
    public List searchPersoninfo(StatusPojo status);

    //管理员根据用户真实名字查询用户信息
    public List searchPersoninfo(PersonPojo personinfo);

    //管理员开户（个人信息）
    public boolean add(PersonPojo personinfo);
}
