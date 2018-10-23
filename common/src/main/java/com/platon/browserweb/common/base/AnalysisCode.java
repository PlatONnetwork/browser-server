package com.platon.browserweb.common.base;

/**
 * User: dongqile
 * Date: 2018/7/6
 * Time: 17:27
 */
public final  class  AnalysisCode {
    public static String AnalysisUserId(String str){
        String a = str.substring(str.indexOf(":")+1, str.length());
        return a;
    }
    public static String AnalysisUserType(String str){
        String b = str.substring(0, str.lastIndexOf(":"));
        return b;
    }

    public static String AnalysisCoin(String str){
        String b = str.substring(0, str.lastIndexOf("_"));
        return b;
    }

    public static String TradeoZne(String str){
        String a = str.substring(str.indexOf("_")+1, str.length());
        return a;
    }

    public static String AnalysisSellExecId (String str){
        String a = str.substring(str.indexOf("|")+1, str.length());
        return a;
    }
    public static String AnalysisBuyExecId(String str){
        String b = str.substring(0, str.lastIndexOf("|"));
        return b;
    }
/*    public static void main(String args[]){
        AnalysisCode analysisCode = new AnalysisCode();
        String a = "BTC_USDT";
        String b = analysisCode.AnalysisCoin(a);
        System.out.print(b);
    }*/
}