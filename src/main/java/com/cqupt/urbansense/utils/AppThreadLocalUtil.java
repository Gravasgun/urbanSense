package com.cqupt.urbansense.utils;


import com.cqupt.urbansense.bean.User;

public class AppThreadLocalUtil {
    private static final ThreadLocal<User> APP_USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 将用户信息存入线程
     *
     * @param user
     */
    public static void setUser(User user) {
        APP_USER_THREAD_LOCAL.set(user);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static User getUser() {
        return APP_USER_THREAD_LOCAL.get();
    }

    /**
     * 清理线程中的用户信息
     */
    public static void clear() {
        APP_USER_THREAD_LOCAL.remove();
    }
}
