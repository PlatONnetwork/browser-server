package com.platon.browser.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

public class GeoUtil {
    private final static Logger logger = Logger.getLogger(GeoUtil.class);
    /*public static Location getLocation(String ip){
        Location location=null;
        if(ipCheck(ip)){
            try {
                String path = LookupService.class.getClassLoader().getResource("GeoLiteCity.dat").getPath();
                LookupService service = new LookupService(path, LookupService.GEOIP_MEMORY_CACHE);
                location = service.getLocation(ip);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return location;
    }*/

    public static CityResponse getResponse(String ip){
        CityResponse response=null;
        if(ipCheck(ip)){
            try {
                InputStream inputStream = GeoUtil.class.getClassLoader().getResource("GeoLite2-City.mmdb").openStream();
                DatabaseReader reader = new DatabaseReader.Builder(inputStream).build();
                InetAddress ipAddress = InetAddress.getByName(ip);
                response = reader.city(ipAddress);
            } catch (IOException | GeoIp2Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public static boolean ipCheck(String ip) {
        if(StringUtils.isBlank(ip)){
            logger.error("请指定IP地址！");
            throw new RuntimeException("请指定IP地址！");
        }
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."+
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        if (ip.matches(regex)) {
            return true;
        }
        logger.error("IP地址不合法！");
        throw new RuntimeException("IP地址不合法！");
    }
}
