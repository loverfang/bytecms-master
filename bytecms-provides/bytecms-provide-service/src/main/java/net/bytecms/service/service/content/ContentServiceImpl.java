package net.bytecms.service.service.content;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.api.BaseSolrService;
import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.constants.SolrCoreEnum;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.model.SolrSearchModel;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.freemark.components.NotifyComponent;
import net.bytecms.freemark.enums.ContentState;
import net.bytecms.freemark.enums.JobActionNotify;
import net.bytecms.freemark.observers.ContentObserver;
import net.bytecms.service.api.category.CmsCategoryModelService;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.api.content.CmsContentRelatedService;
import net.bytecms.service.api.content.ContentAttributeService;
import net.bytecms.service.api.content.ContentFileService;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.api.model.CmsModelService;
import net.bytecms.service.api.resource.SysResourceService;
import net.bytecms.service.api.tags.CmsTagsService;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.service.dto.category.CmsCategoryModelDto;
import net.bytecms.service.dto.content.ContentAttributeDto;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.dto.model.CmsModelDto;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.service.dto.tags.CmsTagsDto;
import net.bytecms.service.entity.content.Content;
import net.bytecms.service.mapper.content.ContentMapper;
import net.bytecms.service.utils.DynamicFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 内容 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Slf4j
@Transactional
@Service
public class ContentServiceImpl extends BaseServiceImpl<ContentDto, Content, ContentMapper> implements ContentService {

    @Autowired
    private CmsCategoryService cmsCategoryService;

    @Autowired
    private CmsCategoryModelService cmsCategoryModelService;

    @Autowired
    private CmsModelService cmsModelService;

    @Autowired
    private ContentAttributeService contentAttributeService;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private NotifyComponent notifyComponent;

    @Autowired
    private BaseRedisService baseRedisService;

    @Autowired
    private BaseSolrService baseSolrService;

    @Autowired
    private ContentFileService contentFileService;

    @Autowired
    private CmsTagsService cmsTagsService;

    @Autowired
    private ContentObserver contentObserver;

    @Autowired
    private CmsContentRelatedService relatedService;

    @Autowired
    private ByteCmsConfig byteCmsConfig;


    @Override
    public PageDto<ContentDto> listPage(PageDto<ContentDto> pageDto) {
        if (Checker.BeNotBlank(pageDto.getDto().getCategoryId())) {
            List<String> childcategoryIds = new ArrayList<>();
            childcategoryIds.add(pageDto.getDto().getCategoryId());
            List<String> newChildCategoryIds = new ArrayList<>();
            recursionCategoryIds(childcategoryIds, newChildCategoryIds);
            pageDto.getDto().setCategoryIds(newChildCategoryIds);
        }
        IPage<ContentDto> pages = new Page<>(pageDto.getPageNo(), pageDto.getPageSize());
        IPage<ContentDto> result = baseMapper.listPage(pages, pageDto.getDto());
        PageDto<ContentDto> resultSearch = new PageDto(result.getTotal(), result.getPages(), result.getCurrent(), Checker.BeNotEmpty(result.getRecords()) ? result.getRecords() : Lists.newArrayList());
        return resultSearch;
    }


    @Override
    public PageDto<ContentDto> pageRecycler(PageDto<ContentDto> pageDto) {
          pageDto.getDto().setStatuses(Arrays.asList(ContentState.DELETE.getCode()));
          return listPage(pageDto);
    }


    @CacheClear(keys = {"getNextOrPreviousContentByIdAndCateg"})
    @Override
    public void finalDoIt(List<String> ids,String status) {
        if(ContentState.DELETE.getCode().equals(status)){
            realDelContentByPks(ids);
        }else{
            List<ContentDto> contentDtos = getByPks(ids);
            if(Checker.BeNotEmpty(contentDtos)){
                contentDtos.forEach(contentDto->{
                    contentDto.setStatus(ContentState.ROUGH.getCode());
                });
                updateByPks(contentDtos);
            }
        }
    }

    @Override
    public IPage<ContentDto> pageAllContentForGen(IPage<ContentDto> pages, List<String> status) {
        return baseMapper.pageAllContentForGen(pages,status);
    }

    @Override
    public Set<String> getTopTag() {
         List<String> topTags=baseMapper.getTopTag();
         if(Checker.BeEmpty(topTags)){
             topTags=Lists.newArrayList();
         }
         Set<String> tags=new LinkedHashSet<>(16);
         for(String tag:topTags){
            String[] tagArr= tag.split(",");
            for(String topTag:tagArr){
                tags.add(topTag);
            }
         }
         return tags;
    }

    @Override
    public ContentDto getByPk(Serializable pk) {
          ContentDto contentDto=baseMapper.getByPk(pk);
          return contentDto;
    }


