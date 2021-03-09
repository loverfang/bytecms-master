package net.bytecms.service.entity.category;

import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_category")
public class CmsCategory extends BaseModel {

    private static final long serialVersionUID = 1L;


    @TableId("id")
    private String id;


    /**
     * 名称
     */

    @TableField("name")
    private String name;


    /**
     * 父分类ID
     */

    @TableField("parent_id")
    private String parentId;


    /**
     * 所有子分类ID
     */

    @TableField("child_ids")
    private String childIds;


    /**
     * 多少秒后自动生成
     */

    @TableField("after_gen_second")
    private Integer afterGenSecond=20;




    /**
     * 标签分类
     */

    @TableField("tag_type_ids")
    private String tagTypeIds;


    /**
     * 编码
     */

    @TableField("code")
    private String code;


    /**
     * 模板路径
     */

    @TableField("template_path")
    private String templatePath;


    /**
     * 首页路径
     */

    @TableField("path")
    private String path;


    /**
     * 外链
     */

    @TableField("only_url")
    private Boolean onlyUrl;


    /**
     * 已经静态化
     */

    @TableField("has_static")
    private Boolean hasStatic;


    /**
     * 是否自動靜態化
     */

    @TableField("auto_gen_static")
    private Boolean autoGenStatic;


    /**
     * 是否是单页
     */
    @TableField("single_page")
    private Boolean singlePage;


    /**
     * 当需要生成页面太多时只生成的页数,此參數僅在 autoGenStatic 為 true 是生效
     */
    @TableField("max_static_page")
    private Integer maxStaticPage;


    /**
     * 首页地址
     */

    @TableField("url")
    private String url;


    /**
     * 内容路径
     */

    @TableField("content_path")
    private String contentPath;


    /**
     * 包含子分类内容
     */

    @TableField("contain_child")
    private Boolean containChild;


    /**
     * 每页数据条数
     */

    @TableField("page_size")
    private Integer pageSize;


    /**
     * 允许投稿
     */

    @TableField("allow_contribute")
    private Boolean allowContribute;


    /**
     * 顺序
     */

    @TableField("sort")
    private Integer sort;


    /**
     * 是否在首页隐藏
     */

    @TableField("hidden")
    private Boolean hidden;


    /**
     * 扩展ID
     */

    @TableField("category_extend_id")
    private String categoryExtendId;


}
