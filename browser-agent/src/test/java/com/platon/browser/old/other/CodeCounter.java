//package com.platon.browser.other;
//
///**
// * User: dongqile
// * Date: 2019/8/31
// * Time: 11:47
// */
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//public class CodeCounter {
//    static long codeLines = 0;
//    static long commentLines = 0;
//    static long blankLines = 0;
//    static ArrayList<File> fileArray = new ArrayList<File>();
//
//
//    public static void main(String[] args) {
//
//        //可以统计指定目录下以及其子目录下的所有java文件中代码
//        File file = new File("D:\\GitWorkspace\\browser-server");
//
//        ArrayList<File> al = getFile(file);
//        for (File f : al) {
//            if (f.getName().matches(".*\\.java$")) // 匹配java格式的文件
////              if (f.getName().matches(".*\\.xml$")) // 匹配xml格式的文件
////              if (f.getName().matches(".*\\.sql$")) // 匹配sql格式的文件
////            if (f.getName().matches(".*\\.properties$")) // 匹配properties格式的文件
////            if (f.getName().matches(".*\\.jsp$")) // 匹配jsp格式的文件
////            if (f.getName().matches(".*\\.js$")) // 匹配js格式的文件
//                count(f);
//        }
//        System.out.println("代码行数：" + codeLines);
//        System.out.println("注释行数：" + commentLines);
//        System.out.println("空白行数： " + blankLines);
//    }
//
//    // 获得目录下的文件和子目录下的文件
//    public static ArrayList<File> getFile(File f) {
//        File[] ff = f.listFiles();
//        for (File child : ff) {
//            if (child.isDirectory()) {
//                getFile(child);
//            } else
//                fileArray.add(child);
//        }
//        return fileArray;
//
//    }
//
//    // 统计方法
//    private static void count(File f) {
//        BufferedReader br = null;
//        boolean flag = false;
//        try {
//            br = new BufferedReader(new FileReader(f));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                line = line.trim(); // 除去注释前的空格
//                if (line.matches("^[ ]*$")) { // 匹配空行
//                    blankLines++;
//                } else if (line.startsWith("//")) {
//                    commentLines++;
//                } else if (line.startsWith("")) {
//                    commentLines++;
//                    flag = true;
//                } else if (line.startsWith("")) {
//                    commentLines++;
//                } else if (flag == true) {
//                    commentLines++;
//                    if (line.endsWith("*/")) {
//                        flag = false;
//                    }
//                } else {
//                    codeLines++;
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                    br = null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
