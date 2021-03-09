package net.bytecms.service.entity.resource;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author LG
 * @since 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_resource")
public class SysResource extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 文件唯一标识
     */

    @TableField("file_uid")
    private String fileUid;


    /**
     * 文件名称
     */

    @TableField("file_name")
    private String fileName;


    /**
     * 标题
     */

    @TableField("file_md5")
    private String fileMd5;


    /**
     * 文件大小kb
     */

    @TableField("file_size")
    private Long fileSize;


    /**
     * 文件类型
     */

    @TableField("file_type")
    private String fileType;


    /**
     * 相对地址
     */

    @TableField("file_path")
    private String filePath;


    /**
     * 全地址
     */

    @TableField("file_full_path")
    private String fileFullPath;


    /**
     * 组
     */

    @TableField("group_name")
    private String groupName;


    /**
     * 业务类型所属哪个模块的
     */

    @TableField("business_type")
    private String businessType;


    /**
     * 原始名称
     */

    @TableField("org_name")
    private String orgName;


    /**
     * 更新人名称
     */

    @TableField("modified_name")
    private String modifiedName;


    /**
     * 创建人名称
     */

    @TableField("create_name")
    private String createName;


}
