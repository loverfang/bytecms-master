package net.bytecms.service.entity.category;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门分类
 * </p>
 *
 * @author LG
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_org_category")
public class OrgCategory extends BaseModel<OrgCategory> {

	private static final long serialVersionUID = 1L;

	@TableId("id")
	private String id;

	/**
	 * 部门ID
	 */

	@TableField("org_id")
	private String orgId;


	/**
	 * 0：半选中 1：全选中
	 */
	@TableField("half_checked")
	private Integer halfChecked;


	/**
	 * 分类ID
	 */

	@TableField("category_id")
	private String categoryId;
}
