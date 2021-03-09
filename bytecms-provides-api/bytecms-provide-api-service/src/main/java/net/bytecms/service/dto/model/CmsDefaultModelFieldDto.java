package net.bytecms.service.dto.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author LG
 * @since 2019-10-23
 */
@Data
@Accessors(chain = true)
public class CmsDefaultModelFieldDto{

    private static final long serialVersionUID = 1L;

    private String fieldNote;

    //重命名默认等于 fieldNote
    private String reFieldNote;

    private String fieldName;

    private String fieldType;

    private String inputType;

    //最大长度
    private Integer maxlength;

    private String extendPrefix="";

    public String defaultValue;

    private Object defaultObjValue;

    // 是否允许修改必填字段
    private Boolean visibleCheck;

    // 是否必选择填字段
    private Boolean visibleSwitch;
    // 是否默认选中
    private Boolean initCheck = false;
    // 是否默认选中
    private Boolean initSwitch = false;
}
