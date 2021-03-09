package net.bytecms.service.service.tags;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.bytecms.service.api.tags.CmsTagsService;
import net.bytecms.service.dto.tags.CmsTagsDto;
import net.bytecms.service.entity.tags.CmsTags;
import net.bytecms.service.mapper.tags.CmsTagsMapper;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-12-09
 */
@Transactional
@Service
public class CmsTagsServiceImpl extends BaseServiceImpl<CmsTagsDto, CmsTags, CmsTagsMapper> implements CmsTagsService {



    @Override
    public PageDto<CmsTagsDto> listPage(PageDto<CmsTagsDto> pageDto) {
        IPage<CmsTagsDto> pages = new Page<>(pageDto.getPageNo(), pageDto.getPageSize());
        IPage<CmsTagsDto> result = baseMapper.listPage(pages, pageDto.getDto());
        PageDto<CmsTagsDto> resultSearch = new PageDto(result.getTotal(), result.getPages(), result.getCurrent(), Checker.BeNotEmpty(result.getRecords()) ? result.getRecords() : Lists.newArrayList());
        return resultSearch;
    }



    @CacheClear(keys = {"getTagsByContentId","listTags"})
    @Override
    public List<String> saveTags(List<String> tags) {
        List<String> tagIds = new ArrayList<>(16);
        List<CmsTagsDto> cmsTagsDtos = new ArrayList<>(16);
        Map<String,String> tagRes=selectTagsByName(tags);
        if(Checker.BeNotEmpty(tags)){
            for(String tag :tags){
                //String dbTagId = ckExistTag(tag);
                String dbTagId = tagRes.get(tag);
                if(Checker.BeNotBlank(dbTagId)){
                    tagIds.add(dbTagId);
                    continue;
                }
                String id =generateId();
                tagIds.add(id);
                CmsTagsDto cmsTags =new CmsTagsDto();
                cmsTags.setName(tag.toUpperCase().replaceAll("\\s*", "")).setId(id);
                cmsTagsDtos.add(cmsTags);
            }
            insertBatch(cmsTagsDtos);
        }
        return tagIds;
    }

    private  Map<String,String> selectTagsByName(List<String> tags){
        Map<String,String> tagRes=new HashMap<>();
        if(Checker.BeNotEmpty(tags)){
            List<String> formaterTags=new ArrayList<>(16);
            tags.forEach(tag->{
                formaterTags.add(tag.toUpperCase().replaceAll("\\s*", ""));
            });
            List<CmsTagsDto> tagsDtos=listByField("name",formaterTags);
            if(Checker.BeNotEmpty(tagsDtos)){
                tagsDtos.forEach(tagsDto->{
                    tagRes.put(tagsDto.getName().toUpperCase().replaceAll("\\s*", ""),tagsDto.getId());
                });
            }
        }
        return tagRes;
    }

    // 判断数据库标签是否存在 存在则不插入
    private String ckExistTag(String tag){
        String tagId = "";
        CmsTagsDto cmsTagsDto =getByField("name",tag.toUpperCase().replaceAll("\\s*", ""));
        if(Checker.BeNotNull(cmsTagsDto)){
            tagId = cmsTagsDto.getId();
        }
        return tagId;
    }

    @Cacheable(value= Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0",unless="#result == null")
    @Override
    public List<String> getTagsByIds(String tagIds) {
        List<String> tags = new ArrayList<>(16);
        if(Checker.BeNotBlank(tagIds)){
           List<CmsTagsDto> tagsDtos =getByPks(Arrays.asList(tagIds.split(",")));
           if(Checker.BeNotEmpty(tagsDtos)){
               tagsDtos.forEach(tagsDto->{
                   tags.add(tagsDto.getName());
               });
           }
        }
        return tags;
    }

    @Cacheable(value= Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0",unless="#result == null")
    @Override
    public List<CmsTagsDto> getTagsByContentId(String contentId) {
        List<CmsTagsDto> cmsTagsDtos= baseMapper.getTagsByContent(contentId);
        return Checker.BeNotEmpty(cmsTagsDtos)?cmsTagsDtos:Lists.newArrayList();
    }

    @Cacheable(value= Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0",unless="#result == null")
    @Override
    public List<CmsTagsDto> listTags(Integer maxRowNum) {
          List<CmsTagsDto> tags= baseMapper.listTags(maxRowNum);
          return Checker.BeNotEmpty(tags) ? tags : Lists.newArrayList();
    }

    @Override
    @CacheEvict(value = Constants.cacheName, key = "#root.targetClass+'.getByPk.'+#p0.id")
    public void update(CmsTagsDto v) {
        String tagId=ckExistTag(v.getName());
        if(Checker.BeNotBlank(tagId)){
            if(!tagId.equals(v.getId())){
                throw new CustomException(ApiResult.result(20025));
            }
        }
        v.setName(v.getName().toUpperCase().replaceAll("\\s*", ""));
        super.updateByPk(v);
    }
}
