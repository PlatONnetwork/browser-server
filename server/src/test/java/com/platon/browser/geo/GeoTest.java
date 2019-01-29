package com.platon.browser.geo;

import com.platon.browser.util.GeoUtil;

import java.util.Arrays;

public class GeoTest {
   /* public static void main(String[] args) throws IOException, GeoIp2Exception {
        *//*try {
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
        }*//*


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
    }*/




    public static void main(String[] args) {
        class IP{
            String name,ip;
            public IP(String name,String ip){
                this.name=name;
                this.ip=ip;
            }
        }
       IP[] ips = {
               new IP("梵蒂冈","212.77.1.243"),
               new IP("德国法兰克福","108.61.210.117"),
               new IP("美国西雅图","108.61.194.105"),
               new IP("美国纽约","170.3.239.102"),
               new IP("美国明尼阿波利斯","63.229.193.92"),
               new IP("日本大阪","218.42.250.255"),
               new IP("日本东京","218.132.30.255"),
               new IP("法国巴黎","193.54.67.4"),
               new IP("莫斯科","89.208.161.81"),
               new IP("韩国首尔","112.144.9.177"),
               new IP("美国休斯顿","99.172.41.65"),
               new IP("澳大利亚","175.39.24.42"),
               new IP("菲律宾 Akamai","27.126.152.134"),
               new IP("印尼","114.4.39.202"),
               new IP("文莱","43.251.128.123"),
               new IP("芬兰","194.112.15.25"),
               new IP("休斯顿","128.42.194.95"),
               new IP("日本北海道","103.10.112.0"),
               new IP("吉隆坡","223.25.242.116"),
               new IP("德国","45.32.152.0"),
               new IP("旧金山","169.230.104.141"),
               new IP("洛杉矶","103.49.209.27"),

       };
        Arrays.asList(ips).forEach(ip->{
            GeoUtil.IpLocation location = GeoUtil.getIpLocation(ip.ip);
            System.out.println("name:【"+ip.name+"】,ip:【"+ip.ip+"】,longitude:【"+location.getLongitude()+"】,latitude:【"+location.getLatitude()+"】");
        });
    }
}
