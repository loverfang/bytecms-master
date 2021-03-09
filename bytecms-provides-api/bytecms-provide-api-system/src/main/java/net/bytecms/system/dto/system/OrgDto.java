package net.bytecms.system.dto.system;

import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class OrgDto extends BaseModel implements Serializable {
	private static final long serialVersionUID = -3381348353979956997L;

	/**
	 * 部门id
	 */
	private String id;
	/**
	 * 部门名称
	 */
	private String orgName;
	/**
	 * 部门编码
	 */
	private String orgCode;
	/**
	 * 父级部门id
	 */
	private String parentId;

	private String parentName;

	private String level;
	/**
	 * 父级部门编码
	 */
	private String parentCode;
	/**
	 * 备注
	 */
	private String comment;

	/**
	 * 状态
	 */
	private String status;

	private Date createTime;
	
	private Date updateTime;
	
	private String createId;
	
	private String updateId;
}