    @Override
    public List<ContentDto> pageContentForCategoryGen(PageDto<ContentDto> pageDto) {
//        IPage<ContentDto> pages = new Page<>(pageDto.getPageNo(), pageDto.getPageSize());
//        IPage<ContentDto> result = baseMapper.pageContentForCategoryGen(pages, pageDto.getDto().getCategoryId());
//        return result.getRecords();
        List<ContentDto> contentDtos = new ArrayList<>(16);
        String categoryId = pageDto.getDto().getCategoryId();
        int pageNo = (int) pageDto.getPageNo();
        int pageSize = (int) pageDto.getPageSize();
        int pageCount = (int) pageDto.getPageCount();
        String key = categoryId + Constants.UNDERLINE;
        boolean hasKey = baseRedisService.hasKey(key);
        if (hasKey) {
            List<ContentDto> cacheContentDtos = (List<ContentDto>) baseRedisService.get(key);
            if (Checker.BeNotEmpty(cacheContentDtos)) {
                log.info("================读取缓存{0}===============|||" + pageNo + "|||||||||||||||||");
            }
            return returnDataByPage(cacheContentDtos, pageNo, pageSize, pageCount, categoryId);
        } else {
            contentDtos = optimizePage((pageNo / 10) + 1, pageSize, categoryId);
            log.info("设置缓存" + pageNo + "设置缓存");
        }
        return contentDtos;
    }


    private List<ContentDto> optimizePage(int pageNo, int pageSize, String categoryId) {
        List<ContentDto> contentDtos = new ArrayList<>(16);
        if (Checker.BeNotBlank(categoryId)) {
            IPage<ContentDto> pages = new Page<>(pageNo, pageSize * 10);
            IPage<ContentDto> result = baseMapper.pageContentForCategoryGen(pages, categoryId, Arrays.asList(new String[]{"1"}));
            if (result.getTotal() > 0) {
                // 10 页的数据
                List<ContentDto> records = result.getRecords();
                extendToMap(records);
                if (Checker.BeNotEmpty(records)) {
                    baseRedisService.set(categoryId + Constants.UNDERLINE, records, 480l);
                }
                int last = records.size() >= pageSize ? pageSize :  records.size();
                contentDtos = records.subList(0, last);
                if (result.getTotal() <= pageSize) {
                    baseRedisService.remove(categoryId + Constants.UNDERLINE);
                }
            }
        }
        return contentDtos;
    }


    private List<ContentDto> returnDataByPage(List<ContentDto> contentDtos, int pageNo, int pageSize, int pageCount, String categoryId) {
        if (Checker.BeEmpty(contentDtos)) {
            return Lists.newArrayList();
        }
        int cacheTotalRow = contentDtos.size();
        int cacheTotaPage = (cacheTotalRow + pageSize - 1) / pageSize;// 总页数
        int statr = ((pageNo - 1) % 10) * pageSize;
        int end = statr + pageSize;
        if ((pageNo % cacheTotaPage == 0)) { // 第一页/循环到每10页 的情况下
            baseRedisService.remove(categoryId + Constants.UNDERLINE);
        }
        if (cacheTotaPage == 1 || (pageNo == pageCount)) { // 第一页/最后一页 的情况下
            baseRedisService.remove(categoryId + Constants.UNDERLINE);
            int remainder = cacheTotalRow % pageSize;
            if (remainder != 0) {
                return contentDtos.subList(statr, statr + remainder);
            }
        }
        return contentDtos.subList(statr, end);
    }

    private void extendToMap(List<ContentDto> records) {
        if (Checker.BeNotEmpty(records)) {
            records.forEach(record -> {
                String extendFieldList = record.getExtendFieldList();
                if (Checker.BeNotBlank(extendFieldList) && Checker.BeNotBlank(record.getData())) {
                    record.setExtendParam(JSONObject.parseObject(record.getData(),Map.class));
                }
            });
        }
    }


