//package com.platon.browser.geo;
//
//import com.maxmind.geoip.Location;
//import com.maxmind.geoip.LookupService;
//
//import java.io.IOException;
//
//public class GeoTest {
//    public static void main(String[] args) {
//        try {
//            String path = LookupService.class.getClassLoader().getResource("dev/GeoLiteCity.dat").getPath();
//            LookupService cl = new LookupService(path, LookupService.GEOIP_MEMORY_CACHE);
//            Location l2 = cl.getLocation("14.215.177.39");
//            System.out.println(
//                    "countryCode: " + l2.countryCode +"\n"+
//                            "countryName: " + l2.countryName +"\n"+
//                            "region: " + l2.region +"\n"+
//                            "city: " + l2.city +"\n"+
//                            "latitude: " + l2.latitude +"\n"+
//                            "longitude: " + l2.longitude);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
