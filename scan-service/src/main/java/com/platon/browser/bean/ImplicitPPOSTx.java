package com.platon.browser.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;

@Data
public class ImplicitPPOSTx {
    private String from;
    private String to;
    //private Integer fnCode;
    //特殊节点收集的函数参数列表，如果是nil，则序列化成json时，会是null，在这里反序列化时，在List<>中会占位，但是element==null，所以，在decode时，只需要函数声明中的参数序号获取参数并根据不同参数对null值进行适当处理（比如, name可能处理成"",number可能处理成0）
    //特殊节点收集的函数参数，类型如果是big.Int，序列成json时，数字个数有多有少，在这里反序列化时，造成反序列化成Long/Integer/BigInteger等类型
    //private List<Object> fnParams;
    private String inputHex;
    private String logDataHex;

    public static void main(String[] args){
        String json = "[{\n" +
                "\t\"from\": \"lat1qyqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqdyd7z7\",\n" +
                "\t\"to\": \"lat1qgqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq07ycrm\",\n" +
                "\t\"fnCode\": 1001,\n" +
                "\t\"fnParams\": [\"lat1qyqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqdyd7z7\", 120000, \"\", null, 678000000000000000000000],\n" +
                "\t\"logDataHex\": \"0xf869308382271095940102030000000000000000000000000000000000b842b840102030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008988746573744e6f6465\"\n" +
                "}]";

        System.out.println(json);

        String json2 = "[{\n" +
                "\t\"from\": \"lat1qyqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqdyd7z7\",\n" +
                "\t\"to\": \"lat1qgqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq07ycrm\",\n" +
                "\t\"fnCode\": 1001,\n" +
                "\t\"fnParams\": [\"lat1e8su9veseal8t8eyj0zuw49nfkvtqlun94k4qa\",[{\"epoch\":1,\"amount\":100000000000000000000},{\"epoch\":2,\"amount\":100000000000000000000},{\"epoch\":3,\"amount\":100000000000000000000},{\"epoch\":4,\"amount\":100000000000000000000},{\"epoch\":5,\"amount\":100000000000000000000}]]\n" +
                "\t\"logDataHex\": \"0xf869308382271095940102030000000000000000000000000000000000b842b840102030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008988746573744e6f6465\"\n" +
                "}]";

        String json3 = "[{\n" +
                "\t\"from\": \"lat1qyqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqdyd7z7\",\n" +
                "\t\"to\": \"lat1qgqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq07ycrm\",\n" +
                "\t\"inputHex\": \"0xf85a83820fa09594c9e1c2b330cf7e759f2493c5c754b34d98b07f93b83ef83ccb0189056bc75e2d63100000cb0289056bc75e2d63100000cb0389056bc75e2d63100000cb0489056bc75e2d63100000cb0589056bc75e2d63100000\",\n" +
                "\t\"logDataHex\": \"0xf869308382271095940102030000000000000000000000000000000000b842b840102030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008988746573744e6f6465\"\n" +
                "}]";

        List<ImplicitPPOSTx> implicitPPOSTxList = JSON.parseArray(json3, ImplicitPPOSTx.class);
        ImplicitPPOSTx element = implicitPPOSTxList.get(0);

        System.out.println( "inputHex:" + element.getInputHex());
        System.out.println( "logDataHex:" + element.getLogDataHex());
    }
}

