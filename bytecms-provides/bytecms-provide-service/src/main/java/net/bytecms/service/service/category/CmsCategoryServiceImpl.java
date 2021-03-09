package net.bytecms.service.service.category;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.freemark.components.NotifyComponent;
import net.bytecms.freemark.corelibs.job.JobExecute;
import net.bytecms.freemark.enums.ContentState;
import net.bytecms.freemark.enums.JobActionNotify;
import net.bytecms.service.api.category.*;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.api.model.CmsModelService;
import net.bytecms.service.api.resource.SysResourceService;
import net.bytecms.service.dto.category.CmsCategoryAttributeDto;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.service.dto.category.CmsCategoryExtendDto;
import net.bytecms.service.dto.category.CmsCategoryModelDto;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.dto.model.CmsModelDto;
import net.bytecms.service.dto.navigation.Navigation;
import net.bytecms.service.entity.category.CmsCategory;
import net.bytecms.service.mapper.category.CmsCategoryMapper;
import net.bytecms.service.service.content.ContentServiceImpl;
import net.bytecms.service.utils.DynamicFieldUtil;
import net.bytecms.core.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 分类 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
@Transactional
@Service
public class CmsCategoryServiceImpl extends BaseServiceImpl<CmsCategoryDto, CmsCategory, CmsCategoryMapper> implements CmsCategoryService {

    @Autowired
    CmsCategoryAttributeService cmsCategoryAttributeService;

    @Autowired
    CmsModelService cmsModelService;

    @Autowired
    CmsCategoryModelService cmsCategoryModelService;

    @Autowired
    CmsCategoryExtendService cmsCategoryExtendService;
    
    @Autowired
    OrgCategoryService orgCategoryService;

    @Autowired
    ContentService contentService;

    @Autowired
    SysResourceService resourceService;

    @Autowired
    NotifyComponent notifyComponent;

    @Autowired
    JobExecute jobExecute;

    @Override
    public CmsCategoryDto getByPk(Serializable id){
        return baseMapper.getByPk(id);
    }

    @Override
    public Tree<CmsCategoryDto> selectTreeCategory(boolean needAuth) {
        List<Tree<CmsCategoryDto>> trees = new ArrayList<Tree<CmsCategoryDto>>();
        CmsCategoryDto cmsCategoryDto = new CmsCategoryDto();
        cmsCategoryDto.condition().orderByAsc("sort");
        List<CmsCategoryDto> cmsCategorys =new ArrayList<>(16);
        if(needAuth){
            cmsCategorys = baseMapper.listCategoryByOrgId(getOrgId());
        }else{
            cmsCategorys = listDto(cmsCategoryDto);
        }
        for (CmsCategoryDto category : cmsCategorys) {
            Map<String,Object> param =new HashMap<>(16);
            param.put("allowContribute",category.getAllowContribute());
            param.put("singlePage",category.getSinglePage());
            Tree<CmsCategoryDto> tree = new Tree<CmsCategoryDto>();
            tree.setKey(category.getId()).setId(category.getId()).setParentId(category.getParentId()).
            setTitle(category.getName()).setAttributes(param).setValue(category.getId()).setCode(category.getCode());
            trees.add(tree);
        }
        List<Tree<CmsCategoryDto>> topNodes = BuildTree.buildList(trees, "0");
        Tree<CmsCategoryDto> root = new Tree<CmsCategoryDto>();
        root.setKey("0").setId("0").setParentId("0").setHasParent(false).setChildren(topNodes).setTitle("父分类");
        return root;
    }

