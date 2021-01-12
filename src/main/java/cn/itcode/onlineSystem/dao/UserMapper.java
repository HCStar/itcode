package cn.itcode.onlineSystem.dao;

import cn.itcode.onlineSystem.entity.AccountPojo;
import cn.itcode.onlineSystem.entity.AdminPojo;
import cn.itcode.onlineSystem.entity.StatusPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    //获取账户对象
    public AccountPojo getAccount(String username);
    //获取账户对象
    public AccountPojo getAccount(int accountid);
    //修改账户
    public boolean updateAccount(AccountPojo account);
    //从session中重新获取对象account
    public void reflush(AccountPojo account);

    //根据username获取管理员对象
    public AdminPojo getAdmin(String username);

    //根据账户状态名称获取账户状态对象
    public StatusPojo getStatus(String name);
    //根据账户状态id获取账户状态对象
    public StatusPojo getStatus(int id);

    //管理员删除账户
    public boolean delAccount(AccountPojo account);
    //开户
    public boolean addAccount(AccountPojo account);
}
