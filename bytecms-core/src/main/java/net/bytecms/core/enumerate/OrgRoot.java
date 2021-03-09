package net.bytecms.core.enumerate;

import lombok.Getter;
public enum OrgRoot {
    DEFAULT("0", "根组织"),
    ;
    @Getter
    private String name;
    @Getter
    private String code;
    OrgRoot(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
