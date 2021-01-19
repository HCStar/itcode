package cn.itcode.onlineSystem.dao;

import cn.itcode.onlineSystem.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
//    //获取账户对象
//    public AccountPojo getAccount(String username);
//    //获取账户对象
//    public AccountPojo getAccount(int accountid);
//    //修改账户
//    public boolean updateAccount(AccountPojo account);
//    //从session中重新获取对象account
//    public void reflush(AccountPojo account);
//
//    //根据username获取管理员对象
//    public AdminPojo getAdmin(String username);
//
//    //根据账户状态名称获取账户状态对象
//    public StatusPojo getStatus(String name);
//    //根据账户状态id获取账户状态对象
//    public StatusPojo getStatus(int id);
//
//    //管理员删除账户
//    public boolean delAccount(AccountPojo account);
//    //开户
//    public boolean addAccount(AccountPojo account);

    //根据ID查询用户
    User selectByID(int id);
    //根据名字查询用户
    User selectByName(String username);
    //根据用户名更新用户
    User updateUser(String username);
    //Email查询用户
    User selectByEmail(String email);
    //增加用户，返回插入行数
    int insertUser(User user);
    //修改User状态Status，返回修改的条数，修改了几条
    int updateStatus(int id,int status);
    //更新密码
    int updatePassword(int id,String password);
    //获取用户类型
    int getRole(String username, String password);
}
