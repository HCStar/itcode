package cn.itcode.onlineSystem.constans;

//常量接口
public interface CommonConstant {
    //激活成功
    int ACTIVATION_SUCCESS=0;
    //待激活
    int ACTIVATION_REPEAT=1;
    //无效卡
    int ACTIVATION_FAILURE =2;

    //默认状态的登陆凭证持续时间
    int DEFAULT_EXPIRED_SECONDS=3600*12;
    //按了”记住我“的登陆凭证持续时间
    int REMEMBER_EXPIRED_SECONDS=3600*24*100;
    //系统用户id
    int SYSTEM_USER_ID = 1;
    //普通用户
    String AUTHORITY_USER = "user";
    //管理员
    String AUTHORITY_ADMIN = "admin";
}
