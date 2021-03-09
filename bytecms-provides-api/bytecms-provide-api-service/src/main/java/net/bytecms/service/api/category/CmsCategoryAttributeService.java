package net.bytecms.service.api.category;
import net.bytecms.service.dto.category.CmsCategoryAttributeDto;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.core.api.BaseService;
import net.bytecms.core.model.PageKeyWord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类扩展 服务类
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
public interface CmsCategoryAttributeService extends BaseService<CmsCategoryAttributeDto> {


    /**
     * 根据分类 获取分类页面关键字
     * @param cmsCategoryDtos
     * @return
     */
     Map<String, PageKeyWord> listPageKeyWordByCategorys(List<CmsCategoryDto> cmsCategoryDtos);

}