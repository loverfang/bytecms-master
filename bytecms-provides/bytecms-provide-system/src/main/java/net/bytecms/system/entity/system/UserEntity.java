package net.bytecms.system.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author dl
 * @since 2018-03-16
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class UserEntity extends BaseModel {


	@TableField(exist=false)
	private Long roleIds[];

    /**
     * 用户名
     */
    @TableField
    private String username;

    @TableField
    private String name;
    
    @TableField("out_date")
    private Long outDate;
    
    @TableField("secret_key")
    private String secretKey;

    /**
     * 密码
     */
    @TableField
    private String password;

    @TableField("org_id")
    private String orgId;


    @TableField("org_code")
    private String orgCode;


    @TableField("org_name")
    private String orgName;


    /**
     * 邮箱
     */
    @TableField
    private String email;

    /**
     * 手机号
     */
    @TableField
    private String mobile;

    /**
     * 状态 0:禁用，1:正常
     */
    @TableField
    private Integer status;


    /**
     * 性别
     */
    @TableField
    private Long sex;


    @TableField
    private Date psdInitDate;


}
