package net.bytecms.service.dto.content;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * <p>
 * 内容扩展
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Data
@Accessors(chain = true)
public class ContentAttributeDto extends BaseModel {

    private static final long serialVersionUID = 1L;

        private String contentId;


        /**
        * 内容来源
        */
        private String source;


        /**
        * 来源地址
        */
        private String sourceUrl;


        /**
        * 数据JSON
        */
        private String data;


        /**
        * 全文索引文本
        */
        private String searchText;


        /**
        * 内容
        */
        private String text;


        /**
        * 字数
        */
        private Integer wordCount;











}
