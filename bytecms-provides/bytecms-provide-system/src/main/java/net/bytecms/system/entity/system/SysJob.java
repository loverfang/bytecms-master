package net.bytecms.system.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author LG
 * @since 2019-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_job")
public class SysJob extends BaseModel<SysJob> {

    private static final long serialVersionUID = 1L;


    @TableId("id")
    private String id;


    /**
     * 任务名称
     */

    @TableField("job_name")
    private String jobName;


    /**
     * 任务组
     */

    @TableField("job_group")
    private String jobGroup;


    /**
     * 任务表达式
     */

    @TableField("cron_expression")
    private String cronExpression;


    /**
     * 任务描述
     */

    @TableField("job_desc")
    private String jobDesc;


    /**
     * 任务状态
     */

    @TableField("job_status")
    private String jobStatus;


    /**
     * job class
     */

    @TableField("job_class")
    private String jobClass;


    /**
     * jobParam 执行参数
     */

    @TableField("job_param")
    private String jobParam;


}
