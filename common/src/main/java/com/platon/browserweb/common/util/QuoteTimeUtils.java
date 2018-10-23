package com.platon.browserweb.common.util;

import com.platon.browserweb.common.enums.KlineType;

import java.util.Calendar;
import java.util.Date;

public class QuoteTimeUtils {
    public static Date getQuoteTime1( KlineType kline, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int[] zeros = kline.getZeros();
        for (int zero : zeros) {
            c.set(zero, 0);
        }

        if (KlineType.WEEKLY.equals(kline)) {
            c.add(kline.getIdentify(), 2 - c.get(kline.getIdentify()));
        } else if (kline.getCurrentDiff() != 0) {
            int diff = c.get(kline.getIdentify());
            c.set(kline.getIdentify(), diff / kline.getCurrentDiff() * kline.getCurrentDiff());
        }
        return c.getTime();
    }


    public static Date getQuoteTime(KlineType kline, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        switch (kline) {
            case MIN_1:
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                return c.getTime();
            case MIN_5:

                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                int fiveMin = c.get(Calendar.MINUTE);
                if (fiveMin <= 4) {
                    c.set(Calendar.MINUTE, 0);
                } else if (fiveMin > 4 && fiveMin <= 9) {
                    c.set(Calendar.MINUTE, 5);
                } else if (fiveMin > 9 && fiveMin <= 14) {
                    c.set(Calendar.MINUTE, 10);
                } else if (fiveMin > 14 && fiveMin <= 19) {
                    c.set(Calendar.MINUTE, 15);
                } else if (fiveMin > 19 && fiveMin <= 24) {
                    c.set(Calendar.MINUTE, 20);
                } else if (fiveMin > 24 && fiveMin <= 29) {
                    c.set(Calendar.MINUTE, 25);
                } else if (fiveMin > 29 && fiveMin <= 34) {
                    c.set(Calendar.MINUTE, 30);
                } else if (fiveMin > 34 && fiveMin <= 39) {
                    c.set(Calendar.MINUTE, 35);
                } else if (fiveMin > 39 && fiveMin <= 44) {
                    c.set(Calendar.MINUTE, 40);
                } else if (fiveMin > 44 && fiveMin <= 49) {
                    c.set(Calendar.MINUTE, 45);
                } else if (fiveMin > 49 && fiveMin <= 54) {
                    c.set(Calendar.MINUTE, 50);
                } else if (fiveMin > 54) {
                    c.set(Calendar.MINUTE, 55);
                }
                return c.getTime();
            case MIN_15:
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                int fifteenMin = c.get(Calendar.MINUTE);
                if (fifteenMin <= 14) {
                    c.set(Calendar.MINUTE, 0);
                } else if (fifteenMin > 14 && fifteenMin <= 29) {
                    c.set(Calendar.MINUTE, 15);
                } else if (fifteenMin > 29 && fifteenMin <= 44) {
                    c.set(Calendar.MINUTE, 30);
                } else if (fifteenMin > 44 && fifteenMin <= 59) {
                    c.set(Calendar.MINUTE, 45);
                }
                return c.getTime();
            case MIN_30:
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                int thirtyMin =  c.get(Calendar.MINUTE);
                if (thirtyMin <= 29) {
                    c.set(Calendar.MINUTE, 0);
                } else if (thirtyMin > 29) {
                    c.set(Calendar.MINUTE, 30);
                }
                return c.getTime();
            case HOUR_1:
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                return c.getTime();
            case DAILY:
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                return c.getTime();
            case WEEKLY:
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                //星期一开始算，如果是星期日开始算，则1 - c.get(Calendar.DAY_OF_WEEK))
                c.add(Calendar.DAY_OF_WEEK, 2 - c.get(Calendar.DAY_OF_WEEK));
                return c.getTime();
            default:
                return date;
        }
    }

