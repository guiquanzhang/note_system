package com.cloudnote.context;

/**
 * 用户上下文
 * 用于在当前线程中存储用户信息
 */
public class UserContext {
    
    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();
    
    /**
     * 设置当前用户ID
     */
    public static void setUserId(Integer userId) {
        USER_ID.set(userId);
    }
    
    /**
     * 获取当前用户ID
     */
    public static Integer getUserId() {
        return USER_ID.get();
    }
    
    /**
     * 清除当前用户信息
     */
    public static void clear() {
        USER_ID.remove();
    }
}
