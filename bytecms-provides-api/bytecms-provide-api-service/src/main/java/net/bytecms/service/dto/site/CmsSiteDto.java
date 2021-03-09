package net.bytecms.service.dto.site;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author LG
 * @since 2019-12-11
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmsSiteDto extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 站点名称
     */
    @DirectMark
    private String siteName;


    /**
     * 关键字
     */
    @DirectMark
    private String siteKeyWords;


    /**
     * 站点描述
     */
    @DirectMark
    private String siteDesc;


    /**
     * logo
     */
    @DirectMark
    private String siteLogo;


    /**
     * 第三方统计代码
     */
    @DirectMark
    private String statisticalCode;


    /**
     * 第三方分享代码
     */
    @DirectMark
    private String shareCode;


    /**
     * 第三方评论代码
     */
    @DirectMark
    private String commentCode;


    /**
     * 域名
     */
    @DirectMark
    private String domain;


    /**
     * 扩展参数
     */
    @DirectMark
    Map<String, Object> extendParams;


}
