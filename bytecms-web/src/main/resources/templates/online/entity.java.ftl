package ${package.Entity};
import java.io.Serializable;
<#list table.importPackages as pkg>
import ${pkg};
</#list>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * <p>
 * ${table.comment}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
<#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
</#if>
@Accessors(chain = true)
<#if table.convert>
@TableName("${table.name}")
</#if>
@TableName("${table.name}")
<#if superEntityClass??>
public class ${cfg.customEntityName?cap_first} extends BaseModel {
<#elseif activeRecord>
public class ${cfg.customEntityName?cap_first} extends BaseModel {
<#else>
public class ${cfg.customEntityName?cap_first} extends BaseModel {
</#if>

private static final long serialVersionUID = 1L;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
<#if field.keyFlag>
<#assign keyPropertyName="${field.propertyName}"/>
</#if>

<#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
</#if>
<#if field.keyFlag>
<#-- 主键 -->
<#if field.keyIdentityFlag>
    @TableId(value = "${field.name}", type = IdType.AUTO)
<#elseif idType??>
    @TableId(value = "${field.name}", type = IdType.${idType})
<#elseif field.convert>
    @TableId("${field.name}")
</#if>
<#-- 普通字段 -->
<#elseif field.fill??>
<#-- -----   存在字段填充设置   ----->
<#if field.convert>
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
<#else>
    @TableField(fill = FieldFill.${field.fill})
</#if>
<#elseif field.convert>
    @TableField("${field.name}")
</#if>
<#-- 乐观锁注解 -->
<#if versionFieldName!"" == field.name>
    @Version
</#if>
<#-- 逻辑删除注解 -->
<#if logicDeleteFieldName!"" == field.name>
    @TableLogic
</#if>

    <#if (field.name != 'gmt_modified' && field.name != 'gmt_create' && field.name != 'create_id' && field.name != 'modified_id')>
        <#if field.name == 'id'>
        @TableId("id")
        <#else>
        @TableField("${field.name}")
        </#if>
        private ${field.propertyType} ${field.propertyName};
    </#if>

</#list>
<#------------  END 字段循环遍历  ---------->



<#if entityColumnConstant>
<#list table.fields as field>
    public static final String ${field.name?upper_case} = "${field.name}";
</#list>
</#if>

}
