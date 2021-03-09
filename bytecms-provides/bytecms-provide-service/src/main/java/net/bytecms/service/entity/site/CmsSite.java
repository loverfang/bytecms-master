package net.bytecms.service.entity.site;

import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 *
 * </p>
 *
 * @author LG
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_site")
public class CmsSite extends BaseModel {

    private static final long serialVersionUID = 1L;


    @TableId("id")
    private String id;


    /**
     * 站点名称
     */

    @TableField("site_name")
    private String siteName;


    /**
     * 关键字
     */

    @TableField("site_key_words")
    private String siteKeyWords;


    /**
     * 站点描述
     */

    @TableField("site_desc")
    private String siteDesc;


    /**
     * logo
     */

    @TableField("site_logo")
    private String siteLogo;


    /**
     * 第三方统计代码
     */

    @TableField("statistical_code")
    private String statisticalCode;


    /**
     * 第三方分享代码
     */

    @TableField("share_code")
    private String shareCode;



    /**
     * 第三方评论代码
     */
    @TableField("comment_code")
    private String commentCode;


    /**
     * 域名
     */

    @TableField("domain")
    private String domain;


}
