package net.bytecms.core.enumerate;

import lombok.Getter;

public enum UserForm {
    USER_FORM("user_form", "user_form"),
    APP_USER("0", "小程序用户"),
    PLAT_USER("1", "平台管理用户"),
    ;
    @Getter
    private String name;
    @Getter
    private String code;
    UserForm(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
