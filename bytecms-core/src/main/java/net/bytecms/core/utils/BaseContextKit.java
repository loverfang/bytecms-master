package net.bytecms.core.utils;

import java.util.*;

/**
 * @ClassName: BaseContextKit
 * @Author: LG
 * @Date: 2019/4/30 15:40
 * @Version: 1.0
 **/
public class BaseContextKit {

    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key) == null ? "" : map.get(key);
    }

    public static String getUserId() {
        Object value = get(SecurityConstants.USER_ID);
        String res = returnObjectValue(value);
        return res;
    }

    public static String getOpenId() {
        Object value = get(SecurityConstants.OPEN_ID);
        String res = returnObjectValue(value);
        return res;
    }

    public static String getUserName() {
        Object value = get(SecurityConstants.USER_NAME);
        String res = returnObjectValue(value);
        return res;
    }

    public static String getName() {
        Object value = get(SecurityConstants.NAME);
        String res = returnObjectValue(value);
        return res;
    }

    public static String getDeptId() {
        Object value = get(SecurityConstants.DEPT_ID);
        String res = returnObjectValue(value);
        return Checker.BeBlank(res) ? null : res;
    }

    public static List<String> getRoleSigns() {
        List<String> roleSigns = new ArrayList<>(16);
        Object value = get(SecurityConstants.ROLE_SIGNS);
        if (Checker.BeNotNull(value) && value instanceof List) {
            roleSigns = (List<String>) value;
        }
        return roleSigns;
    }

    public static void setDeptId(String groupID) {
        set(SecurityConstants.DEPT_ID, groupID);
    }

    public static void setUserId(String userID) {
        set(SecurityConstants.USER_ID, userID);
    }

    public static void setUserName(String userName) {
        set(SecurityConstants.USER_NAME, userName);
    }

    public static void setName(String name) {
        set(SecurityConstants.NAME, name);
    }

    public static void setRoleSigns(Collection<String> roleSigns) {
        set(SecurityConstants.ROLE_SIGNS, roleSigns);
    }

    public static void setOpenId(String openId) {
        set(SecurityConstants.OPEN_ID, openId);
    }

    private static String returnObjectValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