    @Override
    public Integer calculatePageCountByCategoryId(String categoryId, Integer pageSize) {
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId).eq("status", ContentState.PUBLISH.getCode());
        int count = super.count(queryWrapper);
        int pageCount = (count + pageSize - 1) / pageSize;
        pageCount = pageCount==0 ? 1:pageCount;
        return pageCount;
    }

    @Override
    public Map<String, Object> selectContentByCategory(String[] codes, Integer maxRowNum) {
        Map<String,Object> res = new HashMap<>(16);
        if (Checker.BeNotEmpty(codes)) {
            List<ContentDto> contentDtos = baseMapper.selectContentByCategorys(codes, maxRowNum);
            if(Checker.BeNotEmpty(contentDtos)){
                contentDtos = setExtendDefVal(contentDtos);
                for(String code:codes){
                    List<ContentDto> contentList = new ArrayList<>();
                    for (ContentDto contentDto : contentDtos) {
                        if (code.equals(contentDto.getCategoryCode())) {
                            contentList.add(contentDto);
                        }
                    }
                    res.put(code, contentList);
                }
            }
        }
        return res;
    }

    @Override
    public List<ContentDto> selectContentBySingleCategory(String code, Integer maxRowNum) {
        if(Checker.BeBlank(code)) return Lists.newArrayList();
        List<ContentDto> contentDtos = baseMapper.selectContentBySingleCategory(code, maxRowNum);
        setExtendDefVal(contentDtos);
        return Checker.BeNotEmpty(contentDtos)?contentDtos:Lists.newArrayList();
    }

    /**
     * 对结果集进行处理
     *
     * @param contentDtos
     * @return
     */
    private List<ContentDto> setExtendDefVal(List<ContentDto> contentDtos) {
        if (Checker.BeEmpty(contentDtos)) {
            contentDtos = new ArrayList<>();
            return contentDtos;
        }
        contentDtos.forEach(contentDto -> {
            if(Checker.BeNotBlank(contentDto.getData())) {
                contentDto.setExtendParam(JSONObject.parseObject(contentDto.getData(),Map.class));
            }
        });
        return contentDtos;
    }

    private void recursionCategoryIds(List<String> childcategoryIds, List<String> newChildcategoryIds) {
        if (Checker.BeNotEmpty(childcategoryIds)) {
            newChildcategoryIds.addAll(childcategoryIds);
            for (String categoryId : childcategoryIds) {
                if (Checker.BeNotBlank(categoryId)) {
                    List<String> childCategs = getCategoryChildByParentId(categoryId);
                    recursionCategoryIds(childCategs, newChildcategoryIds);
                }
            }
        }
    }

    private List<String> getCategoryChildByParentId(String pid) {
        List<String> categoryIds = new ArrayList<>();
        // List<CmsCategoryDto> cmsCategoryDtos = cmsCategoryService.listByField("parent_id", pid);
        List<CmsCategoryDto> cmsCategoryDtos = cmsCategoryService.listCategoryByPidAndOrgId(pid, getOrgId());
        if (Checker.BeNotEmpty(cmsCategoryDtos)) {
            for (CmsCategoryDto cmsCategoryDto : cmsCategoryDtos) {
                categoryIds.add(cmsCategoryDto.getId());
            }
        }
        return categoryIds;
    }

    @Cacheable(value = Constants.cacheName, key = "#root.targetClass+'.'+#root.methodName+'.'+#p0", unless = "#result == null")
    @Override
    public ApiResult getCreateForm(String categoryId) {
        CmsCategoryModelDto cmsCategoryModelDto = cmsCategoryModelService.getByField("category_id", categoryId);
        if (Checker.BeNull(cmsCategoryModelDto) || Checker.BeBlank(cmsCategoryModelDto.getModelId())) {
            throw new CustomException(ApiResult.result(20003));
        }

        CmsModelDto modelDto = cmsModelService.getByPk(cmsCategoryModelDto.getModelId());
        if (Checker.BeNull(modelDto)) {
            throw new CustomException(ApiResult.result(20003));
        }

        List<CmsDefaultModelFieldDto> allFields = DynamicFieldUtil.formaterField(modelDto.getCheckedFieldList(), modelDto.getExtendFieldList());
        ApiResult apiResult = ApiResult.result(allFields);
        apiResult.put("hasFiles", modelDto.getHasFiles());
        return apiResult;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
    @CacheClear(keys = {"getNextOrPreviousContentByIdAndCateg"})
    public void saveContent(ContentDto v) {
        CmsModelDto modelDto = checkerContent(v);
        v.setTemplatePath(modelDto.getTemplatePath()).setHasFiles(Checker.BeNotEmpty(v.getAttachFiles())).setId(generateId());
        if (Checker.BeNotEmpty(v.getTags())) {
            List<String> tagIds = cmsTagsService.saveTags(v.getTags());
            if (Checker.BeNotEmpty(tagIds)) {
                v.setTagIds(StringUtils.join(tagIds.toArray(), ","));
            }
        }
        if (modelDto.getHasFiles() && Checker.BeNotEmpty(v.getAttachFiles())) {
            contentFileService.saveContentFiles(v.getAttachFiles(), v.getId());
        }
        // 保存的时候将扩展属性相关信息保存到content_attribute 表的data字段中
        Map<String, Object> extendParam = v.getExtendParam();
        ContentAttributeDto attributeDto = new ContentAttributeDto();
        attributeDto.setContentId(v.getId()).setText(v.getText()).setData(JSON.toJSONString(extendParam)).setId(generateId());
        contentAttributeService.insert(attributeDto);
        // 是否有推荐内容
        if (Checker.BeNotEmpty(v.getCmsContentRelateds())) {
            v.setHasRelated(true);
            relatedService.saveRelated(v.getId(), v.getCmsContentRelateds());
        }
        super.insert(v);
        notifyComponent.notifyCreate(contentObserver,v,null,false,"");
    }

    @Transactional()
    @Override
    @CacheClear(keys = {"getNextOrPreviousContentByIdAndCateg","getTopContent"})
    public void publish(ContentDto v,int pubWay) {
        if (Checker.BeNotEmpty(v.getIds())) {
            List<ContentDto> contentDtos = getByPks(v.getIds());
            Set<String> cmsCategoryIds = new HashSet<>();
            contentDtos.forEach(contentDto -> {
                cmsCategoryIds.add(contentDto.getCategoryId());
                contentDto.setStatus(v.getStatus()).setCheckUserId(Checker.BeNotBlank(getUserId())?getUserId():v.getCheckUserId()).
                setCheckUserName(Checker.BeNotBlank(getUserName())?getUserName():v.getCheckUserName()).setPublishDate(new Date());
                if(pubWay==1){ // 定时发布
                    contentDto.setExpiryDate(null);
                }
            });
            updateByPks(contentDtos);
            updateCategory(cmsCategoryIds);
            Map<String, Object> param = new HashMap<>(16);
            param.put("status", v.getStatus());
            List<String> previousIds= getPreviousContent(contentDtos);
            if(Checker.BeNotEmpty(previousIds)){
               reStaticBatchGenCid(previousIds,"");
            }
            // TODO 获取这些文章的上一篇文章重新批量生成
            baseSolrService.updateFieldByIds(SolrCoreEnum.DEFAULT_CORE, v.getIds(), param);
        }
    }

    private List<String> getPreviousContent(List<ContentDto> contentDtos ){
        List<String> ids =new ArrayList<>(16);
        if(Checker.BeNotEmpty(contentDtos)){
            contentDtos.forEach(contentDto->{
                ContentDto previousContent = getNextOrPreviousContentByIdAndCateg(contentDto.getId(),contentDto.getCategoryId(),false);
                if(Checker.BeNotNull(previousContent)){
                    ids.add(previousContent.getId());
                }
            });
        }
        return ids;
    }

    @Transactional
    @Override
    @CacheClear(keys = {"getNextOrPreviousContentByIdAndCateg","getTopContent"})
    public void updateContent(ContentDto v) {
        CmsModelDto modelDto = checkerContent(v);
        if (Checker.BeNotEmpty(v.getTags())) {
            List<String> tagIds = cmsTagsService.saveTags(v.getTags());
            if (Checker.BeNotEmpty(tagIds)) {
                v.setTagIds(StringUtils.join(tagIds.toArray(), ","));
            }
        }else{
            v.setTagIds("");
        }
        contentFileService.saveContentFiles(v.getAttachFiles(), v.getId());
        Map<String, Object> extendParam = v.getExtendParam();
        if (Checker.BeNotEmpty(extendParam) || Checker.BeNotBlank(v.getText())) {
            ContentAttributeDto attributeDto = new ContentAttributeDto();
            attributeDto.setContentId(v.getId()).setText(v.getText()).setData(JSON.toJSONString(extendParam));
            contentAttributeService.updateByField("content_id", v.getId(), attributeDto);
        }
        boolean hasRelateds =  Checker.BeNotEmpty(v.getCmsContentRelateds());
        relatedService.saveRelated(v.getId(), v.getCmsContentRelateds());
        v.setTemplatePath(modelDto.getTemplatePath()).setHasFiles(Checker.BeNotEmpty(v.getAttachFiles())).setHasRelated(hasRelateds);
        super.updateByPk(v);
        CmsCategoryDto categoryDto = cmsCategoryService.getByPk(v.getCategoryId());
        notifyComponent.notifyCreate(contentObserver,v,categoryDto,false,"");
        if (ContentState.PUBLISH.getCode().equals(v.getStatus())) {
            // 如果已发布时更新了内容需要从新生成列表页
            notifyComponent.asyncNotifyCategory(0,10,Arrays.asList(categoryDto));
        }
    }

    @Override
    public ApiResult getInfoByPk(String id) {
        Map<String, Object> resMap = new HashMap<>(16);
        List<CmsDefaultModelFieldDto> allFields = new ArrayList<>();
        List<SysResourceDto> contentFileDtos = new ArrayList<>();

        ContentDto contentDto = getByPk(id);

        if (Checker.BeNotNull(contentDto)) {
            allFields = DynamicFieldUtil.formaterField(contentDto.getCheckedFieldList(), contentDto.getExtendFieldList());
            if (contentDto.getHasFiles()) {
                contentFileDtos = contentFileService.getAttachByContnet(contentDto.getId());
            }
            resMap.put("hasFiles", contentDto.getHasFiles());
            if (Checker.BeNotBlank(contentDto.getTagString())) {
                resMap.put("tags",Arrays.asList(contentDto.getTagString().split(",")));
            }
            // 为每个字段赋值
            DynamicFieldUtil.setContentValByFiled(allFields, contentDto, sysResourceService);
        }

        resMap.put("attachFiles", contentFileDtos);
        resMap.put("allFields", allFields);
        resMap.put("id", contentDto.getId());
        resMap.put("categoryId", contentDto.getCategoryId());
        resMap.put("status", contentDto.getStatus());
        resMap.put("hasStatic", contentDto.getHasStatic() ? 1:0);
        resMap.put("rulesData", contentDto.getRulesData());
        return ApiResult.result(resMap);
    }

    @Transactional
    @Override
    @CacheClear(keys = {"getNextOrPreviousContentByIdAndCateg"})
    public boolean deleteContentByPk(String pk) {
        ContentDto contentDto = getByPk(pk);
        if (Checker.BeNotNull(contentDto)) {
            deleteContentByPks(Arrays.asList(pk));
        }
        return true;
    }

    @Transactional
    @CacheClear(keys = {"getNextOrPreviousContentByIdAndCateg"})
    @Override
    public void deleteContentByPks(List<String> ids) {
        if (Checker.BeNotEmpty(ids)) {
            List<ContentDto> contentDtos = getByPks(ids);
            if(Checker.BeNotEmpty(contentDtos)){
                Set<String> categoryIds=new HashSet<>(16);
                for(ContentDto contentDto:contentDtos){
                    if(Checker.BeNotBlank(contentDto.getCategoryId())){
                        categoryIds.add(contentDto.getCategoryId());
                    }
                    contentDto.setStatus(ContentState.DELETE.getCode());
                }
                updateByPks(contentDtos);
                updateCategory(categoryIds);
                try {
                    baseSolrService.deleteByPks(SolrCoreEnum.DEFAULT_CORE, ids);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new CustomException(ApiResult.result(20020));
                }
            }
        }
    }

    // 更新 category
    private void updateCategory(Set<String> categoryIds){
        if (Checker.BeNotEmpty(categoryIds)) {
            List<CmsCategoryDto> cmsCategoryDtos = cmsCategoryService.getByPks(categoryIds);
            notifyComponent.asyncNotifyCategory(0,10,cmsCategoryDtos);
            notifyComponent.asyncNotifyHomePage(1,0);
        }
    }


    @Transactional
    @Override
    public void realDelContentByPks(List<String> ids) {
        if (Checker.BeNotEmpty(ids)) {
            deleteContentFile(ids);
            deleteByPks(ids);
            try {
                baseSolrService.deleteByPks(SolrCoreEnum.DEFAULT_CORE,ids);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new CustomException(ApiResult.result(20020));
            }
        }
    }

    private void deleteContentFile(List<String> ids){
        List<ContentDto> contentDtos = getByPks(ids);
        if(Checker.BeNotEmpty(contentDtos)){
            for(ContentDto contentDto:contentDtos){
                String url=contentDto.getUrl();
                if(Checker.BeNotBlank(url)){
                    url= byteCmsConfig.getSiteStaticFileRootPath()+url;
                    File file=new File(url);
                    if(file.exists()){
                        file.delete();
                    }
                }
                contentFileService.deleteByFiled("content_id", contentDto.getId());
            }
        }
    }

    public boolean ckHasContentByCategId(String categoryId, List<String> status) {
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId).in("status", status);
        int count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public ContentDto selectSinglePageContent(String categoryId) {
        ContentDto content = baseMapper.selectSinglePageContent(categoryId);
        boolean hasExtend = Checker.BeNotNull(content) && Checker.BeNotBlank(content.getData());
        if (hasExtend) {
            content.setExtendParam(JSONObject.parseObject(content.getData(),Map.class));
        }
        return content;
    }


    @Override
    public ContentDto getContentById(String contentId) {
        ContentDto contentDto = new ContentDto();
        if (Checker.BeNotBlank(contentId)) {
            contentDto=baseMapper.getContentById(contentId);
            if (Checker.BeNotBlank(contentDto.getData()) && Checker.BeNotBlank(contentDto.getExtendFieldList())) { // 存在扩展字段
                Map<String, Object> extendMap = JSONObject.parseObject(contentDto.getData(), Map.class);
                contentDto.setExtendParam(extendMap);
            }
        }
        return contentDto;
    }

    @Override
    public List<ContentDto> getContentsByTopTag(String tag,int rowNum,String categoryId) {
        List<ContentDto> contentDtos=baseMapper.getContentsByTopTag(tag,rowNum, categoryId);
        if(Checker.BeNotEmpty(contentDtos)){
            for(ContentDto contentDto:contentDtos){
                if (Checker.BeNotBlank(contentDto.getData()) && Checker.BeNotBlank(contentDto.getExtendFieldList())) { // 存在扩展字段
                    Map<String, Object> extendMap = JSONObject.parseObject(contentDto.getData(), Map.class);
                    contentDto.setExtendParam(extendMap);
                }
            }
        }
        return Checker.BeNotEmpty(contentDtos)?contentDtos:Lists.newArrayList();
    }

    @Override
    public PageDto<SolrDocument> searchKeyWord(PageDto<SolrSearchModel> pageDto) {
        PageDto<SolrDocument> resPageDto = null;
        try {
            resPageDto = baseSolrService.querySolr(SolrCoreEnum.DEFAULT_CORE, pageDto);
            searchResFilter(resPageDto.getRows());
        } catch (Exception e) {
            resPageDto = new PageDto<>(0, Lists.newArrayList());
            log.error(e.getMessage());
        }
        return resPageDto;
    }

    private  void searchResFilter(List<SolrDocument> rows) {
        Map<String,String> map = new HashMap<>(16);
        if(Checker.BeNotEmpty(rows)){
            Set<String> categoryIds = new HashSet<>(16);
            for(SolrDocument document:rows){
               Object category= document.getFieldValue("categoryId");
               if(Checker.BeNotNull(category) && Checker.BeNotBlank(category.toString())){
                   categoryIds.add(category.toString());
               }
            }
            List<CmsCategoryDto> categoryDtos=cmsCategoryService.getByPks(categoryIds);
            if(Checker.BeNotEmpty(categoryDtos)){
                categoryDtos.forEach(categoryDto->{
                    map.put(categoryDto.getId(),categoryDto.getName());
                    map.put("url_"+categoryDto.getId(),categoryDto.getContentPath());
                });
            }
            rows.forEach(row->{
                row.addField("categoryName",map.get(row.getFieldValue("categoryId")));
                row.addField("categoryUrl",map.get("url_"+row.getFieldValue("categoryId")));
            });
        }
    }



    @CacheClear(keys = {"getTopContent"})
    @Override
    public ApiResult top(ContentDto contentDto) {
        if (Checker.BeNotNull(contentDto) && Checker.BeNotBlank(contentDto.getId())) {
            ContentDto content = getByPk( contentDto.getId());
            content.setSort(contentDto.getSort()).setRecommend(contentDto.getRecommend()).setNotice(contentDto.getNotice())
            .setHot(contentDto.getHot());
            if(Checker.BeNotEmpty(contentDto.getTopTags())){
                content.setTopTag(StringUtils.join(contentDto.getTopTags(), ",").trim());
            }else{
                content.setTopTag("");
            }
            updateByPk(content);
            List<CmsCategoryDto> categoryDtos=cmsCategoryService.getByPks(Arrays.asList(content.getCategoryId()));
            if(Checker.BeNotEmpty(categoryDtos)){
                notifyComponent.asyncNotifyCategory(0,8,categoryDtos);
            }
            notifyComponent.asyncNotifyHomePage(1,0);
        }
        return ApiResult.result();
    }

    @Override
    public void updateContentModel(String categoryId, String modelId, String condition) {
        if (Checker.BeNotBlank(categoryId) && Checker.BeNotBlank(modelId)) {
            if (Checker.BeBlank(condition)) {
                condition = null;
            }
            baseMapper.updateContentModel(categoryId, modelId, condition);
        }
    }

    @Override
    public ApiResult move(String categoryId, List<String> ids) {
        if (Checker.BeNotEmpty(ids) && Checker.BeNotBlank(categoryId)) {
            List<ContentDto> contentDtos = getByPks(ids);
            if (Checker.BeNotEmpty(contentDtos)) {
                Set<String> categoryIds = new HashSet<>(16);
                categoryIds.add(categoryId);
                contentDtos.forEach(contentDto -> {
                    categoryIds.add(contentDto.getCategoryId());
                    contentDto.setCategoryId(categoryId);
                });
                updateByPks(contentDtos);
                List<CmsCategoryDto> cmsCategoryDtos = cmsCategoryService.getByPks(categoryIds);
                notifyComponent.asyncNotifyCategory(0,10,cmsCategoryDtos);
            }
        }
        return ApiResult.result();
    }

    @Cacheable(value=Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0+'.'+#p1+'.'+#p2",unless="#result == null")
    @Override
    public ContentDto getNextOrPreviousContentByIdAndCateg(String contentId, String categoryId, boolean isNext) {
        if (Checker.BeBlank(contentId))
        return null;
        ContentDto contentDto= baseMapper.getNextOrPreviousContentByIdAndCateg(contentId,categoryId,isNext);
        if (Checker.BeNotNull(contentDto) && Checker.BeNotBlank(contentDto.getData()) && Checker.BeNotBlank(contentDto.getExtendFieldList())) { // 存在扩展字段
            Map<String, Object> extendMap = JSONObject.parseObject(contentDto.getData(), Map.class);
            contentDto.setExtendParam(extendMap);
        }
        return contentDto;
//        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
//        if(Checker.BeNotBlank(categoryId)){
//            queryWrapper.eq("category_id",categoryId);
//        }
//        if(isNext){
//            queryWrapper.gt("id",contentId).orderByAsc("id + 0");
//        }else{
//            queryWrapper.lt("id",contentId).orderByDesc("id + 0");
//        }
//        queryWrapper.eq("status",1);
//        queryWrapper.last("limit 0 , 1");
//        Content content=baseMapper.selectOne(queryWrapper);
//        return Checker.BeNotNull(content)? T2D(content) : null;
    }

    @Override
    public void reStaticBatchGenCid(List<String> ids,String userId) {
        if(Checker.BeNotEmpty(ids)){
            List<ContentDto> contentDtos = baseMapper.reStaticBatchGenCid(ids);
            notifyComponent.asyncNotifyBatchContentGen(0,2,userId,contentDtos);
        }
    }

    @CacheClear(keys = {"clicksTopTotal"})
    @Override
    public Long searchClicks(String contentId) {
        long clicks = 1;
        String key = SecurityConstants.CONTENT_CLICK+contentId;
        Boolean hasKey=baseRedisService.hasKey(key);
        if(!hasKey){
            ContentDto contentDto= getByPk(contentId);
            if(Checker.BeNotNull(contentDto)){
                clicks = Checker.BeNotNull(contentDto.getClicks()) ? contentDto.getClicks()+1:1;
            }
            baseRedisService.set(key,clicks);
        }else{
            clicks=   baseRedisService.increment(key, 1);
        }
        return clicks;
    }


    @Cacheable(value=Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0+'.'+#p1",unless="#result == null")
    @Override
    public List<ContentDto> clicksTopTotal(Integer maxRowNum,String categoryId) {
        if(Checker.BeNull(maxRowNum)){
            maxRowNum = 10;
        }else if(maxRowNum>50){
            maxRowNum = 50;
        }
        QueryWrapper<Content> queryWrapper =new QueryWrapper();
        queryWrapper.select("id","title","clicks","category_id","author","url").orderByDesc("clicks").
        eq("status","1").last("limit "+maxRowNum);
        if(Checker.BeNotBlank(categoryId)){
            queryWrapper.eq("category_id",categoryId);
        }
        List<Content> contents =baseMapper.selectList(queryWrapper);
        return Checker.BeNotEmpty(contents)?T2DList(contents):Lists.newArrayList();
    }

    @Override
    public IPage<ContentDto> pageAllContentForToSolr(IPage<ContentDto> pages, List<String> status) {
        IPage<ContentDto> page= baseMapper.pageAllContentForToSolr(pages,status);
        return page;
    }


    @Cacheable(value=Constants.cacheName, key="#root.targetClass+'.'+#root.methodName+'.'+#p0.value+'.'+#p1+'.'+#p2",unless="#result == null")
    @Override
    public List<ContentDto> getTopContent(DirectiveNameEnum directive, Integer rowNum, String categoryId) {
        List<ContentDto> contentDtoList =new ArrayList<>(16);
        rowNum = Checker.BeNotNull(rowNum) ? rowNum: 6;
        QueryWrapper<Content> queryWrapper =new QueryWrapper<>();
        if(directive.equals(DirectiveNameEnum.CMS_RECOMM_DIRECTIVE)){
            queryWrapper.eq("recommend",1);
        }else if(directive.equals(DirectiveNameEnum.CMS_NOTICE_DIRECTIVE)){
            queryWrapper.eq("notice",1);
        }else if(directive.equals(DirectiveNameEnum.CMS_HOT_DIRECTIVE)){
            queryWrapper.eq("hot",1);
        }else if(directive.equals(DirectiveNameEnum.CMS_UPTODATE_DIRECTIVE)){
            queryWrapper.orderByDesc("recommend");
        }
        if(Checker.BeNotBlank(categoryId)){
            queryWrapper.eq("category_id",categoryId);
        }
        queryWrapper.eq("status",1).last("limit "+rowNum).orderByDesc("sort","publish_date");
        List<Content> contents =baseMapper.selectList(queryWrapper);
        if(Checker.BeNotEmpty(contents)){
            contentDtoList =  T2DList(contents);
            Map<String,Object> mapData=getMapData(contentDtoList);
            contentDtoList.forEach(content->{
                if(Checker.BeNotBlank(content.getCover())){
                    String cover=mapData.containsKey(content.getCover()) ? mapData.get(content.getCover()).toString():"";
                    content.setCover(cover);
                }
                if(Checker.BeNotBlank(content.getCategoryId())){
                    CmsCategoryDto cmsCategoryDto=mapData.containsKey(content.getCategoryId()) ? (CmsCategoryDto) mapData.get(content.getCategoryId()):null;
                    if(Checker.BeNotNull(cmsCategoryDto)){
                        content.setCategoryName(cmsCategoryDto.getName()).setCategoryId(cmsCategoryDto.getId()).setCategoryCode(cmsCategoryDto.getCode())
                        .setCategoryUrl(cmsCategoryDto.getContentPath());
                    }
                }
            });
        }
        return contentDtoList;
    }


    @Override
    public ApiResult searchGiveLikes(String contentId,String userAgent,Boolean isClick) {
        long giveLikes = 1;
        boolean isGiveLike = false;
        String key = SecurityConstants.GIVE_LIKES+contentId;
        String userAgentKey =  SecurityConstants.GIVE_LIKES+userAgent+contentId;
        Boolean hasKey=baseRedisService.hasKey(key);
        if(!hasKey){
            ContentDto contentDto= getByPk(contentId);
            if(Checker.BeNotNull(contentDto)){
                if(isClick){
                    giveLikes = Checker.BeNotNull(contentDto.getGiveLikes()) ? contentDto.getGiveLikes()+1:1;
                }else{
                    giveLikes = Checker.BeNotNull(contentDto.getGiveLikes()) ? contentDto.getGiveLikes():1;
                }
            }
            baseRedisService.set(key,giveLikes);
        }else{
            if(isClick){
                if(baseRedisService.hasKey(userAgentKey)){
                    isGiveLike = true;
                    giveLikes=  (Integer) baseRedisService.get(key);
                }else{
                    giveLikes=   baseRedisService.increment(key, 1);
                    baseRedisService.set(userAgentKey,1,10800L);
                }
            }else{
                giveLikes=  (Integer) baseRedisService.get(key);
            }
        }
        ApiResult apiResult=ApiResult.result(giveLikes);
        apiResult.put("isGiveLike",isGiveLike);
        return apiResult;
    }

    @Override
    public PageDto<ContentDto> searchByTag(PageDto<CmsTagsDto> pageDto) {
        IPage<ContentDto> pages = new Page<>(pageDto.getPageNo(), pageDto.getPageSize());
        IPage<ContentDto> result = baseMapper.searchByTag(pages, pageDto.getDto().getId());
        PageDto<ContentDto> resultSearch = new PageDto(result.getTotal(), result.getPages(), result.getCurrent(), Checker.BeNotEmpty(result.getRecords()) ? result.getRecords() : Lists.newArrayList());
        return resultSearch;
    }


    @Override
    public void jobPublish(ContentDto v) {
        Map<String,Object> param = new HashMap<>();
        param.put("contentIds",v.getIds());
        param.put("checkUserName",getUserName());
        param.put(SecurityConstants.USER_ID,getUserId());
        List<ContentDto> contentDtos=getByPks(v.getIds());
        if(Checker.BeNotEmpty(contentDtos)){
            contentDtos.forEach(contentDto->{
                contentDto.setExpiryDate(v.getExpiryDate());
            });
            updateByPks(contentDtos);
        }
        notifyComponent.asyncNotifyContentPublish(JobActionNotify.CONTENT_PUBLISH.setPreciseTime(v.getExpiryDate()).
        setGroup(UUID.randomUUID().toString()),param);
    }

    private Map<String,Object> getMapData(List<ContentDto> contents){
        Map<String,Object> map =new HashMap<>(16);
        if(Checker.BeNotEmpty(contents)){
            Set<String> coverIds = new HashSet<>();
            Set<String> categoryIds = new HashSet<>();
            contents.forEach(content->{
                if(Checker.BeNotBlank(content.getCover())){
                    coverIds.add(content.getCover());
                }
                if(Checker.BeNotBlank(content.getCategoryId())){
                    categoryIds.add(content.getCategoryId());
                }
            });
           List<SysResourceDto> sysResourceDtos= sysResourceService.getByPks(coverIds);
           List<CmsCategoryDto> categoryDtos=cmsCategoryService.getByPks(categoryIds);
           if(Checker.BeNotEmpty(sysResourceDtos)){
               sysResourceDtos.forEach(sysResourceDto->{
                   map.put(sysResourceDto.getId(),sysResourceDto.getFileFullPath());
               });
           }
            if(Checker.BeNotEmpty(categoryDtos)){
                categoryDtos.forEach(categoryDto->{
                    map.put(categoryDto.getId(),categoryDto);
                });
            }
        }
        return map;
    }


    private CmsModelDto checkerContent(ContentDto v) {
        CmsCategoryModelDto cmsCategoryModelDto = cmsCategoryModelService.getByField("category_id", v.getCategoryId());
        if (Checker.BeNull(cmsCategoryModelDto)) {
            throw new CustomException(ApiResult.result(20003));
        }
        CmsModelDto modelDto = cmsModelService.getByPk(cmsCategoryModelDto.getModelId());
        if (Checker.BeNotNull(modelDto)) {
            v.setModelId(modelDto.getId()).setHasFiles(modelDto.getHasFiles()).setHasImages(modelDto.getHasImages());
            if(Checker.BeNotBlank(cmsCategoryModelDto.getTemplatePath())){
                modelDto.setTemplatePath(cmsCategoryModelDto.getTemplatePath());
            }
        }
        return modelDto;
    }
}
