package cn.itcode.onlineSystem.service;

import cn.itcode.onlineSystem.entity.AccountPojo;
import cn.itcode.onlineSystem.entity.AdminPojo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    //获取账户对象
    public AccountPojo getAccount(String username);
    //获取账户对象
    public AccountPojo getAccount(int accountid);
    //修改账户
    public Boolean modifyAccount(AccountPojo account);
    //从session中重新获取对象account
    public void reflush(AccountPojo account);

    //根据username获取管理员对象
    public AdminPojo getAdmin(String username);

    //启用账户
    public void enabled(int id);
    //冻结账户
    public void locking(int id);
    //删除账户
    public boolean delAccount(int id);
    //管理员开户
    public boolean addAccount(AccountPojo account);
}
