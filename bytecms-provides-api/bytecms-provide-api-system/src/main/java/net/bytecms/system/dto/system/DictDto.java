package net.bytecms.system.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
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
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictDto extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 标签名
     */
    private String name;

    /**
     * 数据值
     */
    private String value;

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序（升序）
     */
    private Integer num;

    /**
     * 父级编号
     */
    private String parentId;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 删除标记 0:未删除 1：删除
     */
    private String delFlag;


}
