package cn.itcode.onlineSystem.service.impl;

import cn.itcode.onlineSystem.dao.PersonMapper;
import cn.itcode.onlineSystem.entity.PersonPojo;
import cn.itcode.onlineSystem.entity.StatusPojo;
import cn.itcode.onlineSystem.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonMapper personMapper;

    //修改个人信息
    @Override
    public boolean modifyPersoninfo(PersonPojo personinfo) {
        personMapper.modifyPersoninfo(personinfo);
        return true;
    }

    //管理员根据账户状态查询用户信息,状态为0表示获取所有用户
    @Override
    public List searchPersoninfo(StatusPojo status) {
        List userlist = new ArrayList();
        Integer statusId = status.getId();
        if(statusId != 0){
            //若账户状态标号不为0，则根据编号获取相应的客户记录
            userlist = personMapper.searchPersoninfo(statusId);
        } else {
            //标号为0，则获取所有客户的记录
            userlist = personMapper.getAllPersoninfo();
        }
        return userlist;
    }

    //管理员根据用户真实名字查询用户信息
    @Override
    public List searchPersoninfo(PersonPojo personinfo) {
        personMapper.searchPersoninfo(personinfo);
        return null;
    }

    //管理员开户（添加个人信息）
    @Override
    public boolean add(PersonPojo personinfo) {
        personMapper.add(personinfo);
        return false;
    }
}
