package com.platon.browserweb.Controller;

import com.github.fartherp.framework.common.util.DateUtil;


import java.text.SimpleDateFormat;
import java.util.*;

import com.platon.browserweb.common.enums.DateType2Enum;
import com.platon.browserweb.common.enums.DateTypeEnum;
import com.platon.browserweb.common.enums.NewDateTypeEnum;
import org.apache.commons.lang3.StringUtils;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class BasicsController {



	public Map<String, String> getTime(Integer datype, String startTime, String endTime) {
		Map<String, String> params = new HashMap<>();
		Date start = null;
		Date end = new Date();
		if (DateTypeEnum.TODAY.getCode().equals(datype)) {
			// 近1日
			Calendar c = Calendar.getInstance();
			c.set(HOUR_OF_DAY, 0);
			c.set(MINUTE, 0);
			c.set(SECOND, 0);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, c.getTime());
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (DateTypeEnum.WEEK.getCode().equals(datype)) {
			// 近1周
			start = DateUtil.getDateByCalendar(new Date(), Calendar.WEEK_OF_YEAR, -1);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (DateTypeEnum.MONTH1.getCode().equals(datype)) {
			// 近1月
			start = DateUtil.getDateByCalendar(new Date(), Calendar.MONTH, -1);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (DateTypeEnum.MONTH3.getCode().equals(datype)) {
			// 近3月
			start = DateUtil.getDateByCalendar(new Date(), Calendar.MONTH, -3);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (DateTypeEnum.MONTH6.getCode().equals(datype)) {
			// 近6月
			start = DateUtil.getDateByCalendar(new Date(), Calendar.MONTH, -6);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (DateTypeEnum.YEAR.getCode().equals(datype)) {
			// 近1年
			start = DateUtil.getDateByCalendar(new Date(), Calendar.YEAR, -1);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} /*else if (DateTypeEnum.NOW.getCode().equals(datype)) {
			// 至今
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (DateTypeEnum.THREE_TODAY.getCode().equals(datype)) {
			// 最近三天
			start = DateUtil.getDateByCalendar(new Date(), Calendar.DATE, -3);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		}*/ else {
			startTime = stampToDate(startTime);
			endTime =  stampToDate(endTime);
		}
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return params;
	}

	public Map<String, String> getTime2(Integer datype, String startTime, String endTime) {
		Map<String, String> params = new HashMap<>();
		Date start = null;
		Date end = null;
		if (DateType2Enum.LASTHREEDAY.getCode().equals(datype)) {
			// 近三天
			start = DateUtil.getDateByCalendar(new Date(), Calendar.DATE, -3);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, new Date());
		} else if (DateType2Enum.BEFORETHREEDAY.getCode().equals(datype)) {
			// 三天以前
			if(StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)){
				end = DateUtil.getDateByCalendar(new Date(), Calendar.DATE, -3);
				start = DateUtil.getDateByCalendar(new Date(), Calendar.DATE, -6);
				startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
				endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
			}else {
				startTime = stampToDate(startTime);
				endTime =  stampToDate(endTime);
			}

		}
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return params;
	}

	public static void main(String args[]){
		BasicsController basicsController = new BasicsController();
		Map<String, String> o = basicsController.getTime2(8,null,null);
		System.out.println(o.get("startTime"));
		System.out.println(o.get("endTime"));
	}
//	public String getIP(HttpServletRequest request) {
//		String ip = request.getHeader("x-forwarded-for");
//		if (!checkIP(ip)) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if (!checkIP(ip)) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (!checkIP(ip)) {
//			ip = request.getRemoteAddr();
//		}
//		return ip;
//	}

	public boolean checkIP(String ip) {
		return ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)
				&& ip.split("\\.").length == 4;
	}

	public Map<String, String> getNewTime(Integer datype, String startTime, String endTime) {
		Map<String, String> params = new HashMap<>();
		Date start = null;
		Date end = new Date();
		if (NewDateTypeEnum.TODAY.getCode().equals(datype)) {
			// 近1日
			Calendar c = Calendar.getInstance();
			c.set(HOUR_OF_DAY, 0);
			c.set(MINUTE, 0);
			c.set(SECOND, 0);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, c.getTime());
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (NewDateTypeEnum.WEEK.getCode().equals(datype)) {
			// 近1周
			start = DateUtil.getDateByCalendar(new Date(), Calendar.WEEK_OF_YEAR, -1);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (NewDateTypeEnum.MONTH.getCode().equals(datype)) {
			// 近1月
			start = DateUtil.getDateByCalendar(new Date(), Calendar.MONTH, -1);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (NewDateTypeEnum.MONTH3.getCode().equals(datype)) {
			// 近3月
			start = DateUtil.getDateByCalendar(new Date(), Calendar.MONTH, -3);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		}  else if (NewDateTypeEnum.YEAR.getCode().equals(datype)) {
			// 近1年
			start = DateUtil.getDateByCalendar(new Date(), Calendar.YEAR, -1);
			startTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, start);
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else if (NewDateTypeEnum.NOW.getCode().equals(datype)) {
			// 至今
			endTime = DateUtil.format(DateUtil.yyyy_MM_dd_HH_mm_ss, end);
		} else {
			// 自定义
		}
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return params;
	}


	public static String stampToDate(String s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
}
