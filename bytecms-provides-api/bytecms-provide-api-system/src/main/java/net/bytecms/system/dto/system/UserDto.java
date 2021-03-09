package net.bytecms.system.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dl
 * @since 2018-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserDto extends BaseModel {

    Set<String> perms=new HashSet<>();

    @NotEmpty(message = "")
	private String roleIds[];

    private Set<String> roleSigns=new HashSet<>(16);

    private String username;

    private String name;
    
    private Long outDate;
    
    private String secretKey;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    @Length(min = 10,max = 25,message = "邮箱长度最为10~25")
    @Email(message = "请输入正确的邮箱格式")
    private String email;

    /**
     * 手机号
     */
    @Size(min = 11,max = 11,message = "请输入正确手机号11位")
    private String mobile;

    /**
     * 状态 0:禁用，1:正常
     */
    private Integer status;

    /**
     * 性别
     */
    private Long sex;

    /**
     * 出身日期
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date birth;

    private String headpic;

    private List<String> userIds;

    private Integer type;

    private String orgId;

    private String orgCode;

    private String orgName;

    private String newPass;

    private String newPassAgain;

    private String initPass;

}
