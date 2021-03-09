package net.bytecms.service.service.tags;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.bytecms.service.api.tags.CmsTagsService;
import net.bytecms.service.api.tags.CmsTagsTypeService;
import net.bytecms.service.dto.tags.CmsTagsDto;
import net.bytecms.service.dto.tags.CmsTagsTypeDto;
import net.bytecms.service.entity.tags.CmsTagsType;
import net.bytecms.service.mapper.tags.CmsTagsTypeMapper;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签类型 服务实现类
 * </p>
 *
 * @author LG
 * @since 2020-01-31
 */
@Transactional
@Service
public class CmsTagsTypeServiceImpl extends BaseServiceImpl<CmsTagsTypeDto, CmsTagsType, CmsTagsTypeMapper> implements CmsTagsTypeService {

    @Autowired
    CmsTagsService cmsTagsService;


    @Override
    public PageDto<CmsTagsTypeDto> listPage(PageDto<CmsTagsTypeDto> pageDto) {
        IPage<CmsTagsTypeDto> pages = new Page<>(pageDto.getPageNo(), pageDto.getPageSize());
        IPage<CmsTagsTypeDto> result = baseMapper.listPage(pages, pageDto.getDto());
        PageDto<CmsTagsTypeDto> resultSearch = new PageDto(result.getTotal(), result.getPages(), result.getCurrent(), Checker.BeNotEmpty(result.getRecords()) ? result.getRecords() : Lists.newArrayList());
        return resultSearch;
    }


    @Override
    @CacheEvict(value = Constants.cacheName, key = "#root.targetClass+'.getByPk.'+#root.args[0]")
    public boolean deleteById(String id) {
        List<CmsTagsDto> cmsTagsDtos=cmsTagsService.listByField("type_id",id);
        if(Checker.BeNotEmpty(cmsTagsDtos)){
            throw  new CustomException(ApiResult.result(20023));
        }
        return super.deleteByPk(id);
    }

    @CacheClear(keys = {"getByPk"},clzs = {CmsTagsServiceImpl.class})
    @Override
    public void tagsBelong(CmsTagsTypeDto v) {
        boolean cklegal=Checker.BeNotBlank(v.getId())&&Checker.BeNotEmpty(v.getTagIds());
        if(cklegal){
            List<CmsTagsDto> cmsTagsDtos=cmsTagsService.getByPks(v.getTagIds());
            if(Checker.BeNotEmpty(cmsTagsDtos)){
                cmsTagsDtos.forEach(cmsTagsDto->{
                    cmsTagsDto.setTypeId(v.getId());
                });
                cmsTagsService.updateByPks(cmsTagsDtos);
            }
        }
    }

    @Override
    public void save(CmsTagsTypeDto v) {
        String name=v.getName();
        boolean ckNotlegal = Checker.BeBlank(name)||Checker.BeNotEmpty(isExist(name));
        if(ckNotlegal){
            throw new CustomException(ApiResult.result(20024));
        }
        v.setName(name.toUpperCase().replaceAll("\\s*", ""));
        super.insert(v);
    }

    @Override
    @CacheEvict(value = Constants.cacheName, key = "#root.targetClass+'.getByPk.'+#p0.id")
    public void update(CmsTagsTypeDto v) {
        List<CmsTagsType> cmsTagsTypes=isExist(v.getName());
        if(Checker.BeNotEmpty(cmsTagsTypes)){
            if(!v.getId().equals(cmsTagsTypes.get(0).getId())){
                throw new CustomException(ApiResult.result(20024));
            }
        }
        v.setName(v.getName().toUpperCase().replaceAll("\\s*", ""));
        super.updateByPk(v);
    }

    private List<CmsTagsType> isExist(String name){
        Map<String,Object> map = new HashMap<>(16);
        map.put("name",name.toUpperCase().replaceAll("\\s*", ""));
        List<CmsTagsType> cmsTagsTypes=baseMapper.selectByMap(map);
        return cmsTagsTypes;
    }
}
