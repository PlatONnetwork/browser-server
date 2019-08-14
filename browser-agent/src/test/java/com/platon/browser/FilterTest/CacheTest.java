//package com.platon.browser.FilterTest;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * User: dongqile
// * Date: 2019/1/25
// * Time: 10:32
// */
//public class CacheTest {
//
//    protected static Logger logger = LoggerFactory.getLogger(CacheTest.class);
//
//    public static CacheLoader<String, String> createCacheLoader () {
//        return new CacheLoader <String, String>() {
//            @Override
//            public String load ( String key ) throws Exception {
//                logger.info("加载并创建key" + key);
//
//                //return new Block(key, key + "dept", key + "id");
//                return null;
//            }
//        };
//
//    }
//
//    public void testCacha () {
//
//    }
///*    public static void main(String args[]){
//        String a = "01d033b5b07407e377a3eb268bdc3f07033774fb845b7826a6b741430c5e6b719bda5c4877514e8052fa5dbc2f20fb111a576f6696b6a16ca765de49e11e0541";
//        System.out.println(a.replaceFirst("^0*", ""));
//    }*/
//
//    public static void main ( String[] args ) {
//        Cache<Integer, String> cache = CacheBuilder.newBuilder().build();
//        cache.put(1, "a");
//        System.out.println(cache.getIfPresent(1));
//        System.out.println(cache.getIfPresent(2));
//    }
//}
//
//
