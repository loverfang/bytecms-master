package net.bytecms.core.enumerate;

import lombok.Getter;

public enum LogOperation {
    DEFAULT(""),
    VIEW("页面查询操作"),
    SAVE("新增操作"),
    UPDATE("更新操作"),
    DELETE("删除操作"),
    BATCH_SAVE("批量新增操作"),
    BATCH_UPDATE("批量更新操作"),
    BATCH_DELETE("批量删除操作"),
    ;

    @Getter
    private String code;
    LogOperation(String code) {
        this.code = code;
    }
}
