package net.bytecms.service.entity.content;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * <p>
 * 内容附件
 * </p>
 *
 * @author LG
 * @since 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_content_file")
public class ContentFile extends BaseModel {

private static final long serialVersionUID = 1L;


    /**
     * 内容
     */

        @TableField("content_id")
        private String contentId;


    /**
     * 用户
     */

        @TableField("user_id")
        private String userId;


    /**
     * 关联资源表
     */

        @TableField("sys_resource_id")
        private String sysResourceId;



        @TableField("file_path")
        private String filePath;



        @TableField("file_full_path")
        private String fileFullPath;



        @TableField("file_name")
        private String fileName;

        @TableField("path")
        private String path;




    /**
     * 下载数
     */

        @TableField("downs")
        private Integer downs;


    /**
     * 排序
     */

        @TableField("sort")
        private Integer sort;


    /**
     * 描述
     */

        @TableField("description")
        private String description;


}
