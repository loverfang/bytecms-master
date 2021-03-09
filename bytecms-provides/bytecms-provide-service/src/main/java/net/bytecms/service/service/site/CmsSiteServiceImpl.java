package net.bytecms.service.service.site;
import net.bytecms.service.api.site.CmsSiteService;
import net.bytecms.service.dto.site.CmsSiteDto;
import net.bytecms.service.entity.site.CmsSite;
import net.bytecms.service.mapper.site.CmsSiteMapper;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.Checker;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-12-11
 */
@Transactional
@Service
public class CmsSiteServiceImpl extends BaseServiceImpl<CmsSiteDto, CmsSite, CmsSiteMapper> implements CmsSiteService {



    @Cacheable(value= Constants.cacheName, key="#root.targetClass+'.'+#root.methodName",unless="#result == null")
    @Override
    public CmsSiteDto getSite() {
        List<CmsSite> cmsSites=list();
        if(Checker.BeNotEmpty(cmsSites)){
            return T2D(cmsSites.get(0));
        }
        return null;
    }


    @CacheClear(keys = {"getSite","getByPk"})
    @Override
    public void updateById(CmsSiteDto v) {
        super.updateByPk(v);
    }

    @CacheClear(keys = {"getSite","getByPk"})
    @Override
    public void save(CmsSiteDto v) {
        super.insert(v);
    }

    @CacheClear(keys = {"getSite","getByPk"})
    @Override
    public boolean deleteById(String pk) {
        return super.deleteByPk(pk);
    }
}
