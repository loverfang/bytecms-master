/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package net.bytecms.core.utils;

/**
 * @author lengleng
 * @date 2017-12-18
 */
public interface SecurityConstants {

    String LICENSE_NAME = "/license.dat";

    String LICENSE_NAME_SPLIT = ":";

    String LICENSE_All_DOMAIN = "*";

    String LICENSE_DOMAIN_SPLIT = "\\|";

    String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCagWXCpLlk/X3Alvr6mDoYyXtIcnd5Bbe9Tvan6+dy5MkDJ5urNVX71Fp3bObKjkDod+fth4cOOu+wEtD8MI5ycnDQZDDB5YylKhl68q6eZnMOZ20u/eG3TfaNmQwjcuSZeCxhBF99qnA+Vn67xYTHqPCBVIxbcRtghIp9EVvV6wIDAQAB";

    String SIGNATURER="signaturer";

    String AUTHORIZEDESC="authorizeDesc";

    /**
     * 密码输入错误次数
     */
    String ERROR_INPUT_PASS="error_input_pass_times_";


    /**
     * 文章点击次数
     */
    String CONTENT_CLICK="click:content_click_times_:";

    String LOGIN_DATE_FRIST = "lb-cms-cache::login_date_frist";

    /**
     * 文章点赞次数
     */
    String GIVE_LIKES="click:content_give_likes_times_:";


    //jwt 秘钥
    final String SECURITYSECRET="j83jxnjsleubf73fdsEWrtsduids";


    public static final Long expireTime=1728000L;//20 days

    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    String CLIENT_FIELDS = "client_id, client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS
            + " from sys_oauth_client_details";

    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

    /**
     * 版本定义
     */
    String VERSION = "version";

    static final String DEPT_ID = "deptId";

    static final String USER_ID = "userId";

    static final String OPEN_ID = "openId";

    static final String USER_NAME = "user_name";

    static final String NAME = "name";

    static final String ROLE_SIGNS = "role_signs";

    public static final String NOT_REQUIRED_HAVE_PERM = "NOT_REQUIRED_HAVE_PERM";

    public static final String DO_NOT_HAVE_ANY_PERM = "DO_NOT_HAVE_ANY_PERM";
}
