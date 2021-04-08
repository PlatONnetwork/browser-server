package com.platon.browser.model;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Common {

    /**
     * 项目路径
     */
    public static String ProjectUrl;

    public static String driverClass;

    public static String url;

    public static String username;

    public static String password;

    /**
     * 生成文件的路径
     */
    public static String filrUrl;

    /**
     * src目录
     */
    public static String SrcUrl;

    /**
     * 模板目录
     */
    public static String TemplateUrl;

    /**
     * 包名
     */
    public static String packagePath;

    /**
     * 包路径，把.转为/
     */
    public static String packagePathURL;

    /**
     * 模块名
     */
    public static String modelName;

    /**
     * 作者名
     */
    public static String author;

    /**
     * 生成文件的baseURL
     */
    public static String baseURL;

    /**
     * 生成mapper文件的路径
     */
    public static String mapperURL;

    /**
     * 生成xml文件的路径
     */
    public static String xmlURL;

    /**
     * 生成pojo文件的路径
     */
    public static String entityURL;

    /**
     * 生成service文件的路径
     */
    public static String serviceURL;

    /**
     * 生成serviceImpl文件的路径
     */
    public static String serviceImplURL;

    /**
     * 生成controller文件的路径
     */
    public static String controllerURL;

    /**
     * 生成dto文件的路径
     */
    public static String dtoURL;

    /**
     * columnDaoSet
     */
    public static Set<ColumnDao> columnDaoSet;

    static {
        // 获取根目录
        ProjectUrl = System.getProperty("user.dir") + "/browser-generator";
        String configUrl = Concat(ProjectUrl, "/src/main/resources", "config.setting");
        Setting setting = new Setting(configUrl, true);
        driverClass = setting.get("driverClass");
        url = setting.get("url");
        username = setting.get("username");
        password = setting.get("password");
        SrcUrl = setting.get("SrcUrl");
        TemplateUrl = setting.get("TemplateUrl");
        packagePath = setting.get("packagePath");
        packagePathURL = StrUtil.replaceChars(Common.packagePath, ".", "/");
        modelName = setting.get("modelName");
        author = setting.get("author");
        baseURL = setting.get("baseURL");
        mapperURL = setting.get("mapperURL");
        xmlURL = setting.get("xmlURL");
        entityURL = setting.get("entityURL");
        serviceURL = setting.get("serviceURL");
        serviceImplURL = setting.get("serviceImplURL");
        controllerURL = setting.get("controllerURL");
        dtoURL = setting.get("dtoURL");
        filrUrl = setting.get("filrUrl");
        if (StrUtil.isBlank(filrUrl)) {
            filrUrl = ProjectUrl;
        }
        columnDaoSet = new HashSet<ColumnDao>();
    }

    /**
     * 转换路径分隔符,如D:\aa\ss->D:\aa\ss\,再把\转为/
     *
     * @param filepath   要转换的路径
     * @param additional 是否最后加/
     * @return
     */
    public static String ChangeFileDelimiter(String filepath, Boolean additional) {
        // windows和linux的斜杠不一样，但是File.separator可以跨平台
        // File.pathSeparator指的是分隔连续多个路径字符串的分隔符
        // File.separator是用来分隔同一个路径字符串中的目录的,例如,C:\aa\bb就是指“\”
        if (additional) {
            if (!filepath.endsWith(File.separator)) {
                filepath = filepath + File.separator;
            }
        }
        filepath = filepath.replaceAll("\\\\", "/");
        filepath = filepath.replaceAll("//", "/");
        return filepath;
    }

    /**
     * 拼接文件路径
     *
     * @param filepath 多个路径
     * @return
     */
    public static String Concat(String... filepath) {
        String url = "";
        String path = "";
        int length = filepath.length - 1;
        for (int i = 0; i < filepath.length; i++) {
            if (i == length) {
                path = ChangeFileDelimiter(filepath[i], false);
            } else {
                path = ChangeFileDelimiter(filepath[i], true);
            }
            url += path;
        }
        url = ChangeFileDelimiter(url, false);
        return url;
    }

    /**
     * 取出src/main/resources/template下的所有模板文件
     *
     * @return
     */
    public static List<String> listFile() {
        String path = Concat(Common.ProjectUrl, Common.SrcUrl, Common.TemplateUrl);
        List<String> list = new ArrayList<String>();
        File[] s = FileUtil.ls(path);
        for (File file : s) {
            list.add(file.getName());
        }
        return list;
    }

}
