package net.bytecms.service.dto.fragment;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 页面片段文件模型
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FragmentFileModelDto extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;


    /**
     * 别名
     */
    @NotBlank(message = "名称不能为空")
    private String alias;


    /**
     * 别名
     */
    @NotBlank(message = "编码不能为空")
    private String code;


    /**
     * 展示条数
     */
    @NotNull(message = "数据条数不能为空")
    private Integer size;


    /**
     * 默认字段
     */
    private String defaultFieldList;


    /**
     * 选中的字段json
     */
    private String checkedFieldList;


    /**
     * 必填字段
     */
    private String requiredFieldList;


    /**
     * 扩展字段
     */
    private String extendFieldList;


    /**
     * 字段对应中文名称
     */
    private String fieldTextMap;


    /**
     * 文件类型（文件还是文件夹）
     */
    private String fileName;


    /**
     * 路径
     */
    private String filePath = "";


    //@NotEmpty(message = "内容系统字段不能为空")
    private List<CmsDefaultModelFieldDto> modelFieldDtos;


}
