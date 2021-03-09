package net.bytecms.service.service.category;

import net.bytecms.service.api.category.CmsCategoryAttributeService;
import net.bytecms.service.api.category.CmsCategoryExtendService;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.service.dto.category.CmsCategoryExtendDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.entity.category.CmsCategoryExtend;
import net.bytecms.service.mapper.category.CmsCategoryExtendMapper;
import net.bytecms.service.utils.DynamicFieldUtil;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 分类扩展 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Service
public class CmsCategoryExtendServiceImpl extends BaseServiceImpl<CmsCategoryExtendDto, CmsCategoryExtend, CmsCategoryExtendMapper> implements CmsCategoryExtendService {

    @Autowired
    CmsCategoryService cmsCategoryService;

    @Autowired
    CmsCategoryAttributeService attributeService;

    @Transactional
    @Override
    public boolean deleteByPk(String id) {
        List<CmsCategoryDto> cmsCategoryDtos=cmsCategoryService.listByField("category_extend_id",id);
        if(Checker.BeNotEmpty(cmsCategoryDtos)){
            cmsCategoryDtos.forEach(cmsCategoryDto->{
                cmsCategoryDto.setCategoryExtendId("");
            });
            cmsCategoryService.updateByPks(cmsCategoryDtos);
        }
        return super.removeById(id);
    }

    @Override
    public ApiResult getExtendFieldById(String id) {
         ApiResult apiResult = ApiResult.result();
         CmsCategoryExtendDto categoryExtend = getByPk(id);
         List<CmsDefaultModelFieldDto> allFields= DynamicFieldUtil.formaterField(null,categoryExtend.getExtendFieldList());
         apiResult.put("extendField",allFields);
         return apiResult;
    }
}
