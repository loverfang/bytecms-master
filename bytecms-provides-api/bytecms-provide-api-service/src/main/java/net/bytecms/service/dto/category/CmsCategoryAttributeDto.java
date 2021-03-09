package net.bytecms.service.dto.category;

import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * <p>
 * 分类扩展
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Data
@Accessors(chain = true)
public class CmsCategoryAttributeDto extends BaseModel {

    private static final long serialVersionUID = 1L;

        /**
        * 分类ID
        */
        private String categoryId;


        /**
        * 标题(分类标题用于 SEO 关键字优化)
        */
        private String title;


        /**
        * 关键词用于 SEO 关键字优化
        */
        private String keywords;


        /**
        * 描述用于 SEO 关键字优化
        */
        private String description;


        /**
        * 数据JSON
        */
        private String data;











}
