package net.bytecms.service.dto.category;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmsCategoryDto extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @DirectMark
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 模型id
     */
    @NotBlank(message = "请选择模型")
    private String modelId;


    /**
     * 模型对应的 模板地址
     */
    private String modelTemplate;

    /**
     * 父分类ID
     */
    @NotBlank(message = "父分类不能为空")
    private String parentId;


    private String parentName;

    /**
     * 多少秒后自动生成
     */
    private Integer afterGenSecond=20;

    /**
     * 所有子分类ID
     */
    private String childIds;


    /**
     * 标签分类
     */
    private String tagTypeIds;


    /**
     * 编码
     */
    @DirectMark
    @NotBlank(message = "编码不能为空")
    private String code;


    /**
     * 模板路径
     */
    @NotBlank(message = "分类模板不能为空")
    private String templatePath;


    /**
     * 首页路径
     */
    @DirectMark
    private String path;


    /**
     * 外链
     */
    private Boolean onlyUrl;


    /**
     * 已经静态化
     */
    private Boolean hasStatic;


    /**
     * 是否自動鼎太華
     */
    private Boolean autoGenStatic;


    private Boolean singlePage;


    /**
     * 当需要生成页面太多时只生成的页数
     */
    private Integer maxStaticPage;


    /**
     * 首页地址
     */
    private String url;


    /**
     * 内容路径
     */
    @DirectMark
    @NotBlank(message = "访问路径不能为空")
    private String contentPath;


    /**
     * 包含子分类内容
     */
    private Boolean containChild;


    /**
     * 每页数据条数
     */
    private Integer pageSize;


    /**
     * 允许投稿
     */
    private Boolean allowContribute;


    /**
     * 顺序
     */
    private Integer sort;


    /**
     * 是否在首页隐藏
     */
    private Boolean hidden;


    /**
     * 扩展ID
     */
    private String categoryExtendId;


    @DirectMark
    private Map<String, Object> extendParam = new HashMap<>(16);

    private List<CmsDefaultModelFieldDto> extendFieldList;

    /**
     * 标题(分类标题用于 SEO 关键字优化)
     */
    @DirectMark
    private String title;


    /**
     * 关键词用于 SEO 关键字优化
     */
    @DirectMark
    private String keywords;


    /**
     * 描述用于 SEO 关键字优化
     */
    @DirectMark
    private String description;


    @DirectMark
    private Boolean defaultCheck = false;


    /**
     * 扩展属性
     */
    private String data;
}
