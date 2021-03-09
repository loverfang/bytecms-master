package net.bytecms.system.entity.online;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author dl
 * @since 2018-03-28
 */
@Data
@TableName("sys_online")
public class Online  extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableField("table_name")
    private String tableName;

    @TableField("table_note")
    private String tableNote;

    private String engine;

    private String prefix;
    
    private String author;
    
    @TableField("file_override")
    private int fileOverride;
    
    private int open;
    
    private int resultmap;
    
    @TableField("column_list")
    private int columnList;
    
    
    @TableField("enable_cache")
    private int enableCache;
    
    private String location;


    //模块名称
    @TableField("model_name")
    private String modelName;

    //controller 包位置
    @TableField("controller_position")
    private String controllerPosition;

    //api 包位置
    @TableField("api_position")
    private String apiPosition;

    @TableField("service_position")
    private String servicePosition;

    @TableField("entity_position")
    private String entityPosition;

    @TableField("dto_position")
    private String dtoPosition;

    @TableField("vue_js_position")
    private String vueJsPosition;

    @TableField("vue_page_position")
    private String vuePagePosition;


    @TableField("mapper_position")
    private String mapperPosition;


    @TableField("xml_position")
    private String xmlPosition;


}
