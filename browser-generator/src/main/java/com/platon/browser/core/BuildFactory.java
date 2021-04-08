package com.platon.browser.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.platon.browser.model.Column;
import com.platon.browser.model.ColumnDao;
import com.platon.browser.model.Common;
import com.platon.browser.model.DbConfig;
import com.platon.browser.util.MysqlDaoUtil;
import com.platon.browser.util.TemplateCoreUtil;


import java.io.File;
import java.util.List;

public class BuildFactory
{
    private static final Log log = LogFactory.get();

    /**
     * 生成所有的数据库表
     */
    public void coreRun()
    {
        DbConfig dbConfig = new DbConfig();
        MysqlDaoUtil mysqlDaoUtil = new MysqlDaoUtil();
        List<String> AllTableNamelist = mysqlDaoUtil.queryAllTables(dbConfig);
        String[] strArray = AllTableNamelist.toArray(new String[AllTableNamelist.size()]);
        coreRun(strArray);
        log.info("生成所有的数据库表成功");
    }

    /**
     * @param tableNames
     *            需要生成的数据库表名
     */
    public void coreRun(String... tableNames)
    {
        DbConfig dbConfig = new DbConfig();
        MysqlDaoUtil mysqlDaoUtil = new MysqlDaoUtil();
        List<String> templateList = Common.listFile();
        TemplateCoreUtil templateCore = new TemplateCoreUtil();
        for (String tableName : tableNames)
        {
            ColumnDao columnDao = mysqlDaoUtil.queryColumns(dbConfig, tableName);
            Common.columnDaoSet.add(columnDao);
            for (String templateName : templateList)
            {
                String fileUrl = Common.Concat(Common.filrUrl, Common.baseURL, Common.packagePathURL, Common.modelName);
                switch (templateName) {
                case "Dto.ftl":
                    String dtofile = Common.Concat(fileUrl, Common.dtoURL, MysqlDaoUtil.toClassName(tableName) + "DTO.java");
                    common(templateCore, templateName, tableName, columnDao, dtofile);
                    break;
                case "Mapper.ftl":
                    String mapperfile = Common.Concat(fileUrl, Common.mapperURL, MysqlDaoUtil.toClassName(tableName) + "Mapper.java");
                    common(templateCore, templateName, tableName, columnDao, mapperfile);
                    break;
                case "Mybatis_xml.ftl":
                    String xmlfile = Common.Concat(fileUrl, Common.xmlURL, MysqlDaoUtil.toClassName(tableName) + "Mapper.xml");
                    common(templateCore, templateName, tableName, columnDao, xmlfile);
                    break;
                case "Pojo.ftl":
                    String pojofile = Common.Concat(fileUrl, Common.entityURL, MysqlDaoUtil.toClassName(tableName) + ".java");
                    common(templateCore, templateName, tableName, columnDao, pojofile);
                    break;
                case "Service_spi.ftl":
                    String spifile = Common.Concat(fileUrl, Common.serviceURL, MysqlDaoUtil.toClassName(tableName) + "Service.java");
                    common(templateCore, templateName, tableName, columnDao, spifile);
                    break;
                case "Service_impl.ftl":
                    String implfile = Common.Concat(fileUrl, Common.serviceImplURL, MysqlDaoUtil.toClassName(tableName) + "ServiceImpl.java");
                    common(templateCore, templateName, tableName, columnDao, implfile);
                    break;
                case "Controller.ftl":
                    String controfile = Common.Concat(fileUrl, Common.controllerURL, MysqlDaoUtil.toClassName(tableName) + "Controller.java");
                    common(templateCore, templateName, tableName, columnDao, controfile);
                    break;
                case "ParamValidGroup.ftl":
                    String paramfile = Common.Concat(fileUrl, "common", "ParamValidGroup.java");
                    common(templateCore, templateName, tableName, columnDao, paramfile);
                    break;
                default:
                    break;
                }
            }
        }
    }

    private void common(TemplateCoreUtil templateCore, String templateName, String tableName, ColumnDao columnDao, String filepath)
    {
        Template template = templateCore.getTemplate(templateName);
        if (template == null)
        {
            log.error("{}模板为空", templateName);
        }
        String className = MysqlDaoUtil.toClassName(tableName);
        addTemplateParam(tableName, className, columnDao, template, filepath);
    }

    /**
     * 添加模板参数
     *
     * @param tableName
     *            表名
     * @param className
     *            表名对应的类名
     * @param columnDao
     *            字段集
     * @param template
     *            模板
     * @param fileurl
     *            生成的文件路径
     */
    private void addTemplateParam(String tableName, String className, ColumnDao columnDao, Template template, String fileurl)
    {
        String primaryKey = "";
        String primaryKey_java = "";
        for (Column column : columnDao.getColumnList())
        {
            if (column.getIsPrimary())
            {
                primaryKey = column.getName();
                primaryKey_java = column.getJavaName();
            }
        }
        // @formatter:off
        Dict dict = Dict.create().set("package_path", Common.packagePath)//包名
                .set("model", Common.modelName)//模块名
                .set("author", Common.author)//作者名
                .set("class_name", className)//类名
                .set("LowerCaseClassName", MysqlDaoUtil.toLowerCaseFirstOne(className))//首字母小写的类名
                .set("table_column", columnDao.getColumnList())//表字段列表
                .set("table_name", tableName)//表名
                .set("hasBigDecimalColumn", Column.typeContains(columnDao.getColumnList(), "BigDecimal"))//判断columns里面是否有BigDecimal类型
                .set("hasDateColumn", Column.typeContains(columnDao.getColumnList(), "Date"))//判断columns里面是否有Date类型
                .set("tableRemark", columnDao.getTableRemark())//表注释
                .set("columnDaoSet", Common.columnDaoSet)//所有的columnDao对象，用于字段分组
                .set("primaryKey", primaryKey)//主键名
                .set("primaryKey_java", primaryKey_java);//主键java名
        // @formatter:on
        File file = FileUtil.touch(fileurl);
        template.render(dict, file);
        log.info("生成文件{}成功", fileurl);
    }

}
