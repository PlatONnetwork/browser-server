package com.platon.browser.util;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateConfig.ResourceMode;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.platon.browser.model.Common;

public class TemplateCoreUtil {

    private static final Log log = LogFactory.get();

    private TemplateEngine engine;

    public TemplateCoreUtil() {
        try {
            String templateUrl = Common.Concat(Common.ProjectUrl, Common.SrcUrl, Common.TemplateUrl);
            log.info("模板路径={}", templateUrl);
            templateUrl = Common.ChangeFileDelimiter(templateUrl, false);
            TemplateConfig templateConfig = new TemplateConfig(templateUrl, ResourceMode.FILE);
            engine = TemplateUtil.createEngine(templateConfig);
            log.info("初始化模板引擎成功");
        } catch (Exception e) {
            log.error(e, "初始化模板引擎失败");
        }
    }

    /**
     * 获取模板
     *
     * @param templateName 模板文件名
     * @return
     */
    public Template getTemplate(String templateName) {
        try {
            if (engine != null) {
                Template template = engine.getTemplate(templateName);
                log.info("获取模板{}成功", templateName);
                return template;
            } else {
                log.error("请先初始化模板引擎");
                return null;
            }
        } catch (Exception e) {
            log.error("获取模板失败");
            return null;
        }
    }

}
