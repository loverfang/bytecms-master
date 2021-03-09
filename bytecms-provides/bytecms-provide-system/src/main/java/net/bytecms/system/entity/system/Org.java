package net.bytecms.system.entity.system;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_org")
public class Org  extends BaseModel {
    private static final long serialVersionUID = -8612167154396486091L;

    /**
     * 部门id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 部门名称
     */
    @TableField("org_name")
    private String orgName;
    /**
     * 部门编码
     */
    @TableField("org_code")
    private String orgCode;
    /**
     * 父级部门id
     */
    @TableField("parent_id")
    private String parentId;


    @TableField("level")
    private String level;
    /**
     * 父级部门编码
     */
    @TableField("parent_code")
    private String parentCode;
    /**
     * 备注
     */
    @TableField("comment")
    private String comment;

}