    public static Date getPreviousQuoteTime1(KlineType kline, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int[] zeros = kline.getZeros();
        for (int zero : zeros) {
            c.set(zero, 0);
        }

        if (KlineType.WEEKLY.equals(kline)) {
            c.add(Calendar.DAY_OF_MONTH, -7);
            c.add(kline.getIdentify(), 2 - c.get(kline.getIdentify()));
        } else if (kline.getCurrentDiff() != 0) {
            int diff = c.get(kline.getIdentify());
            c.set(kline.getIdentify(), diff / kline.getCurrentDiff() * kline.getCurrentDiff());
        }
        return c.getTime();
    }

    public static Date getPreviousQuoteTime(KlineType kline, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        switch (kline) {
            case MIN_1:
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                c.add(Calendar.MINUTE, -1);
                return c.getTime();
            case MIN_5:

                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                c.add(Calendar.MINUTE, -5);

                int fiveMin = c.get(Calendar.MINUTE);
                if (fiveMin <= 4) {
                    c.set(Calendar.MINUTE, 0);
                } else if (fiveMin > 4 && fiveMin <= 9) {
                    c.set(Calendar.MINUTE, 5);
                } else if (fiveMin > 9 && fiveMin <= 14) {
                    c.set(Calendar.MINUTE, 10);
                } else if (fiveMin > 14 && fiveMin <= 19) {
                    c.set(Calendar.MINUTE, 15);
                } else if (fiveMin > 19 && fiveMin <= 24) {
                    c.set(Calendar.MINUTE, 20);
                } else if (fiveMin > 24 && fiveMin <= 29) {
                    c.set(Calendar.MINUTE, 25);
                } else if (fiveMin > 29 && fiveMin <= 34) {
                    c.set(Calendar.MINUTE, 30);
                } else if (fiveMin > 34 && fiveMin <= 39) {
                    c.set(Calendar.MINUTE, 35);
                } else if (fiveMin > 39 && fiveMin <= 44) {
                    c.set(Calendar.MINUTE, 40);
                } else if (fiveMin > 44 && fiveMin <= 49) {
                    c.set(Calendar.MINUTE, 45);
                } else if (fiveMin > 49 && fiveMin <= 54) {
                    c.set(Calendar.MINUTE, 50);
                } else if (fiveMin > 54) {
                    c.set(Calendar.MINUTE, 55);
                }
                return c.getTime();
            case MIN_15:
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                c.add(Calendar.MINUTE, -15);

                int fifteenMin = c.get(Calendar.MINUTE);
                if (fifteenMin <= 14) {
                    c.set(Calendar.MINUTE, 0);
                } else if (fifteenMin > 14 && fifteenMin <= 29) {
                    c.set(Calendar.MINUTE, 15);
                } else if (fifteenMin > 29 && fifteenMin <= 44) {
                    c.set(Calendar.MINUTE, 30);
                } else if (fifteenMin > 44 && fifteenMin <= 59) {
                    c.set(Calendar.MINUTE, 45);
                }
                return c.getTime();
            case MIN_30:
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                c.add(Calendar.MINUTE, -30);
                int thirtyMin =  c.get(Calendar.MINUTE);
                if (thirtyMin <= 29) {
                    c.set(Calendar.MINUTE, 0);
                } else if (thirtyMin > 29) {
                    c.set(Calendar.MINUTE, 30);
                }
                return c.getTime();
            case HOUR_1:
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                c.add(Calendar.HOUR_OF_DAY, -1);

                return c.getTime();
            case DAILY:
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                c.add(Calendar.DAY_OF_MONTH, -1);

                return c.getTime();
            case WEEKLY:
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                c.add(Calendar.DAY_OF_MONTH, -7);
                //星期一开始算，如果是星期日开始算，则1 - c.get(Calendar.DAY_OF_WEEK))
                c.add(Calendar.DAY_OF_WEEK, 2 - c.get(Calendar.DAY_OF_WEEK));
                return c.getTime();
            default:
                return date;
        }
    }
}
