package com.platon.browser.util;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class GeoUtil {
    private final static Logger logger = Logger.getLogger(GeoUtil.class);
    public static Location getLocation(String ip){
        if(StringUtils.isBlank(ip)){
            logger.error("请指定IP地址！");
            throw new RuntimeException("请指定IP地址！");
        }
        if(!ipCheck(ip)){
            logger.error("IP地址不合法！");
            throw new RuntimeException("IP地址不合法！");
        }
        Location location=null;
        try {
            String path = LookupService.class.getClassLoader().getResource("GeoLiteCity.dat").getPath();
            LookupService service = new LookupService(path, LookupService.GEOIP_MEMORY_CACHE);
            location = service.getLocation(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    public static boolean ipCheck(String ip) {
        if (ip != null && !ip.isEmpty()) {
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."+
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
            "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            if (ip.matches(regex)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
