package cn.itcode.onlineSystem.service.impl;

import cn.itcode.onlineSystem.dao.UserMapper;
import cn.itcode.onlineSystem.entity.AccountPojo;
import cn.itcode.onlineSystem.entity.AdminPojo;
import cn.itcode.onlineSystem.entity.StatusPojo;
import cn.itcode.onlineSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    //根据用户名获取账户对象
    @Override
    public AccountPojo getAccount(String username) {
        return userMapper.getAccount(username);
    }


    //根据账户id获取账户对象
    @Override
    public AccountPojo getAccount(int accountid) {
        return null;
    }

    //修改账户信息
    @Override
    public Boolean modifyAccount(AccountPojo account) {
        return userMapper.updateAccount(account);
    }

    //从session中重新获取对象account
    @Override
    public void reflush(AccountPojo account) {
        userMapper.reflush(account);
    }

    //获取管理员对象
    @Override
    public AdminPojo getAdmin(String username) {
        return userMapper.getAdmin(username);
    }

    //启用账户
    @Override
    public void enabled(int id) {
        AccountPojo accountPojo = userMapper.getAccount(id);
        //获取并修改账户对象的状态属性，设置为启用
        StatusPojo status = userMapper.getStatus("启用");
        accountPojo.setStatus(status);
        userMapper.updateAccount(accountPojo);
    }

    //冻结账户
    @Override
    public void locking(int id) {
        AccountPojo account = userMapper.getAccount(id);
        //获取并修改账户对象的状态属性，设置为启用
        StatusPojo status = userMapper.getStatus("启用");
        account.setStatus(status);
        userMapper.updateAccount(account);
    }

    //管理员删除账户
    @Override
    public boolean delAccount(int id) {
        AccountPojo account = userMapper.getAccount(id);
        userMapper.delAccount(account);
        return false;
    }

    //管理员开户
    @Override
    public boolean addAccount(AccountPojo account) {
        StatusPojo status = userMapper.getStatus("启用");
        account.setStatus(status);
        return userMapper.addAccount(account);
    }
}
