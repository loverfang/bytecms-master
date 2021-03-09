package net.bytecms.service.dto.tags;

import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * <p>
 * 标签
 * </p>
 *
 * @author LG
 * @since 2019-12-09
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmsTagsDto extends BaseModel {

        /**
        * 名称
        */
        @DirectMark
        private String name;

        /**
        * 分类ID
        */
        @DirectMark
        private String typeId;

        /**
         * 分类name
         */
        @DirectMark
        private String typeName;


        @DirectMark
        private Integer useCount=0;



}
