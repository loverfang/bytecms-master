package net.bytecms.service.api.category;
import net.bytecms.service.dto.category.CmsCategoryExtendDto;
import net.bytecms.core.api.BaseService;
import net.bytecms.core.utils.ApiResult;

/**
 * <p>
 * 分类扩展 服务类
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
public interface CmsCategoryExtendService extends BaseService<CmsCategoryExtendDto> {


    /**
     * 获取扩展字段展示
     * @param v
     * @return
     */
    ApiResult getExtendFieldById(String id);
}