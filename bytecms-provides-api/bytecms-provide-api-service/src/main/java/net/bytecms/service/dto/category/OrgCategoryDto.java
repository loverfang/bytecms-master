package net.bytecms.service.dto.category;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgCategoryDto extends BaseModel<OrgCategoryDto> {

    private static final long serialVersionUID = 1L;

    private String id;


    /**
     * 部门ID
     */
    private String orgId;


    /**
     * 0：半选中 1：全选中`
     */
    private Integer halfChecked;


    /**
     * 分类ID
     */
    private String categoryId;


    private List<String> onCheckKeys;//全选中

    private List<String> halfCheckedKeys;//半选中


}
