package ${package_path}<#if (model?exists && model!="")>.${model}</#if>.common.paramvalidgroup;

/**
 * 用于字段校验分组
 */
public interface ParamValidGroup{
	<#list columnDaoSet as c>
		 /**
		 * ${c.tableRemark!}
		 */
		 interface ${c.className!}{
			<#list table_column as c2>
				<#if (c2.isPrimary=true)>
			     /**
			     * ${c2.javaName}不能为Null
			     */
			     </#if>
			</#list>
		 	 interface Group1{
	        }
		 }
	</#list>
}
