package net.bytecms.system.entity.log;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author lgs
 * @since 2019-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_log")
public class Log extends BaseModel {

    private static final long serialVersionUID = 1L;


    @TableId("id")
    private String id;


    /**
     * type 模块标识类型
     */

    @TableField("type")
    private String type;


    /**
     * 模块名称
     */

    @TableField("module")
    private String module;


    /**
     * 当前用户 id
     */

    @TableField("user_id")
    private String userId;


    /**
     * 用户名 账户
     */

    @TableField("username")
    private String username;


    /**
     * 用户名
     */
    @TableField("name")
    private String name;

    /**
     * 用户操作
     */

    @TableField("operation")
    private String operation;


    /**
     * 响应时间
     */

    @TableField("time")
    private Integer time;


    /**
     * 请求方法
     */

    @TableField("method")
    private String method;


    /**
     * 请求参数
     */

    @TableField("params")
    private String params;


    /**
     * IP地址
     */

    @TableField("ip")
    private String ip;


    /**
     * 创建时间
     */


}
