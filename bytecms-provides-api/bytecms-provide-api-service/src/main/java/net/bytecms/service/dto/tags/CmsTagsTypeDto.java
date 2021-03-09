package net.bytecms.service.dto.tags;
import net.bytecms.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
/**
 * <p>
 * 标签类型
 * </p>
 *
 * @author LG
 * @since 2020-01-31
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmsTagsTypeDto extends BaseModel {

    private static final long serialVersionUID = 1L;

        /**
        * 名称
        */
        private String name;


        /**
        * 标签数
        */
        private Integer count;



        List<String> tagIds;

}
