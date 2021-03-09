package net.bytecms.service.service.category;

import net.bytecms.service.api.category.CmsCategoryAttributeService;
import net.bytecms.service.dto.category.CmsCategoryAttributeDto;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.service.entity.category.CmsCategoryAttribute;
import net.bytecms.service.mapper.category.CmsCategoryAttributeMapper;
import net.bytecms.core.model.PageKeyWord;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.Checker;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类扩展 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Transactional
@Service
public class CmsCategoryAttributeServiceImpl extends BaseServiceImpl<CmsCategoryAttributeDto, CmsCategoryAttribute, CmsCategoryAttributeMapper> implements CmsCategoryAttributeService {


    @Override
    public Map<String, PageKeyWord> listPageKeyWordByCategorys(List<CmsCategoryDto> cmsCategoryDtos) {
        Map<String, PageKeyWord> params = new HashMap<>(16);
        if (Checker.BeNotEmpty(cmsCategoryDtos)) {
            List<String> categoryIds = new ArrayList<>(16);
            for (CmsCategoryDto cmsCategoryDto : cmsCategoryDtos) {
                categoryIds.add(cmsCategoryDto.getId());
            }
            List<CmsCategoryAttributeDto> attributeDtos = listByField("category_id", categoryIds);
            if (Checker.BeNotEmpty(attributeDtos)) {
                for (CmsCategoryAttributeDto categAttr : attributeDtos) {
                      PageKeyWord pageKeyWord =new PageKeyWord();
                      pageKeyWord.setTitle(categAttr.getTitle()).setDescription(categAttr.getDescription()).setKeywords(categAttr.getKeywords());
                      params.put(categAttr.getCategoryId(),pageKeyWord);
                }
            }
        }
        return params;
    }
}
