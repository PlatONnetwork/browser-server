package ${package_path}<#if (model?exists && model!="")>.${model}</#if>.dto;
import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.common.ParamValidGroup;
<#if (hasDateColumn)>
import java.util.Date;
</#if>
<#if (hasBigDecimalColumn)>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * ${tableRemark!}
 * @author ${author}
 */
@ApiModel(description = "${tableRemark!}")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ${class_name}DTO  implements Serializable {
    private static final long serialVersionUID = 1L;

<#list table_column as c>
    <#if (c.remark?exists && c.remark!="")>
     /**
     * ${c.remark}，数据库字段<#if (c.isPrimary=true)>为主键且</#if><#if (c.isNull!=true)>不</#if>可为Null
     */
    </#if>
    <#if (c.name!="uuid")>
     <#if (c.isPrimary=true)>
     @NotNull(message = "${c.javaName}不能为空", groups = { ParamValidGroup.${class_name}.Group1.class })
     </#if>
	 <#if (c.remark?exists && c.remark!="")>@ApiModelProperty(value = "${c.remark}",example = "")</#if>
     private ${c.type} ${c.javaName};
    </#if>
</#list>
<#--
<#list table_column as c>
    <#if (c.name!="uuid")>
        <#if (c.remark?exists && c.remark!="")>
	    /**
	     * @return ${c.remark}，数据库字段<#if (c.isNull!=true)>不</#if>可为Null
	     */
        </#if>
        public ${c.type} get${c.getsetJavaName}()
	    {
	        return ${c.javaName};
	    }

	    <#if (c.remark?exists && c.remark!="")>
	    /**
	     * @param ${c.javaName} ${c.remark}，数据库字段<#if (c.isNull!=true)>不</#if>可为Null
	     * @return
	     */
        </#if>
	    public ${class_name} set${c.getsetJavaName}(${c.type} ${c.javaName})
	    {
	        this.${c.javaName} = ${c.javaName};
	        return this;
	    }

    </#if>
</#list>
-->
}
