package net.bytecms.system.dto.log;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
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
@Accessors(chain = true)
public class LogDTO extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 日志类型：0:普通日志 1:异常日志
     */
    private String type;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 登录名
     */
    private String userId;

    /**
     * 用户账号
     */
    private String username;



    /**
     * 用户名
     */
    private String name;

    /**
     * 用户操作
     */
    private String operation;

    /**
     * 响应时间
     */
    private Integer time;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * IP地址
     */
    private String ip;


}