    @Cacheable(value = Constants.cacheName, key = "#root.targetClass+'.'+#root.methodName+'.'+#p0" , unless="#result == null")
    @Override
    public Tree<CmsCategoryDto> selectHomePageCategory(String categoryId) {
        List<Tree<CmsCategoryDto>> trees = new ArrayList<Tree<CmsCategoryDto>>();
        CmsCategoryDto cmsCategoryDto = new CmsCategoryDto();
        cmsCategoryDto.condition().orderByAsc("sort").select("name","code","content_path","id","parent_id").eq("hidden","1");
        List<CmsCategoryDto> cmsCategorys = listDto(cmsCategoryDto);
        for (CmsCategoryDto category : cmsCategorys) {
            Tree<CmsCategoryDto> tree = new Tree<CmsCategoryDto>();
            tree.setKey(category.getId()).setId(category.getId()).setParentId(category.getParentId()).
            setTitle(category.getName()).setUrl(category.getContentPath()).setCode(category.getCode());
            if(Checker.BeNotBlank(categoryId)){
                tree.setChecked(categoryId.equals(category.getId()));
            }
            trees.add(tree);
        }
        List<Tree<CmsCategoryDto>> topNodes = BuildTree.buildList(trees, "0");
        Tree<CmsCategoryDto> root = new Tree<CmsCategoryDto>();
        root.setChildren(topNodes);
        return root;
    }

    @Override
    public Map<String,Object> selectChildCategorys(String categoryId) {
        Map<String,Object> map=new LinkedHashMap<>(16);
        CmsCategoryDto categoryDto=getByPk(categoryId);
        String parentId=categoryDto.getParentId();
        if(!Constants.PARANT_CATEGORY_ID.equals(parentId)){
            categoryDto=getByPk(parentId);
        }
        map.put("pcategory",categoryDto);
        CmsCategoryDto cmsCategoryDto = new CmsCategoryDto();
        cmsCategoryDto.condition().select("id","name","parent_id","code","content_path").orderByAsc("sort").
        eq("parent_id",categoryDto.getId()).eq("hidden","1");
        List<CmsCategoryDto> categoryDtos=listDto(cmsCategoryDto);
        if(Checker.BeNotEmpty(categoryDtos)){
            categoryDtos.forEach(category->{
                category.setDefaultCheck(category.getId().equals(categoryId));
            });
        }
        map.put("categorys",Checker.BeNotEmpty(categoryDtos)?categoryDtos:Lists.newArrayList());
        return map;
    }


    @Transactional
    @Override
    @CacheClear(keys = {"selectHomePageCategory"})
    public boolean deleteByCategoryId(String pk) {
        // 先验证栏目下是否存在文章
        String[]  status = {ContentState.ROUGH.getCode(),ContentState.PUBLISH.getCode()};
        boolean hasContent=contentService.ckHasContentByCategId(pk, Arrays.asList(status));
        if(hasContent){
            throw  new CustomException(ApiResult.result(20014));
        }
        cmsCategoryModelService.deleteByFiled("category_id",pk);
        cmsCategoryAttributeService.deleteByFiled("category_id",pk);
        boolean res= deleteByPk(pk);
        if(res){
            jobExecute.execute(JobActionNotify.JOB_HOME_PAGE);
        }
        return res;
    }

    @Override
    public List<CmsCategoryDto> selectCategoryByCodes(String[] codes) {
        if(Checker.BeNotNull(codes) && codes.length>0){
              List<CmsCategoryDto> categorys=baseMapper.selectCategoryByCodes(Arrays.asList(codes));//listByField("code",Arrays.asList(codes));
              if(Checker.BeNotEmpty(categorys)){
                  categorys.forEach(category->{
                      boolean hasExtend = Checker.BeNotBlank(category.getCategoryExtendId()) && Checker.BeNotBlank(category.getData());
                      if(hasExtend){
                          Map<String,Object> extendParam= JSONObject.parseObject(category.getData(),Map.class);
                          if(!extendParam.isEmpty()){
                              category.setExtendParam(extendParam);
                          }
                      }
                  });
                  return categorys;
              }
        }
        return Lists.newArrayList();
    }

    @Override
    public List<CmsCategoryDto> selectCategoryForWholeSiteGen() {
        QueryWrapper<CmsCategory> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("hidden",1);
        List<CmsCategory> list =super.baseMapper.selectList(queryWrapper);
        return Checker.BeNotEmpty(list)?T2DList(list):Lists.newArrayList();
    }

    @Override
    public void updateContentModel(String id) {
        CmsCategoryDto cmsCategoryDto = getByPk(id);
        List<CmsCategoryModelDto> cmsCategoryModelDtos=cmsCategoryModelService.listByField("category_id",id);
        if(Checker.BeNotNull(cmsCategoryDto) && Checker.BeNotEmpty(cmsCategoryModelDtos)){
            String modelId =cmsCategoryModelDtos.get(0).getModelId();
            if(Checker.BeNotBlank(modelId)){
                contentService.updateContentModel(cmsCategoryDto.getId(),modelId,modelId);
            }
        }
    }

