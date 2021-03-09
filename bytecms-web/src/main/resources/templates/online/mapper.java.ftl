package ${package.Mapper};

import ${package.Entity}.${cfg.customEntityName?cap_first};
import ${superMapperClassPackage};
import org.apache.ibatis.annotations.Mapper;
/**
 * <p>
 * ${table.comment} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
@Mapper
public interface ${cfg.customMapperName?cap_first} extends ${superMapperClass}<${cfg.customEntityName?cap_first}> {

}
</#if>
