package net.bytecms.service.service.fragment;
import net.bytecms.service.api.fragment.FragmentAttributeService;
import net.bytecms.service.dto.fragment.FragmentAttributeDto;
import net.bytecms.service.entity.fragment.FragmentAttribute;
import net.bytecms.service.mapper.fragment.FragmentAttributeMapper;
import net.bytecms.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Transactional
@Service
public class FragmentAttributeServiceImpl extends BaseServiceImpl<FragmentAttributeDto, FragmentAttribute, FragmentAttributeMapper> implements FragmentAttributeService {


    @Override
    public boolean deleteByFragmentId(String pk) {
        Map<String,Object> param = new HashMap<>();
        param.put("fragment_id",pk);
        return removeByMap(param);
    }
}
