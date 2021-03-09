package net.bytecms.system.dto.online;

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
public class OnlineDto extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String tableName;

    private String tableNote;

    private String engine;

    private String position;

    private String prefix;
    
    private String author;
    
    private int fileOverride;
    
    private int open;
    
    private int resultmap;
    
    private int columnList;

    private int enableCache;
    
    private String location;

    //模块名称
    private String modelName;

    //controller 包位置
    private String controllerPosition;

    //api 包位置
    private String apiPosition;

    private String servicePosition;

    private String entityPosition;

    private String dtoPosition;

    private String vueJsPosition;

    private String vuePagePosition;

    private String mapperPosition;


    private String xmlPosition;

}
