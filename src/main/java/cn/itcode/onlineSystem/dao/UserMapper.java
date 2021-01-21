package cn.itcode.onlineSystem.dao;

import cn.itcode.onlineSystem.entity.Account;
import cn.itcode.onlineSystem.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //根据ID查询用户
    User selectByID(String userId);
    //根据名字查询用户
    User selectByName(String userName);
    //根据用户名更新用户
    User updateUser(String userName);
    //Email查询用户
    User selectByEmail(String email);
    //增加用户，返回插入行数
    int insertUser(User user);
    //修改User状态Status，返回修改的条数，修改了几条
    int updateStatus(int id,int userStatus);
    //更新密码
    int updatePassword(String id,String password);
    //获取用户类型
    int getRole(String username, String password);

    //根据账号获取账户对象
    Account getAccount(String accountNum);
    //修改账户
    boolean updateAccount(Account account);

    //根据账户状态名称获取账户状态对象
    int getStatus(String name);
    //根据账户状态id获取账户状态对象
    int getStatus(int id);

    //管理员删除账户
    boolean delAccount(Account account);
    //开户
    boolean addAccount(Account account);
}
