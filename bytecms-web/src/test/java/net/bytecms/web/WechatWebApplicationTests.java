package net.bytecms.web;

import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.api.BaseSolrService;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.model.SolrSearchModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatWebApplicationTests {

    @Autowired
    BaseRedisService<String,Object> baseRedisService ;

    @Autowired
    BaseSolrService baseSolrService;

    @Test
    public void redisDelKey()
    {
        String key = "lb-cms-cache::class com.org.lb.business.service.fragment.FragmentServiceImpl.getFragmentDataByCode *";
        baseRedisService.removeBlear(key);
    }

    @Test
    public void solrSearch() throws IOException, SolrServerException {
        SolrSearchModel solrSearchModel =new SolrSearchModel();
        solrSearchModel.setKeyWords("美丽");
        PageDto<SolrSearchModel> pageDto =new PageDto<>();
        pageDto.setDto(solrSearchModel);
//        SolrDocumentList solrDocuments=baseSolrService.querySolr(SolrCoreEnum.DEFAULT_CORE,pageDto);
//        System.out.println(solrDocuments);

    }

    @Test
    public void toList(){
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("List转字符串");
        List<String> list1 = new ArrayList<String>();
        list1.add("张三");
        list1.add("李四");
        list1.add("王五");
        String ss = String.join(",", list1);
        System.out.println(StringUtils.join("",list1));
        System.out.println(ss);
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("字符串转List");
        List<String> listString = Arrays.asList(ss.split(","));
        for (String string : listString) {
            System.out.println(string);
        }
        System.out.println("+++++++++++++++++++++++++++++++++");
    }

}
