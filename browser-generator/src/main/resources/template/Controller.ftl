package ${package_path}<#if (model?exists && model!="")>.${model}</#if>.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.context.annotation.Scope;
import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.service.${class_name}Service;
import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.entity.${class_name};
import ${package_path}<#if (model?exists && model!="")>.${model}</#if>.common.ParamValidGroup;
/**
 * ${tableRemark!}管理
 * @author ${author}
 */
@SuppressWarnings("all")
@RestController
@Scope("prototype")
@RequestMapping("/${class_name}Controller")
public class ${class_name}Controller
{
	private static final Log log = LogFactory.get();

	@Resource
    private ${class_name}Service ${LowerCaseClassName}Service;

     /**
     * 添加${class_name}
     *
     * @param request
     * @param ${LowerCaseClassName}
     * @return
     */
    @RequestMapping(value = "add${class_name}")
    public void add${class_name}(HttpServletRequest request, @RequestBody ${class_name} ${LowerCaseClassName})
    {
           log.info("入参是={}",${LowerCaseClassName});
           int flag= ${LowerCaseClassName}Service.insert(${LowerCaseClassName});
           if(flag==1){
           log.info("添加成功={}",${LowerCaseClassName});
           }else{
           log.error("添加失败={}",${LowerCaseClassName});
           }
    }

     /**
     * 删除${class_name}
     *
     * @param request
     * @param ${LowerCaseClassName}
     * @return
     */
    @RequestMapping(value = "delete${class_name}")
    public void delete${class_name}(HttpServletRequest request, @RequestBody @Validated({ ParamValidGroup.${class_name}.Group1.class }) ${class_name} ${LowerCaseClassName})
    {
           log.info("入参是={}",${LowerCaseClassName});
           <#list table_column as c>
			    <#if (c.isPrimary=true)>
			      int flag= ${LowerCaseClassName}Service.delete(${LowerCaseClassName}.get${c.getsetJavaName}());
			    </#if>
			</#list>
           if(flag==1){
           log.info("删除成功={}",${LowerCaseClassName});
           }else{
           log.error("删除失败={}",${LowerCaseClassName});
           }
    }

     /**
     * 修改${class_name}
     *
     * @param request
     * @param ${LowerCaseClassName}
     * @return
     */
    @RequestMapping(value = "update${class_name}")
    public void update${class_name}(HttpServletRequest request, @RequestBody @Validated({ ParamValidGroup.${class_name}.Group1.class }) ${class_name} ${LowerCaseClassName})
    {
           log.info("入参是={}",${LowerCaseClassName});
           int flag= ${LowerCaseClassName}Service.update(${LowerCaseClassName});
           if(flag==1){
           log.info("修改成功={}",${LowerCaseClassName});
           }else{
           log.error("修改失败={}",${LowerCaseClassName});
           }
    }

      /**
     * 条件查询${class_name}
     *
     * @param request
     * @param ${LowerCaseClassName}
     * @return
     */
    @RequestMapping(value = "find${class_name}List")
    public void find${class_name}List(HttpServletRequest request, @RequestBody ${class_name} ${LowerCaseClassName})
    {
           log.info("入参是={}",${LowerCaseClassName});
           List<${class_name}> ${LowerCaseClassName}List= ${LowerCaseClassName}Service.findList(${LowerCaseClassName});
           log.info("条件查询成功={}",${LowerCaseClassName}List);
    }
}
