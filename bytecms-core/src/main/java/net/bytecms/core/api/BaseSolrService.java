package net.bytecms.core.api;

import net.bytecms.core.constants.SolrCoreEnum;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.model.SolrSearchModel;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BaseSolrService {

    /**
     * solr 查询
     */
    PageDto<SolrDocument> querySolr(SolrCoreEnum coreEnum, PageDto<SolrSearchModel> pageDto) throws IOException, SolrServerException;
    /**
     * 根据ID查询
     */

    /**
     * 更新
     * @param coreEnum
     * @param id
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    void updateFieldById(SolrCoreEnum coreEnum, String id,Map<String,Object> param) throws IOException, SolrServerException;


    void updateFieldByIds(SolrCoreEnum coreEnum, List<String> ids,Map<String,Object> param);


    SolrDocument getById(SolrCoreEnum coreEnum, String id) throws IOException, SolrServerException;

    /**
     * 这个方法慎用
     * 删除全部索引
     * @param collection
     */
    void deleteAll(SolrCoreEnum collection) throws IOException, SolrServerException;


    /**
     * 根据主键删除
     * @param collection
     * @throws IOException
     * @throws SolrServerException
     */
    void deleteByPk(SolrCoreEnum collection,String id);


    /**
     * 根据主键批量删除
     * @param collection
     * @param ids
     * @throws IOException
     * @throws SolrServerException
     */
    void deleteByPks(SolrCoreEnum collection, List<String> ids) throws IOException, SolrServerException;


    /**
     * 添加内容
     */
    void addContent(SolrCoreEnum coreEnum, Map<String,Object> param) throws IOException, SolrServerException;

    void pushToSolr(SolrCoreEnum coreEnum,Object object);
}



