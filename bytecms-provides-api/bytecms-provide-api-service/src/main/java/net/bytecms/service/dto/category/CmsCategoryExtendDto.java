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
public class CmsCategoryExtendDto extends BaseModel {

    private static final long serialVersionUID = 1L;

        private String id;


        /**
        * 分类扩展名称
        */
        private String categoryExtendName;


        /**
        * 扩展字段(json 保存)
        */
        private String extendFieldList;


        /**
        * 排序
        */
        private Integer sort;











}
