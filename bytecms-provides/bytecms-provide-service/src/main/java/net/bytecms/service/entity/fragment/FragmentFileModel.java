package net.bytecms.service.entity.fragment;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 页面片段文件模型
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_fragment_file_model")
public class FragmentFileModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */

    @TableId("id")
    private String id;


    /**
     * 别名
     */

    @TableField("alias")
    private String alias;
    /**
     * 编码
     */

    @TableField("code")
    private String code;

    /**
     * 展示条数
     */

    @TableField("size")
    private Integer size;


    /**
     * 默认字段
     */

    @TableField("default_field_list")
    private String defaultFieldList;


    /**
     * 选中的字段json
     */

    @TableField("checked_field_list")
    private String checkedFieldList;


    /**
     * 必填字段
     */

    @TableField("required_field_list")
    private String requiredFieldList;


    /**
     * 扩展字段
     */

    @TableField("extend_field_list")
    private String extendFieldList;


    /**
     * 字段对应中文名称
     */

    @TableField("field_text_map")
    private String fieldTextMap;


    /**
     * 文件类型（文件还是文件夹）
     */

    @TableField("file_name")
    private String fileName;


    /**
     * 路径
     */

    @TableField("file_path")
    private String filePath;


}
