package net.bytecms.service.mapper.category;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.service.entity.category.CmsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分类 Mapper 接口
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Mapper
public interface CmsCategoryMapper extends BaseMapper<CmsCategory> {

    IPage<CmsCategoryDto> listPage(IPage<CmsCategoryDto> pages, @Param("id") String id);

    List<CmsCategoryDto> listCategoryByOrgId(@Param("orgId") String orgId);

    List<CmsCategoryDto> listCategoryByPidAndOrgId(@Param("pid") String pid, @Param("orgId") String orgId);

    CmsCategoryDto getByPk(@Param("id") Serializable id);

    CmsCategoryDto getCategoryInfoByPk(@Param("categoryId") String categoryId, @Param("categoryCode") String categoryCode);

    List<CmsCategoryDto> selectCategoryByCodes(@Param("codes") List<String> codes);
}
