package net.bytecms.freemark.corelibs.observer;

import lombok.Getter;

/**
 * 观察行为(以下行为将影响网站页面的生成)
 */
public enum ObserverAction {
    CONTENT_PUBLISH("CONTENT_PUBLISH", "发布文章"),
    CONTENT_DELETE("CONTENT_DELETE", "删除文章"),
    CONTENT_CREATE("CONTENT_CREATE", "创建文章"),
    CONTENT_UN_CREATE("CONTENT_UN_CREATE", "下架文章"),
    CATEGORY_CHANGE_PAGE_NUM("CATEGORY_CHANGE_PAGE_NUM", "修改分类(栏目)分页数"),
    CATEGORY_CHANGE_ADD("CATEGORY_CHANGE_ADD","新增分类"),
    CATEGORY_CHANGE_DELETE("CATEGORY_CHANGE_ADD","删除分类"),
    CATEGORY_CHANGE_RENAME("CATEGORY_CHANGE_RENAME","分类重命名"),
    CATEGORY_CHANGE("CATEGORY_CHANGE","分类变动"),
    HOME_PAGE_CHANGE("HOME_PAGE_CHANGE","首页文件生成"),
    SOLR_CHANGE("SOLR_CHANGE","SOLR_CHANGE"),
    DEFAULT_GEN("DEFAULT_GEN","缺省执行"),
    ;

    @Getter
    private String code;

    @Getter
    private String action;

    ObserverAction(String code, String action) {
        this.code = code;
        this.action = action;
    }
}
