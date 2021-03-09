package net.bytecms.service.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.bytecms.service.api.resource.SysResourceService;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.service.dto.fragment.FragmentDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.dto.model.CmsExtendModelFieldDto;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.InputTypeEnum;
import net.bytecms.core.utils.Checker;

import java.lang.reflect.Field;
import java.util.*;

public class DynamicFieldUtil {

    //  扩展转默认(将选中字段和扩展字段 统一转化模型)
    public static List<CmsDefaultModelFieldDto> formaterField(String checkedFieldList,String extendFieldList){
        List<CmsDefaultModelFieldDto> cmsDefaultModelFieldDtos =new ArrayList<>();
        if(Checker.BeNotBlank(checkedFieldList)){
            cmsDefaultModelFieldDtos= JSON.parseArray(checkedFieldList,CmsDefaultModelFieldDto.class);
        }
        if(Checker.BeNotBlank(extendFieldList)){
            List<CmsExtendModelFieldDto> extendModelFieldDtos =JSON.parseArray(extendFieldList,CmsExtendModelFieldDto.class);
            if(Checker.BeNotEmpty(extendModelFieldDtos)){
                for (CmsExtendModelFieldDto extendModelFieldDto : extendModelFieldDtos) {
                    CmsDefaultModelFieldDto cmsDefaultModelFieldDto = new CmsDefaultModelFieldDto();
                    cmsDefaultModelFieldDto.setFieldName(extendModelFieldDto.getCode()).setInputType(extendModelFieldDto.getInputType()).
                    setFieldNote(extendModelFieldDto.getName()).setInitSwitch(extendModelFieldDto.getIsRequired()).setMaxlength(extendModelFieldDto.getMaxlength())
                    .setReFieldNote(extendModelFieldDto.getName()).setExtendPrefix(Constants.EXTEND_FIELD_PREFIX);
                    cmsDefaultModelFieldDtos.add(cmsDefaultModelFieldDto);
                }
            }
        }
        return cmsDefaultModelFieldDtos;
    }

    //  模板片段数据根据字段赋值
    public static void setFragmentValByFiled(List<CmsDefaultModelFieldDto> allFiled, FragmentDto fragmentDto, SysResourceService resourceService){
        if(Checker.BeNotEmpty(allFiled) && Checker.BeNotNull(fragmentDto)){
            String extendData=fragmentDto.getData();
            JSONObject extendFiledVal = null;
            if (Checker.BeNotBlank(extendData)) {
                extendFiledVal = JSON.parseObject(extendData);
            }
            Map<String,Object> contentMap=getObjMap(fragmentDto);
            if(Checker.BeNotEmpty(extendFiledVal)){
                contentMap.putAll(extendFiledVal);
            }
            for(CmsDefaultModelFieldDto field:allFiled){
                if(contentMap.containsKey(field.getFieldName())){
                    Object val=contentMap.get(field.getFieldName());
                    if(Checker.BeNotNull(val)) {
                        if (val instanceof String || val instanceof Boolean ||  val instanceof  Number) {
                            field.setDefaultValue(val.toString());
                        } else {
                            boolean isFile = InputTypeEnum.FILE.getValue().equals(field.getInputType()) ||
                                    InputTypeEnum.PICTURE.getValue().equals(field.getInputType());
                            if (isFile) {
                                field.setDefaultObjValue(Arrays.asList(val));
                            }
                        }
                        if ("cover".equals(field.getFieldName())) {
                            SysResourceDto resourcr = resourceService.getByPk(val.toString());
                            if (Checker.BeNotNull(resourcr)) {
                                field.setDefaultObjValue(Arrays.asList(resourcr));
                            }
                        }
                    }
                }
            }
        }
    }

    //  根据字段赋值
    public static void setContentValByFiled(List<CmsDefaultModelFieldDto> allFiled, ContentDto contentDto, SysResourceService resourceService){
        if(Checker.BeNotEmpty(allFiled) && Checker.BeNotNull(contentDto)){
            String extendData=contentDto.getData();
            JSONObject extendFiledVal = null;
            if (Checker.BeNotBlank(extendData)) {
                extendFiledVal = JSON.parseObject(extendData);
            }

            Map<String,Object> contentMap=getObjMap(contentDto);
            if(Checker.BeNotEmpty(extendFiledVal)){
                contentMap.putAll(extendFiledVal);
            }

            for(CmsDefaultModelFieldDto field:allFiled){
                // 模型中需要的字段列表
                if(contentMap.containsKey(field.getFieldName())){
                    // 字段对应的值
                    Object val=contentMap.get(field.getFieldName());
                    // 字段对应的值不为空时
                    if(Checker.BeNotNull(val)){
                        // 如果得到的字段是字符串,布尔，或者数字类型
                        if(val instanceof String || val instanceof  Boolean || val instanceof  Number){
                            // 直接用查询到的值赋予字段默认的值，也就是赋值
                            field.setDefaultValue(val.toString());
                        }else{
                            // 如果是文件，或者图片类型的则按图片/文件规则进行赋值
                            boolean isFile = InputTypeEnum.FILE.getValue().equals(field.getInputType()) ||
                                    InputTypeEnum.PICTURE.getValue().equals(field.getInputType());
                            if(isFile){
                                field.setDefaultObjValue(Arrays.asList(val));
                            }
                        }
                        // 如果是cover字段的话
                        if("cover".equals(field.getFieldName())){
                            SysResourceDto resourcr=resourceService.getByPk(val.toString());
                            if(Checker.BeNotNull(resourcr)) {
                                field.setDefaultObjValue(Arrays.asList(resourcr));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void setCategoryValByFiled(List<CmsDefaultModelFieldDto> allFiled,String attrData,SysResourceService resourceService){
        if(Checker.BeNotEmpty(allFiled) && Checker.BeNotBlank(attrData)){
            JSONObject extendFiledVal =  JSON.parseObject(attrData);
            if(extendFiledVal!=null && !extendFiledVal.isEmpty()){
                for(CmsDefaultModelFieldDto field:allFiled){
                    if(extendFiledVal.containsKey(field.getFieldName())){
                        Object val=extendFiledVal.get(field.getFieldName());
                        if(Checker.BeNotNull(val)){
                            if(val instanceof String || val instanceof  Boolean || val instanceof Number){
                                field.setDefaultValue(val.toString());
                            }else{
                                boolean isFile = InputTypeEnum.FILE.getValue().equals(field.getInputType()) ||
                                        InputTypeEnum.PICTURE.getValue().equals(field.getInputType());
                                if(isFile){
                                    field.setDefaultObjValue(Arrays.asList(val));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static  Map<String,Object> getObjMap(Object obj){
        Map<String,Object> map=new HashMap<>(16);
        Class objCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = objCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            // 设置些属性是可以访问的
            f.setAccessible(true);
            Object val = new Object();
            try {
                // 得到此属性的值
                val = f.get(obj);
                // 设置键值
                map.put(f.getName(), val);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
