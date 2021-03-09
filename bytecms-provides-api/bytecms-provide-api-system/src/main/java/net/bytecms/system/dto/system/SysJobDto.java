package net.bytecms.system.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.core.model.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 *
 * @author LG
 * @since 2019-11-19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysJobDto extends BaseModel<SysJobDto> {

    private static final long serialVersionUID = 1L;

    private String id;


    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    private String jobName;


    /**
     * 任务组
     */
    @NotBlank(message = "任务组不能为空")
    private String jobGroup;


    /**
     * 任务表达式
     */
    private String cronExpression;


    /**
     * 任务描述
     */
    private String jobDesc;


    /**
     * 任务状态
     */
    private String jobStatus;


    /**
     * job class
     */
    private String jobClass;


    /**
     * job class
     */
    private String jobParam;


    /**
     * 动作 0：重新启动任务 1：暂停任务
     */
    private String action;


}
