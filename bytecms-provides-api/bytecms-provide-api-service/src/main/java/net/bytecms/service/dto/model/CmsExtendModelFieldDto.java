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
public class CmsExtendModelFieldDto {

    private static final long serialVersionUID = 1L;


    private String code; //字段名

    private String inputType; //input 类型

    private String name;// 名称

    private Boolean isRequired;//是否必填

    private Boolean isSearch; //是否可搜索

    private String defaultValue;//默认值

    private Integer maxlength;//最大长度

    private String description ;//描述

    private String sort ;//排序
}
