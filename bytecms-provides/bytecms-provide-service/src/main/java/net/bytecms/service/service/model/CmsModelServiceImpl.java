package net.bytecms.service.service.model;

import com.alibaba.fastjson.JSON;
import net.bytecms.service.api.category.CmsCategoryModelService;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.api.model.CmsModelService;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.dto.model.CmsModelDto;
import net.bytecms.service.entity.content.Content;
import net.bytecms.service.entity.content.ContentAttribute;
import net.bytecms.service.entity.model.CmsModel;
import net.bytecms.service.mapper.model.CmsModelMapper;
import net.bytecms.service.service.content.ContentServiceImpl;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.annotation.FieldDefault;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.Checker;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-10-23
 */
@Service
public class CmsModelServiceImpl extends BaseServiceImpl<CmsModelDto, CmsModel, CmsModelMapper> implements CmsModelService {


    @Autowired
    ContentService contentService;

    @Autowired
    CmsCategoryModelService cmsCategoryModelService;

    @Override
    public List<CmsDefaultModelFieldDto> listDefaultField() {
        List<CmsDefaultModelFieldDto> cmsDefaultModelFieldDtos=new ArrayList<>();
        Field[] fieldsContent = Content.class.getDeclaredFields();
        Field[] fieldsAttr= ContentAttribute.class.getDeclaredFields();
        Field[] allFields=ArrayUtils.addAll(fieldsContent, fieldsAttr);
        for (Field f : allFields) {
            boolean hasAnnotation = f.isAnnotationPresent(FieldDefault.class);
            if (hasAnnotation) {
                FieldDefault annotation = f.getAnnotation(FieldDefault.class);
                CmsDefaultModelFieldDto mfDto=new CmsDefaultModelFieldDto();
                mfDto.setVisibleCheck(annotation.visibleCheck())
                .setVisibleSwitch(annotation.visibleSwitch())
                .setInitCheck(annotation.initCheck())
                .setInitSwitch(annotation.initSwitch())
                .setFieldNote(annotation.fieldNote())
                .setReFieldNote(annotation.fieldNote())
                .setFieldType(f.getType().getTypeName().replace("java.lang.",""))
                .setInputType(annotation.inputType().getValue())
                .setFieldName(f.getName());
                cmsDefaultModelFieldDtos.add(mfDto);
            }
        }
        return cmsDefaultModelFieldDtos;
    }

    @Override
    public List<CmsModelDto> listModelByCategory(CmsModelDto v) {
        return baseMapper.listModelByCategory(v);
    }

    @CacheClear(keys = {"getCreateForm"}, clzs = ContentServiceImpl.class)
    @Override
    public boolean insert(CmsModelDto v){
        String ckDefaultField= v.getDefaultFieldList();
        List<CmsDefaultModelFieldDto> cmsCheckedModelFields =new ArrayList<>();
        List<CmsDefaultModelFieldDto> cmsDefaultModelFields=JSON.parseArray(ckDefaultField,CmsDefaultModelFieldDto.class);
        List<String> requireFied= new ArrayList<>();
        if(Checker.BeNotEmpty(cmsDefaultModelFields)){
            Map<String,String>  fieldTextMap =new HashMap<>(16);
            cmsDefaultModelFields.forEach(defaultModel->{
                if(defaultModel.getInitCheck()){
                    if(defaultModel.getInitSwitch()){
                        requireFied.add(defaultModel.getFieldName());
                    }
                    fieldTextMap.put(defaultModel.getFieldName(),defaultModel.getReFieldNote());
                    cmsCheckedModelFields.add(defaultModel);
                }
            });
            if(Checker.BeNotEmpty(requireFied)){
                v.setRequiredFieldList(JSON.toJSONString(requireFied));
            }
            if(!fieldTextMap.isEmpty()){
                v.setFieldTextMap(JSON.toJSONString(fieldTextMap));
            }
            if(Checker.BeNotEmpty(cmsCheckedModelFields)){
                v.setCheckedFieldList(JSON.toJSONString(cmsCheckedModelFields));
            }
        }
        return super.insert(v);
    }

    @CacheClear(keys = {"getCreateForm"}, clzs = ContentServiceImpl.class)
    @Override
    public boolean updateByPk(CmsModelDto v){
        String ckDefaultField= v.getDefaultFieldList();
        List<CmsDefaultModelFieldDto> cmsCheckedModelFields =new ArrayList<>();
        List<CmsDefaultModelFieldDto> cmsDefaultModelFields=JSON.parseArray(ckDefaultField,CmsDefaultModelFieldDto.class);
        List<String> requireFied= new ArrayList<>();
        if(Checker.BeNotEmpty(cmsDefaultModelFields)){
            Map<String,String>  fieldTextMap =new HashMap<>(16);
            cmsDefaultModelFields.forEach(defaultModel->{
                if(defaultModel.getInitCheck()){
                    if(defaultModel.getInitSwitch()){
                        requireFied.add(defaultModel.getFieldName());
                    }
                    fieldTextMap.put(defaultModel.getFieldName(),defaultModel.getReFieldNote());
                    cmsCheckedModelFields.add(defaultModel);
                }
            });
            if(Checker.BeNotEmpty(requireFied)){
                v.setRequiredFieldList(JSON.toJSONString(requireFied));
            }
            if(!fieldTextMap.isEmpty()){
                v.setFieldTextMap(JSON.toJSONString(fieldTextMap));
            }
            if(Checker.BeNotEmpty(cmsCheckedModelFields)){
                v.setCheckedFieldList(JSON.toJSONString(cmsCheckedModelFields));
            }
        }
        return super.updateByPk(v);
    }


    @Transactional
    @Override
    public boolean deleteByPk(String id){
        //  删除模型时清空对应模型为当前模型的 内容的 模型id
        super.deleteByPk(id);
        ContentDto contentDto =new ContentDto();
        contentDto.setModelId("");
        contentService.updateByField("model_id",id,contentDto);
        //  删除所有引用到当前模型的分类设置 空
        return cmsCategoryModelService.deleteByFiled("model_id",id);
    }
}
