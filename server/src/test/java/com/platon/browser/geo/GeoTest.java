package com.platon.browser.geo;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import com.platon.browser.util.GeoUtil;

import java.io.IOException;

public class GeoTest {
    public static void main(String[] args) throws IOException, GeoIp2Exception {
        /*try {
            String path = LookupService.class.getClassLoader().getResource("dev/GeoLiteCity.dat").getPath();
            LookupService cl = new LookupService(path, LookupService.GEOIP_MEMORY_CACHE);
            Location l2 = cl.getLocation("14.215.177.39");
            System.out.println(
                    "countryCode: " + l2.countryCode +"\n"+
                            "countryName: " + l2.countryName +"\n"+
                            "region: " + l2.region +"\n"+
                            "city: " + l2.city +"\n"+
                            "latitude: " + l2.latitude +"\n"+
                            "longitude: " + l2.longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        CityResponse response = GeoUtil.getResponse("128.101.101.101");

        Country country = response.getCountry();
        System.out.println(country.getIsoCode());            // 'US'
        System.out.println(country.getName());               // 'United States'
        System.out.println(country.getNames().get("zh-CN")); // '美国'

        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println(subdivision.getName());    // 'Minnesota'
        System.out.println(subdivision.getIsoCode()); // 'MN'

        City city = response.getCity();
        System.out.println(city.getName()); // 'Minneapolis'

        Postal postal = response.getPostal();
        System.out.println(postal.getCode()); // '55455'

        Location location = response.getLocation();
        System.out.println(location.getLatitude());  // 44.9733
        System.out.println(location.getLongitude()); // -93.2323
    }
}
