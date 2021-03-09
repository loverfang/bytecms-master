package net.bytecms.core.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "thinkitcms")
public class ThinkCmsConfig {

    /**
     * 资源根目录
     */
    public String sourceRootPath;

    /**
     * 模板目录
     */
    public String sourceTempPath;


    /**
     * 碎片文件（页面片段）目录
     */
    public String sourceFragmentFilePath;


    /**
     * 网站静态资源目录
     */
    public String siteStaticFileRootPath;


    /**
     * 前端网站地址
     */
    public String siteDomain;


    /**
     * 服务器地址
     */
    public String serverApi;


    /**
     * fastdfs domain
     */
    public String siteFdfsDomain;


    /**
     * 百度统计 url
     */
    public String baiDuTongJiUrl;


    /**
     * 百度统计 url 手机端地址
     */
    public String baiDuTongjiUrlM;


    /**
     * 是否允许一个账号多地方登录
     */
    public Boolean allowMultiLogin=true;


    /**
     * 是否开启 全文检索存储
     */
    public Boolean startSolr=false;


    /**
     * 证书路径
     */
    public String licensePath=sourceRootPath;


    /**
     * 上传的资源文件（只有开启本地存储的上传方式时有效）
     */
    public String fileResourcePath;

    /**
     * 插件基础路径
     */
    public String pluginsBasePath;

    /**
     * 第三方对象存储
     */
    public ThinkCmsConfig.oss oss = new ThinkCmsConfig.oss();

    @Data
    public static class oss{

        private String accessKey;

        private String secretKey ;

        private String endpoint;

        private String bucket ;

        private String prefix;
    }
}
