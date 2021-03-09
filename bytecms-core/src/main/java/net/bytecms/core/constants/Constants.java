package net.bytecms.core.constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.function.BinaryOperator;

/**
 *
 * Constants
 * 
 */
public class Constants {


    public static final  String cacheName="lb-cms-cache";

    public static final  String cacheNameClear="lb-cms-cache::";

    public static  final String fastDfsKeepUpload="file_keep_upload:";

    public static  final String AllowMultiLoginKey="allow_multi_login_key:";


    public static final String localtionUploadPattern="/thinkitcmsresource/";
    /**
     * Json Mapper
     */
    public static final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 随机数
     * 
     * Random
     */
    public static final Random random = new Random();
    /**
     * 默认字符编码名称
     * 
     * Default CharSet Name
     */
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";


    public static final String DEFAULT_HTML_SUFFIX = ".html";

    public static final String DEFAULT_ZIP_SUFFIX = ".zip";

    public static final String DEFAULT_HOME_PAGE = "index.html";

    public static final String PAGE_NO = "pageNo";

    public static final String PAGE_SIZE = "pageSize";

    public static final String PAGE_COUNT = "pageCount";

    public static final String EXTEND_FIELD_PREFIX =  "extend_field_";


    public static final String FDFS_GROUP =  "group1";

    public static final String DOMAIN = "domain";

    public static final String SERVER = "server";

    public static final String PARANT_CATEGORY_ID = "0";

    public static final String delRedisKey = "*";
    /**


    /**
     * 默认字符编码
     * 
     * Default CharSet
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
    /**
     * 间隔符
     * 
     * separator
     */
    public static final String SEPARATOR = "/";
    /**
     * 空白字符串
     * 
     * blank
     */
    public static final String BLANK = "";
    /**
     * 点
     * 
     * dot
     */
    public static final String DOT = ".";
    /**
     * 下划线
     * 
     * underline
     */
    public static final String UNDERLINE = "_";
    /**
     * 空格
     * 
     * blank space
     */
    public static final String BLANK_SPACE = " ";
    /**
     * 逗号分隔符
     * 
     * comma delimited
     */
    public static final String COMMA_DELIMITED = ",";
    /**
     * 逗号分隔符
     * 
     * comma delimited
     */
    public static final char COMMA = ',';
    /**
     * 文件路径间隔符号
     * 
     * comma delimited
     */
    public static final String FILE_PATH_DOUBLE_BACKLASH = "\\";
    
    /**
     * yyyy-MM-dd
     * 
     * comma delimited
     */
    public static final String YMD = "yyyy-MM-dd";

    public static final String YMD_ = "yyyyMMdd";
    
    /**
     * yyyyMMddHHmm
     * 
     * comma delimited
     */
    public static final String YMDHM = "yyyyMMddHHmm";

    /**
     * freemark 变量定义
     */

    public static final String categoryId="categoryId";

    public static final String categoryCode="categoryCode";

    public static final String categoryParentId="categoryParentId";

    public static final String rowNum="rowNum";

    public static final String codes="codes";

    public static final String code="code";

    public static final String hasRelated="hasRelated";

    public static final String hasTag="hasTag";

    public static final String hasFiles="hasFiles";

    public static final String contentId="contentId";

    public static final String topTag="topTag";

    public static final String url="url";

    public static final String title="title";

    public static final String keywords="keywords";

    public static final String description="description";

    public static final String progress="progress";

    public static final String staticFilePath="staticFilePath";

    public static final String categorys="categorys";



    /**
     * @return deafult meger function
     */
    public static <T> BinaryOperator<T> defaultMegerFunction() {
        return (first, second) -> first;
    }
}