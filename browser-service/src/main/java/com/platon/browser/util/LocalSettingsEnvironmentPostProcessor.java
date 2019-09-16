//package com.platon.browser.util;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.env.EnvironmentPostProcessor;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.MutablePropertySources;
//import org.springframework.core.env.PropertiesPropertySource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.support.PropertiesLoaderUtils;
//
//
///**
// *  数据库用户名密码加密
// *  @file LocalSettingsEnvironmentPostProcessor.java
// *  @description 
// *	@author zhangrj
// *  @data 2019年9月12日
// */
//public class LocalSettingsEnvironmentPostProcessor implements EnvironmentPostProcessor {
//	
//	private Logger log = LoggerFactory.getLogger(LocalSettingsEnvironmentPostProcessor.class);
//
//	private static String fileName = "redirectjasypt.properties";
//	
//	private static String jasFileName = "jasypt.properties";
//	
//	private static String key = "jasypt.path";
//	
//	private static String proName = "Config";
//	
//	private static String LOCATIONS [] = {};
//	static {
//        Properties properties = new Properties();
//        // 使用ClassLoader加载properties配置文件生成对应的输入流
//        InputStream in = LocalSettingsEnvironmentPostProcessor.class.getClassLoader().getResourceAsStream(fileName);
//        // 使用properties对象加载输入流
//        try {
//            properties.load(in);
//       } catch (IOException e) {
//            e.printStackTrace();
//       }
//        //获取key对应的value值
//        LOCATIONS = properties.getProperty(key).split(",");
//   }
//	@Override
//	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
//		MutablePropertySources propertySources = environment.getPropertySources();
//		Properties properties = loadProperties();
//		if(properties != null) {
//			//优先加载同目录下文件
//			propertySources.addFirst(new PropertiesPropertySource(proName, properties));
//			return;
//		}
//		for(String fileLocation :  LOCATIONS){
//            File file = new File(fileLocation);
//            //循环加载config的源文件
//            if (file.exists()) {
//                properties = loadProperties(file);
//                propertySources.addFirst(new PropertiesPropertySource(proName, properties));
//                return ;
//           }
//       }
//
//	}
//
//	/**
//	 * 加载文件目录
//	 * @method loadProperties
//	 * @param f
//	 * @return
//	 */
//	private Properties loadProperties(File f) {
//		log.info("加载目录：{}", f.getPath());
//        FileSystemResource resource = new FileSystemResource(f);
//        try {
//            return PropertiesLoaderUtils.loadProperties(resource);
//       }
//        catch (Exception ex) {
//        	log.info("加载目录失败：{}", f.getPath());
//            return null;
//       }
//   }
//	
//	/**
//	 * 加载jar同级目录下文件
//	 * @method loadProperties
//	 * @return
//	 */
//	private Properties loadProperties() {
//		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//		log.debug("加载loadProperties目录：{}", path);
//		int startindex = path.indexOf("file");
//		if(startindex != -1) {
//			path = path.substring(5, path.length());
//		}
//		int endIndex = path.indexOf("browser");
//		if(endIndex != -1) {
//			path = path.substring(0, endIndex);
//		}
//		try {
//			path = java.net.URLDecoder.decode(path, "utf-8");
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.info("加载同级目录失败：{}", path);
//			return null;
//		}
//		return loadProperties(new File(path + File.separator + jasFileName));
//	}
//	
//}
