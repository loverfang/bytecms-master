package net.bytecms.service.dto.content;
import net.bytecms.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * <p>
 * 内容推荐
 * </p>
 *
 * @author LG
 * @since 2019-12-12
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmsContentRelatedDto extends BaseModel {

    private static final long serialVersionUID = 1L;

        private String id;


        /**
        * 当前文章内容id
        */
        private String contentId;


        /**
        * 推荐内容id
        */
        private String relatedContentId;


        /**
        * 推荐用户
        */
        private String userId;


        /**
        * 推荐链接地址
        */
        private String url;


        /**
        * 推荐标题
        */
        private String title;


        /**
        * 推荐简介
        */
        private String description;


        /**
        * 点击数
        */
        private Integer clicks;


        /**
        * 排序
        */
        private Integer sort;











}
