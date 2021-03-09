package net.bytecms.service.api.content;
import net.bytecms.service.dto.content.CmsContentRelatedDto;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.core.api.BaseService;

import java.util.List;

/**
 * <p>
 * 内容推荐 服务类
 * </p>
 *
 * @author LG
 * @since 2019-12-12
 */
public interface CmsContentRelatedService extends BaseService<CmsContentRelatedDto> {


    void saveRelated(String id, List<CmsContentRelatedDto> cmsContentRelateds);

    List<ContentDto> getRelatedByContentId(String contentId, int count);
}