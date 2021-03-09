package net.bytecms.system.entity.system;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 字典表
 * </p>
 *
 * @author LG
 * @since 2019-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_dict")
public class Dict extends BaseModel {

private static final long serialVersionUID = 1L;


    /**
     * 标签名
     */

        @TableField("name")
        private String name;


    /**
     * 数据值
     */

        @TableField("value")
        private String value;


    /**
     * 类型
     */

        @TableField("type")
        private String type;


    /**
     * 描述
     */

        @TableField("description")
        private String description;


    /**
     * 排序（升序）
     */

        @TableField("num")
        private Integer num;


    /**
     * 父级编号
     */

        @TableField("parent_id")
        private String parentId;


    /**
     * 备注信息
     */

        @TableField("remarks")
        private String remarks;


    /**
     * 删除标记 0:未删除 1：删除
     */

        @TableField("del_flag")
        private String delFlag;

}
