package ${cfg.customDtoPack};
import java.io.Serializable;
import BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
<#list table.importPackages as pkg>
import ${pkg};
</#list>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * ${table.comment}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@Accessors(chain = true)
<#if superEntityClass??>
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${cfg.customDtoName?cap_first} extends BaseModel{
<#elseif activeRecord>
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${cfg.customDtoName?cap_first} extends BaseModel {
<#else>
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${cfg.customDtoName?cap_first} extends BaseModel implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
<#if field.keyFlag>
<#assign keyPropertyName="${field.propertyName}"/>
</#if>

    <#if (field.name != 'gmt_modified' && field.name != 'gmt_create' && field.name != 'create_id' && field.name != 'modified_id')>
        <#if field.comment!?length gt 0>
        /**
        * ${field.comment}
        */
        </#if>
        <#-- <#if field.keyFlag>
        @TableId("id")
        <#else>
        @TableField("${field.name}")
        </#if>
        -------->
        private ${field.propertyType} ${field.propertyName};
</#if>

</#list>
<#------------  END 字段循环遍历  ---------->

<#if entityColumnConstant>
<#list table.fields as field>
    public static final String ${field.name?upper_case} = "${field.name}";

</#list>
</#if>
<#if activeRecord>
    @Override
    protected Serializable pkVal() {
<#if keyPropertyName??>
        return this.${keyPropertyName};
<#else>
        return this.id;
</#if>
    }

</#if>

}
