package net.bytecms.core.service;

import net.bytecms.core.api.BaseSolrService;
import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.constants.SolrCoreEnum;
import net.bytecms.core.iterator.BeanPropertyBox;
import net.bytecms.core.iterator.SolrIterator;
import net.bytecms.core.iterator.ThinkIterator;
import net.bytecms.core.model.ConditionModel;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.model.SolrSearchModel;
import net.bytecms.core.utils.Checker;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class BaseSolrServiceImpl implements BaseSolrService {

    @Autowired
    ThinkCmsConfig thinkCmsConfig;
    /**
     * q:query input，要查询的字符串，必须填写
     *
     * fq: Filter Query,即过滤查询，表示符合q查询的结果并且符合fq查询的结果，个人理解为是多条件查询
     *
     * sort:排序，示例（id desc，price asc）表示先按id降序，再按价格升序，默认为降序
     *
     * start:完成对query查询的结果显示，start表示起始记录数，默认为0
     *
     * rows:配合start完成对查询结果集的分页。官方中解释为You can consider it as the maximum number of result appear in the page，也就是说rows表示，查询结果每页显示的最多数量，默认为10
     *
     * fl:field list.指定返回哪些字段，多个用逗号或空格隔开
     *
     * df:default field，默认查询的字段，一般为默认
     *
     * wt:write type，指定输出格式,可为JSON，XML等
     * @param coreEnum
     * @param pageDto
     * @return
     * @throws IOException
     * @throws SolrServerException
     */

    @Override
    public PageDto<SolrDocument> querySolr(SolrCoreEnum coreEnum, PageDto<SolrSearchModel> pageDto) throws IOException, SolrServerException {
        SolrQuery params = new SolrQuery();
        SolrSearchModel searchModel = pageDto.getDto();
        ConditionModel condition=searchModel.getCondition();
        if(Checker.BeNotNull(condition)){
           List<String> descFields= condition.getDescField();
           List<String> ascFields= condition.getAscField();
           if(Checker.BeNotEmpty(descFields)){
               descFields.forEach(descField->{
                   params.addSort(descField, SolrQuery.ORDER.desc);
               });
               ascFields.forEach(ascField->{
                   params.addSort(ascField, SolrQuery.ORDER.asc);
               });
           }
        }
        //查询条件, 这里的 q 对应 下面图片标红的地方
        String keyWord = Checker.BeNotBlank(searchModel.getKeyWords())?searchModel.getKeyWords():"*";
        params.set("q","content:"+keyWord);
        if(Checker.BeNotBlank(searchModel.getCategoryId())){
            params.set("fq","categoryId:"+searchModel.getCategoryId());
        }
        params.set("fq","status:1");
        params.setHighlight(true);
        //指定高亮域
        params.addHighlightField("id");
        params.addHighlightField("title");
        params.addHighlightField("content");
        params.addHighlightField("author");
        params.addHighlightField("editor");
        params.addHighlightField("description");
        params.setHighlightSimplePre("<span style='color:red'>");
        params.setHighlightSimplePost("</span>");
        params.setStart((int)(pageDto.getPageNo()-1)*10);
        params.setRows((int)pageDto.getPageSize());
        QueryResponse queryResponse = client.query(coreEnum.getValue(), params);
        SolrDocumentList results = queryResponse.getResults();
        long total = results.getNumFound(); // 查询到的结果
        //获取高亮显示的结果, 高亮显示的结果和查询结果是分开放的
        Map<String, Map<String, List<String>>> highlight = queryResponse.getHighlighting();
        List<SolrDocument> res = new ArrayList<>();
        String domain = thinkCmsConfig.getSiteDomain();
        for (SolrDocument result : results) { // 将高亮结果合并到查询结果中
            highlight.forEach((k, v) -> {
                if (result.get("id").equals(k)){
                    v.forEach((k1, v1) -> {
                       result.setField(k1, v1); // 高亮列合并如结果
                    });
                }
            });
            Object url = result.get("url");
            if(Checker.BeNotNull(url)){
                result.setField("url",domain+url.toString());
            }
            res.add(result);
        }
        PageDto<SolrDocument> resPageDto = new  PageDto<SolrDocument>(total,pageDto.getPageSize(),(int)pageDto.getPageNo(),res);
        return resPageDto;
    }

    @Override
    public void updateFieldById(SolrCoreEnum coreEnum, String id,Map<String,Object> param) throws IOException, SolrServerException {
        if(isStrtSolr()){
            SolrDocument solrDocument =getById(coreEnum,id);
            if(solrDocument!=null){
                Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    solrDocument.setField(entry.getKey(),entry.getValue());
                }
            }
            SolrInputDocument doc =toSolrInputDocument(solrDocument);
            client.add(coreEnum.getValue(),doc);
            client.commit(coreEnum.getValue());
        }
    }

    @Override
    public void updateFieldByIds(SolrCoreEnum coreEnum, List<String> ids, Map<String, Object> param)  {
        if(isStrtSolr()){
            if(Checker.BeNotEmpty(ids)){
                ids.forEach(id->{
                    try {
                        updateFieldById(coreEnum,id,param);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public SolrDocument getById(SolrCoreEnum coreEnum, String id) throws IOException, SolrServerException {
        return client.getById(coreEnum.getValue(), id);
    }

    @Override
    public void deleteAll(SolrCoreEnum coreEnum) throws IOException, SolrServerException {
        if(isStrtSolr()){
            client.deleteByQuery(coreEnum.getValue(), "*:*");
            client.commit(coreEnum.getValue());
        }
    }

    @Override
    public void deleteByPk(SolrCoreEnum coreEnum,String id) {
        if(isStrtSolr()){
            try {
                client.deleteById(coreEnum.getValue(),id);
                client.commit(coreEnum.getValue());
            }catch (IOException | SolrServerException e){
                log.error("solr 根据主键删除失败");
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void deleteByPks(SolrCoreEnum coreEnum, List<String> ids) throws IOException, SolrServerException {
        if(isStrtSolr()){
            client.deleteById(coreEnum.getValue(),ids);
            client.commit(coreEnum.getValue());
        }
    }

    @Override
    public void addContent(SolrCoreEnum coreEnum, Map<String, Object> param) throws IOException, SolrServerException {
        if(isStrtSolr()){
            if (Checker.BeNotEmpty(param)) {
                if(!param.containsKey("id")){
                    throw new SolrServerException("主键不能为空!");
                }
                SolrInputDocument doc = new SolrInputDocument();
                Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    doc.addField(entry.getKey(),entry.getValue());
                }
                client.add(coreEnum.getValue(),doc);
                client.commit(coreEnum.getValue());
            }
        }
    }

    @Override
    public void pushToSolr(SolrCoreEnum coreEnum,Object obj) {
        if(isStrtSolr()){
            Map<String,Object> params=new HashMap<>(16);
            BeanPropertyBox propertyBox=new BeanPropertyBox(obj);
            ThinkIterator iterator=propertyBox.iterator(SolrIterator.class);
            while (iterator.hasNext()){
                Map<String,Object> map= (Map)iterator.next();
                params.putAll(map);
            }
            try {
                addContent(coreEnum, params);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }


    private  SolrInputDocument toSolrInputDocument(SolrDocument solrDocument) {
        SolrInputDocument doc = new SolrInputDocument();
        for (String name : solrDocument.getFieldNames()) {
            doc.addField(name, solrDocument.getFieldValue(name));
        }
        return doc;
    }


    private boolean isStrtSolr(){
        return thinkCmsConfig.getStartSolr();
    }


    @Autowired
    private SolrClient client;



}
