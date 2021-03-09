package net.bytecms.service.entity.fragment;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.annotation.FieldDefault;
import net.bytecms.core.constants.InputTypeEnum;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 页面片段数据表
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_fragment")
public class Fragment extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */

    @TableId("id")
    private String id;


    /**
     * 标题
     */

    @FieldDefault(fieldNote = "标题", visibleSwitch = false, visibleCheck = true, initCheck = true, initSwitch = true)
    @TableField("title")
    private String title;


    /**
     * 封面图
     */

    @FieldDefault(fieldNote = "封面",inputType = InputTypeEnum.PICTURE)
    @TableField("cover")
    private String cover;


    /**
     * 片段路径
     */

    @TableField("path")
    private String path;


    /**
     * 超链接
     */

    @FieldDefault(fieldNote = "链接")
    @TableField("url")
    private String url;


    /**
     * 排序
     */
    @FieldDefault(fieldNote = "排序", inputType = InputTypeEnum.NUMBER)
    @TableField("sort")
    private Integer sort;


    /**
     * 片段模型文件id
     */

    @TableField("fragment_file_model_id")
    private String fragmentFileModelId;


}
