package com.platon.browser.utils;

public class CommonUtil {

    public static String getTime(long lastTime, long startTime, long endTime) {
        long h = (endTime - startTime + lastTime) / 1000 / 60 / 60;
        long m = ((endTime - startTime + lastTime) / 1000 / 60) % 60;
        return h + "h:" + m + "m";
    }

    public static long resolving(String time) {
        String[] times = time.split(":");
        String h = times[0].substring(0, times[0].length() - 1);
        String m = times[1].substring(0, times[1].length() - 1);
        System.out.println("打印结果为：" + h + "   " + m);
        long hh = new Long(h) * 60 * 60 * 1000;
        long mm = new Long(m) * 60 * 1000;
        return hh + mm;
    }

    /**
     * 判断当前系统是win(true)还是linux(false)
     *
     * @param
     * @return boolean
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/13
     */
    public static boolean isWin() {
        String osname = System.getProperties().getProperty("os.name");
        if (osname.toLowerCase().indexOf("win") > -1) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("打印结果为：" + System.getProperties().getProperty("os.name").toLowerCase());
        System.out.println("打印结果为：" + isWin());
    }

}
