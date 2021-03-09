package net.bytecms.freemark.enums;

import lombok.Getter;
import net.bytecms.freemark.jobs.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public enum JobActionNotify {
    JOB_HOME_PAGE ("业务触发首页更新", "group1", 1, HomePageGenJob.class),
    CATEGORY_LIST_PAGE ("业务触发分类列表更新", "group1", 1, CategoryListGenJob.class),
    CONTENT_PUBLISH ("定时发文", "group1", 1, ContentPublishJob.class),
    CONTENT_CLICK_AND_GIVELIKE_JOB ("定时发文", "group1", 0, ContentClickAndGiveLikeJob.class),

    JOB_CATEGORY_LIST_PAGE_RIGHT_NOW ("手动触发分类列表更新", "group2", 0,CategoryListGenJob.class),
    JOB_HOME_PAGE_RIGHT_NOW ("手动触发首页更新", "group2", 0,HomePageGenJob.class),
    JOB_WHOLE_SITE_RIGHT_NOW ("手动触发全站更新", "group2", 0, WholeSiteGenJob.class),
    JOB_CONTENT_LIST_RIGHT_NOW ("手动触发文章详情页更新", "group2", 0, ContentListGenJob.class),
    JOB_SOLR_SYNC_DATA_RIGHT_NOW ("手动触发同步solr更新", "group2", 0, SolrSyncDataJob.class),
    JOB_FIXED_TEMPLATE_RIGHT_NOW ("手动触发生成指定固定页面", "group2", 0, FixedTemplateJob.class)
    ;
    @Getter
    private String name;
    @Getter
    private String group;
    @Getter
    private int minute;
    @Getter
    private int second;

    @Getter
    private Date preciseTime;


    @Getter
    private Class<? extends QuartzJobBean> jobClass;


    JobActionNotify(String name, String group, int minute, Class<? extends QuartzJobBean> jobClass) {
        this.name = name;
        this.group = group;
        this.minute = minute;
        this.jobClass = jobClass;
    }

    JobActionNotify(String name, String group, int minute, int second, Class<? extends QuartzJobBean> jobClass) {
        this.name = name;
        this.group = group;
        this.minute = minute;
        this.jobClass = jobClass;
        this.second = second;
    }

    public  JobActionNotify setMinute(int minute){
        this.minute = minute;
        return this;
    }

    public  JobActionNotify setSecond(int second){
        this.second = second;
        return this;
    }

    public JobActionNotify setName(String name){
        this.name = name;
        return this;
    }

    public JobActionNotify setGroup(String group){
        this.group = group;
        return this;
    }

    public JobActionNotify setPreciseTime(Date preciseTime){
        this.preciseTime = preciseTime;
        return this;
    }
}
