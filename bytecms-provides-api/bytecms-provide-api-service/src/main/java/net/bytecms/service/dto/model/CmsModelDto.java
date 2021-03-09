package net.bytecms.service.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmsModelDto extends BaseModel {

    private static final long serialVersionUID = 1L;


    private String parentId;

    /**
     * 模型名称
     */
    @NotBlank(message = "模型名称不能为空")
    private String name;

    /**
     * 静态化模板地址
     */
    private String templatePath;

    /**
     * 分类 ID
     */
    private String categoryId;

    private Boolean hasChild;

    /**
     * 是否有用图片
     */
    private Boolean hasImages;

    /**
     * 是否拥有文件
     */
    private Boolean hasFiles;

    private Integer isUrl;

    /**
     * 默认字段
     */
    private String defaultFieldList;


    /**
     * 选中字段
     */
    private String checkedFieldList;

    /**
     * 扩展字段
     */
    private String extendFieldList;

    /**
     * 必填字段
     */
    private String requiredFieldList;

    /**
     * 字段对应中文名称
     */
    private String fieldTextMap;




}
