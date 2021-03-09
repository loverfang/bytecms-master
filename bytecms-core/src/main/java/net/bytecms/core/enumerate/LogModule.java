package net.bytecms.core.enumerate;

import lombok.Getter;

public enum LogModule {
    DEFAULT("SYSTEM", "系统日志"),
    CONTENT("CONTENT", "内容管理模块"),
    JOB("JOB", "JOB管理模块"),
    TASK("TASK", "任务计划管理模块"),
    SITE_FILE("SITE_FILE", "站点文件管理模块"),
    TEMPLATES("TEMPLATES", "模板文件管理模块"),
    FRAGMENT("FRAGMENT", "页面片段管理模块"),
    TAG("TAG", "标签管理模块"),
    SITE("SITE", "站点配置管理模块"),
    MODEL("MODEL", "模型管理模块"),
    LOG("LOG", "日志管理模块"),
    CATEGORY("CATEGORY", "栏目分类管理模块"),
    CATEGORY_EXTEND("CATEGORY_EXTEND", "栏目分类扩展管理模块"),

    USER("USER", "用户管理模块"),
    ROLE("ROLE", "角色管理模块"),
    MENU("MENU", "菜单管理模块"),
    ORG("ORG", "组织管理模块"),
    ;
    @Getter
    private String name;
    @Getter
    private String code;
    LogModule(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