    @Override
    public List<CmsCategoryDto> listCategoryByPidAndOrgId(String pid, String orgId) {
        List<CmsCategoryDto> cmsCategoryDtos =baseMapper.listCategoryByPidAndOrgId(pid,orgId);
        return Checker.BeNotEmpty(cmsCategoryDtos)? cmsCategoryDtos:Lists.newArrayList();
    }

    @Cacheable(value=Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#root.args[0]+'.'+#root.args[1]",unless="#result == null")
    @Override
    public CmsCategoryDto getCategoryInfoByPk(String categoryId,String categoryCode) {
        if(Checker.BeBlank(categoryId) && Checker.BeBlank(categoryCode)){
            return null;
        }
        CmsCategoryDto categoryDto=baseMapper.getCategoryInfoByPk(categoryId,categoryCode);
        if(Checker.BeNotNull(categoryDto)){
            boolean hasExtend = Checker.BeNotBlank(categoryDto.getCategoryExtendId()) && Checker.BeNotBlank(categoryDto.getData());
            if(hasExtend){
                Map<String,Object> extendParam= JSONObject.parseObject(categoryDto.getData(),Map.class);
                if(!extendParam.isEmpty()){
                    categoryDto.setExtendParam(extendParam);
                }
            }
        }
         return categoryDto;
//        if(Checker.BeNotBlank(categoryId)){
//            CmsCategoryDto cmsCategoryDto=getByPk(categoryId);
//            CmsCategoryAttributeDto attributeDto = cmsCategoryAttributeService.getByField("category_id",categoryId);
//            boolean hasExtend = Checker.BeNotBlank(cmsCategoryDto.getCategoryExtendId()) &&
//                    Checker.BeNotNull(attributeDto) && Checker.BeNotBlank(attributeDto.getData());
//            if(hasExtend){// 有扩展字段
//                CmsCategoryExtendDto extendDto= cmsCategoryExtendService.getByPk(cmsCategoryDto.getCategoryExtendId());
//                if(Checker.BeNotNull(extendDto) && Checker.BeNotBlank(extendDto.getExtendFieldList())){
//                    cmsCategoryDto.setExtendParam(JSONObject.parseObject(attributeDto.getData(),Map.class));
//                }
//            }
//            return cmsCategoryDto;
//        }
//        return null;
    }

    @Override
    public List<Navigation> getNavigation(String contentId) {
        List<Navigation> navigations=new ArrayList<>(16);
        ContentDto contentDto=contentService.getByPk(contentId);
        if(Checker.BeNotNull(contentDto)){
            Set<String> categoryIds=new LinkedHashSet(16);
            recursionCid(categoryIds,contentDto.getCategoryId());
            if(Checker.BeNotEmpty(categoryIds)){
                List<CmsCategoryDto> categoryDtos = getByPks(categoryIds);
                if(Checker.BeNotEmpty(categoryDtos)){
                    categoryDtos.forEach(categoryDto->{
                        navigations.add(new Navigation(categoryDto.getName(),categoryDto.getContentPath()));
                    });
                }
            }
        }
        navigations.add(new Navigation(contentDto.getTitle(),null));
        return navigations;
    }

    private void recursionCid(Set<String> categoryIds,String categoryId){
        CmsCategoryDto categoryDto=getByPk(categoryId);
        categoryIds.add(categoryId);
        if(!"0".equals(categoryDto.getParentId())){
            recursionCid(categoryIds,categoryDto.getParentId());
        }
    }

    @CacheClear(keys = {"selectHomePageCategory","getCategoryInfoByPk"})
    @Transactional
    @Override
    public void save(CmsCategoryDto v) {
        checkerCodeIsExist(v.getCode(),v.getId());
        v.setId(generateId());
        insert(v);
        if (Checker.BeNotNull(v.getExtendParam())) {
            CmsCategoryAttributeDto categoryAttributeDto = new CmsCategoryAttributeDto();
            categoryAttributeDto.setCategoryId(v.getId()).setTitle(v.getTitle()).setKeywords(v.getKeywords())
            .setDescription(v.getDescription());
            if(!v.getExtendParam().isEmpty()){
                categoryAttributeDto.setData(JSON.toJSONString(v.getExtendParam()));
            }
            cmsCategoryAttributeService.insert(categoryAttributeDto);
        }

        CmsModelDto cmsModelDto= cmsModelService.getByPk(v.getModelId());
        if(Checker.BeNotNull(cmsModelDto)){
            CmsCategoryModelDto cmsCategoryModelDto =new CmsCategoryModelDto();
            cmsCategoryModelDto.setCategoryId(v.getId()).setModelId(cmsModelDto.getId()).setTemplatePath(v.getModelTemplate());
            cmsCategoryModelService.insert(cmsCategoryModelDto);
        }
        orgCategoryService.updateCategoryToHalfChecked(v.getId());
        jobExecute.execute(JobActionNotify.JOB_HOME_PAGE);
    }

    private void checkerCodeIsExist(String code,String id){
        if(Checker.BeNotBlank(code)){
            CmsCategoryDto cmsCategoryDto= super.getByField("code",code);
            if(Checker.BeNotBlank(id)){// 编辑
                if(Checker.BeNotNull(cmsCategoryDto) && !id.equals(cmsCategoryDto.getId())){ // 新增
                    throw  new CustomException(ApiResult.result(20007));
                }
            }else{
                if(Checker.BeNotNull(cmsCategoryDto)){ // 新增
                    throw  new CustomException(ApiResult.result(20007));
                }
            }

        }
    }

    @Override
    public CmsCategoryDto getInfoById(String id) {
        CmsCategoryDto categoryDto = getByPk(id);
        if(Checker.BeNotBlank(categoryDto.getParentId())){
            if("0".equals(categoryDto.getParentId())){
                categoryDto.setParentName("父分类");
            }else{
                CmsCategoryDto parentCat = getByPk(categoryDto.getParentId());
                categoryDto.setParentName(parentCat.getName());
            }
        }
        CmsCategoryAttributeDto attributeDto = cmsCategoryAttributeService.getByField("category_id", categoryDto.getId());
        if(Checker.BeNotNull(attributeDto)){
            categoryDto.setTitle(attributeDto.getTitle()).setKeywords(attributeDto.getKeywords()).setDescription(attributeDto.getDescription());
        }
        if(Checker.BeNotBlank(categoryDto.getCategoryExtendId())){// 扩展分类
            CmsCategoryExtendDto extendDto = cmsCategoryExtendService.getByPk(categoryDto.getCategoryExtendId());
            boolean cond =Checker.BeNotNull(extendDto)&&Checker.BeNotBlank(extendDto.getExtendFieldList()) &&
            Checker.BeNotNull(attributeDto);
            if(cond){
                List<CmsDefaultModelFieldDto> allFields= DynamicFieldUtil.formaterField(null,extendDto.getExtendFieldList());
                if(Checker.BeNotBlank(attributeDto.getData())){
                    //ExtendFieldUtil.formaterDefaultModel(allFields,attributeDto.getData());
                    DynamicFieldUtil.setCategoryValByFiled(allFields,attributeDto.getData(), resourceService);
                }
                if(Checker.BeNotEmpty(allFields)){
                    categoryDto.setExtendFieldList(allFields);
                }
            }
        }

        List<CmsCategoryModelDto> categoryModels= cmsCategoryModelService.listByField("category_id",categoryDto.getId());
        if(Checker.BeNotEmpty(categoryModels)){
            categoryDto.setModelId(categoryModels.get(0).getModelId());
        }
        return categoryDto;
    }

    @CacheClear(keys = {"selectHomePageCategory","getByPk", "getCategoryInfoByPk","getCreateForm"}
    ,clzs = {CmsCategoryServiceImpl.class, ContentServiceImpl.class})
    @Transactional
    @Override
    public void update(CmsCategoryDto v) {
        checkerCodeIsExist(v.getCode(),v.getId());
        super.updateByPk(v);
        if (Checker.BeNotNull(v.getExtendParam())) {
            CmsCategoryAttributeDto categoryAttributeDto = new CmsCategoryAttributeDto();
            categoryAttributeDto.setCategoryId(v.getId()).setTitle(v.getTitle()).setKeywords(v.getKeywords())
            .setDescription(v.getDescription());
            if(!v.getExtendParam().isEmpty()&& Checker.BeNotBlank(v.getCategoryExtendId())){
                categoryAttributeDto.setData(JSON.toJSONString(v.getExtendParam()));
            }else{
                categoryAttributeDto.setData("");
            }
            cmsCategoryAttributeService.updateByField("category_id",v.getId(),categoryAttributeDto);
        }
        if(Checker.BeNotBlank(v.getModelId())){ // 更新时从新选择模型
            CmsModelDto cmsModelDto= cmsModelService.getByPk(v.getModelId());
            if(Checker.BeNotNull(cmsModelDto)){// 已经选择过的模型
                CmsCategoryModelDto cmsCategoryModelDto =new CmsCategoryModelDto();
                cmsCategoryModelDto.setCategoryId(v.getId()).setModelId(cmsModelDto.getId()).setTemplatePath(v.getModelTemplate());
                List<CmsCategoryModelDto> cmsCategoryModelDtos =cmsCategoryModelService.listByField("category_id",v.getId());
                if(Checker.BeNotEmpty(cmsCategoryModelDtos)){ // 更新
                    cmsCategoryModelService.updateByField("category_id",v.getId(),cmsCategoryModelDto);
                }else{ // 保存
                    cmsCategoryModelService.insert(cmsCategoryModelDto);
                }
                // 将当前栏目下的所有文章模型为空的内容 应用到当前内容模型
                contentService.updateContentModel(v.getId(),cmsModelDto.getId(),"");
            }
        }
        jobExecute.execute(JobActionNotify.JOB_HOME_PAGE);
    }

    @Override
    public PageDto<CmsCategoryDto> listPage(PageDto<CmsCategoryDto> pageDto) {
        CmsCategoryDto cmsCategoryDto = pageDto.getDto();
        IPage<CmsCategoryDto> pages = new Page<>(pageDto.getPageNo(), pageDto.getPageSize());
        IPage<CmsCategoryDto> result = baseMapper.listPage(pages, cmsCategoryDto.getId());
        PageDto<CmsCategoryDto> resultSearch = new PageDto(result.getTotal(), result.getPages(), result.getCurrent(), Checker.BeNotEmpty(result.getRecords()) ? result.getRecords() : Lists.newArrayList());
        return resultSearch;
    }


    @Override
    public void reStaticFileByCid(String id,List<String> status) {
//        boolean hasContent = contentService.ckHasContentByCategId(id,status);
//        if(!hasContent){
//            throw  new CustomException(ApiResult.result(20013));
//        }
        List<CmsCategoryDto> cmsCategoryDtos =new ArrayList<>(16);
        CmsCategoryDto cmsCategoryDto=getByPk(id);
        cmsCategoryDto.setAutoGenStatic(false);
        cmsCategoryDtos.add(cmsCategoryDto);
        Map<String,Object> param = new HashMap<>();
        param.put("categorys",cmsCategoryDtos);
        param.put(SecurityConstants.USER_ID,getUserId());
        jobExecute.execute(JobActionNotify.JOB_CATEGORY_LIST_PAGE_RIGHT_NOW.setSecond(2),param);
    }


    @Override
    public List<CmsCategoryDto> selectParentCategorys(String id) {
        List<CmsCategoryDto> cmsCategoryDtos=new ArrayList<>(16);
        CmsCategoryDto thisCateg = getByPk(id);
        if(Checker.BeNotNull(thisCateg)){
            if("0".equals(thisCateg.getParentId())){
                cmsCategoryDtos.add(thisCateg);
            }else{
                recursionParent(cmsCategoryDtos,thisCateg.getParentId());
                cmsCategoryDtos.add(thisCateg);
            }
        }
        return cmsCategoryDtos;
    }

    private void recursionParent(List<CmsCategoryDto> cmsCategoryDtos,String categoryId){
        CmsCategoryDto thisCateg = getByPk(categoryId);
        if(Checker.BeNotNull(thisCateg)){
            cmsCategoryDtos.add(thisCateg);
            recursionParent(cmsCategoryDtos,thisCateg.getParentId());
        }
    }

}
