package cn.itcode.onlineSystem.util;

//用于得到某个实体在Redis的key
public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_TICKET = "ticket";


    //拼登录凭证的key
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;

    }

    //拼用户的key
    public static String getUserKey(int userId) {
        return PREFIX_TICKET + SPLIT + userId;

    }

}