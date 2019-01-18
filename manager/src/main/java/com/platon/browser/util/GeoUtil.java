package com.platon.browser.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

public class GeoUtil {
    private final static Logger logger = LoggerFactory.getLogger(GeoUtil.class);
    private static DatabaseReader reader;

    static {
        try {
            reader = new DatabaseReader.Builder(GeoUtil.class.getClassLoader().getResource("GeoLite2-City.mmdb").openStream()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class IpLocation {
        private String ip;
        private String countryCode;
        private String location;
        private String longitude;
        private String latitude;
    }

    public static IpLocation getIpLocation(String ip){
        IpLocation il = new IpLocation();
        il.setIp(ip);

        try{
            CityResponse response = GeoUtil.getResponse(ip);
            if(response!=null){
                Location location = response.getLocation();
                if(location!=null){
                    il.setLatitude(String.valueOf(location.getLatitude()));
                    il.setLongitude(String.valueOf(location.getLongitude()));
                }
                Country country = response.getCountry();
                if(country!=null){
                    il.setCountryCode(country.getIsoCode());
                    il.setLocation(country.getName());
                }

                City city = response.getCity();
                if(city!=null){
                    if(StringUtils.isNotBlank(city.getName())){
                        il.setLocation(il.getLocation()+" "+city.getName());
                    }
                }
            }else{
                throw new RuntimeException("");
            }
        }catch (Exception ex){
            // ip不合法，直接返回默认数据
            il.setCountryCode("0");
            il.setLocation("");
            il.setLatitude("0");
            il.setLongitude("0");
            logger.debug("Cant't resolve ip location: {}", ip);
        }

        return il;
    }

    public static CityResponse getResponse(String ip){
        CityResponse response=null;
        if(ipCheck(ip)){
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);
                response = reader.city(ipAddress);
            } catch (IOException | GeoIp2Exception e) {
                logger.debug(e.getMessage());
            }
        }
        return response;
    }

    public static boolean ipCheck(String ip) {
        if(StringUtils.isBlank(ip)){
            logger.debug("请指定IP地址！");
            throw new RuntimeException("请指定IP地址！");
        }
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."+
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        if (ip.matches(regex)) {
            return true;
        }
        logger.debug("IP地址不合法！");
        throw new RuntimeException("IP地址不合法！");
    }

}
