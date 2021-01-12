package cn.itcode.onlineSystem.dao;

import cn.itcode.onlineSystem.entity.PersonPojo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonMapper {
    //修改个人信息
    public void modifyPersoninfo(PersonPojo personinfo);

    //管理员获取所有用户的信息
    public List getAllPersoninfo();

    //管理员根据账户状态查询用户信息，参数id为管理员页面超链接上面的id值
    public List searchPersoninfo(int id);

    //管理员根据用户真实名字查询用户信息
    public List searchPersoninfo(PersonPojo personinfo);

    //管理员开户（个人信息）
    public boolean add(PersonPojo personinfo);
}
