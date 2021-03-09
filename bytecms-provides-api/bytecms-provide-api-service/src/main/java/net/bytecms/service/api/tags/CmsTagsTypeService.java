package net.bytecms.service.api.tags;
import net.bytecms.service.dto.tags.CmsTagsTypeDto;
import net.bytecms.core.api.BaseService;

/**
 * <p>
 * 标签类型 服务类
 * </p>
 *
 * @author LG
 * @since 2020-01-31
 */
public interface CmsTagsTypeService extends BaseService<CmsTagsTypeDto> {


    boolean deleteById(String id);

    void tagsBelong(CmsTagsTypeDto v);

    void save(CmsTagsTypeDto v);

    void update(CmsTagsTypeDto v);
}