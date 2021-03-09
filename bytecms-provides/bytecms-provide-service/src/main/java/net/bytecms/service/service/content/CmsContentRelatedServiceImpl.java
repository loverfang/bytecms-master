package net.bytecms.service.service.content;

import net.bytecms.service.api.content.CmsContentRelatedService;
import net.bytecms.service.dto.content.CmsContentRelatedDto;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.service.entity.content.CmsContentRelated;
import net.bytecms.service.mapper.content.CmsContentRelatedMapper;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 内容推荐 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-12-12
 */
@Transactional
@Service
public class CmsContentRelatedServiceImpl extends BaseServiceImpl<CmsContentRelatedDto, CmsContentRelated,CmsContentRelatedMapper> implements CmsContentRelatedService {


    @Autowired
    ThinkCmsConfig thinkCmsConfig;


    @CacheClear(keys = {"getRelatedByContentId"})
    @Transactional
    @Override
    public void saveRelated(String id, List<CmsContentRelatedDto> cmsContentRelateds) {
        if(Checker.BeNotBlank(id)){
            if(cmsContentRelateds.size()>50){
                throw  new CustomException(ApiResult.result(20019));
            }
            if(Checker.BeNotEmpty(cmsContentRelateds)){
                cmsContentRelateds.forEach(cmsContentRelated->{
                    cmsContentRelated.setContentId(id).setRelatedContentId(cmsContentRelated.getId()).setUserId(getUserId())
                            .setId(generateId());
                });
                deleteByFiled("content_id",id);
                insertBatch(cmsContentRelateds);
            }else{
                deleteByFiled("content_id",id);
            }
        }
    }

    @Cacheable(value= Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0+'.'+#p1",unless="#result == null")
    @Override
    public List<ContentDto> getRelatedByContentId(String contentId, int count) {
        count= count>50?50:count;
        return baseMapper.getRelatedByContentId(contentId,count);
    }
}
