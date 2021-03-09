package net.bytecms.service.dto.fragment;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 * 页面片段数据表
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FragmentDto extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;


    /**
     * 封面图
     */
    @DirectMark
    private String cover;



    private SysResourceDto resource;


    /**
     * 片段路径
     */
    private String path;


    /**
     * 标题
     */
    @DirectMark
    private String title;


    /**
     * 超链接
     */
    @DirectMark
    private String url;


    /**
     * 排序
     */
    @DirectMark
    private Integer sort;


    /**
     * 片段模型文件 id
     */
    private String fragmentFileModelId;


    private String data;

    @DirectMark
    private Map<String,Object> extendParam;
}
