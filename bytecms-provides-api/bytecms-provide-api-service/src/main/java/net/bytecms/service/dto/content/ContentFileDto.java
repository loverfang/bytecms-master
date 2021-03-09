package net.bytecms.service.dto.content;
import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * <p>
 * 内容附件
 * </p>
 *
 * @author LG
 * @since 2019-12-07
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentFileDto extends BaseModel {

      private static final long serialVersionUID = 1L;

        /**
        * 内容
        */
        @DirectMark
        private String contentId;

        /**
        * 用户
        */
        private String userId;
        /**
        * 关联资源表
        */

        @DirectMark
        private String sysResourceId;


        private String filePath;


        @DirectMark
        private String fileFullPath;

        @DirectMark
        private String fileName;

        private String path;

        /**
        * 下载数
        */
        private Integer downs;

        /**
        * 排序
        */
        private Integer sort;

        /**
        * 描述
        */
        private String description;

}
