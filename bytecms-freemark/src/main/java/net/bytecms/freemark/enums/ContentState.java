package net.bytecms.freemark.enums;

import lombok.Getter;

public enum ContentState {
    ROUGH ("0", "草稿"),
    PUBLISH ("1", "发布"),
    DELETE ("2", "删除"),
    ;
    @Getter
    private String name;
    @Getter
    private String code;
    ContentState(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
