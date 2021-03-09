package net.bytecms.service.service.fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.freemark.corelibs.job.JobExecute;
import net.bytecms.freemark.enums.JobActionNotify;
import net.bytecms.service.api.fragment.FragmentAttributeService;
import net.bytecms.service.api.fragment.FragmentFileModelService;
import net.bytecms.service.api.fragment.FragmentService;
import net.bytecms.service.api.resource.SysResourceService;
import net.bytecms.service.dto.fragment.FragmentAttributeDto;
import net.bytecms.service.dto.fragment.FragmentDto;
import net.bytecms.service.dto.fragment.FragmentFileModelDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.entity.fragment.Fragment;
import net.bytecms.service.mapper.fragment.FragmentMapper;
import net.bytecms.service.utils.DynamicFieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面片段数据表 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Transactional
@Service
public class FragmentServiceImpl extends BaseServiceImpl<FragmentDto,Fragment,FragmentMapper> implements FragmentService {

    @Autowired
    FragmentAttributeService fragmentAttributeService;

    @Autowired
    FragmentFileModelService fragmentFileModelService;

    @Autowired
    SysResourceService resourceService;

    @Autowired
    JobExecute jobExecute;

    @Transactional
    @Override
    @CacheClear(key = "getFragmentDataByCode")
    public void saveFragment(FragmentDto v) {
        v.setId(generateId());
        insert(v);
        Map<String,Object> extendParam=v.getExtendParam();
        if(Checker.BeNotEmpty(extendParam)){
            FragmentAttributeDto fragmentAttributeDto =new FragmentAttributeDto();
            fragmentAttributeDto.setFragmentId(v.getId()).setId(generateId()).setData(JSON.toJSONString(extendParam));
            fragmentAttributeService.insert(fragmentAttributeDto);
        }
        jobExecute.execute(JobActionNotify.JOB_HOME_PAGE);
    }

    @CacheClear(key = "getFragmentDataByCode")
    @Override
    public void updateFragmentByPk(FragmentDto v) {
        updateByPk(v);
        Map<String,Object> extendParam=v.getExtendParam();
        if(Checker.BeNotEmpty(extendParam)){
            FragmentAttributeDto fragmentAttributeDto =new FragmentAttributeDto();
            String data = JSON.toJSONString(extendParam);
            fragmentAttributeDto.setFragmentId(v.getId()).setData(data);
            fragmentAttributeService.updateByField("fragment_id",v.getId(),fragmentAttributeDto);
        }
        jobExecute.execute(JobActionNotify.JOB_HOME_PAGE);
    }


    @Transactional
    @Override
    @CacheClear(key = "getFragmentDataByCode")
    public boolean deleteFragmentByPk(String pk) {
        deleteByPk(pk);
        boolean delRes= fragmentAttributeService.deleteByFragmentId(pk);
        if(delRes) jobExecute.execute(JobActionNotify.JOB_HOME_PAGE);
        return delRes;
    }


    @Override
    @Cacheable(value = Constants.cacheName, key = "#root.targetClass+'.'+#root.methodName+'.'+#p0")
    public List<FragmentDto> getFragmentDataByCode(String code) {
        List<FragmentDto> fragmentDtos= baseMapper.getFragmentDataByCode(code);
        if(Checker.BeNotNull(fragmentDtos)){
             for(FragmentDto fragmentDto:fragmentDtos){
                  if(Checker.BeNotBlank(fragmentDto.getData())){
                      fragmentDto.setExtendParam(JSONObject.parseObject(fragmentDto.getData(),Map.class));
                  }
             }
        }
        return Checker.BeNotEmpty(fragmentDtos)?fragmentDtos: Lists.newArrayList();
    }

    @Override
    public ApiResult getInfoByPk(String id) {
        FragmentDto fragmentDto = baseMapper.getByPk(id);
        List<CmsDefaultModelFieldDto> allFields = new ArrayList<>();
        if(Checker.BeNotNull(fragmentDto)){
            FragmentFileModelDto fileModelDto= fragmentFileModelService.getByPk(fragmentDto.getFragmentFileModelId());
            if(Checker.BeNotNull(fileModelDto)){
                 allFields = DynamicFieldUtil.formaterField(fileModelDto.getCheckedFieldList(),fileModelDto.getExtendFieldList());
                if(Checker.BeNotEmpty(allFields)){
                    DynamicFieldUtil.setFragmentValByFiled(allFields,fragmentDto,resourceService);
                }
            }
        }
        ApiResult result= ApiResult.result(allFields);
        result.put("id",fragmentDto.getId());
        return result;
    }
}
