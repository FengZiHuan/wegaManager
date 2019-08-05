package com.nantian.iwap.app.util;


import hirondelle.date4j.DateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
/**
* @ClassName: DateUtil 
* @Description: 日期处理工具
* @author: ZCP 
* @date: 2015-4-1
*
 */
public final class DateUtil {

	private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();
	
	private DateUtil() {
		super();
	}

	private static Logger logger = Logger.getLogger(DateUtil.class);
	// 工作时间段
	public final static String WORK_TIME_MIN = " 05:30";
	public final static String WORK_TIME_MIN_FOUR = " 04:00";
	public final static String WORK_TIME_MIN_24 = " 24:00";
	public final static String WORK_TIME_MAX = " 23:30";

	public final static String TIME_MIN = " 00:00";
	public final static String TIME_MAX = " 23:60";
	
	/**
	 * 用于限制考勤设备告警时间段
	 */
	public static final String WARNING_INIT_TIME = "00:00:00";

	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public final static String HH_MM = "HH:mm";
	public final static String HHMM = "HHmm";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYY_MM_DD_E_CN = "yyyy年MM月dd日  E";
	public final static String YYYYMMDD_L = "yyyyMMdd";
	public final static String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public final static String YYYYMMDD = "yyyy/MM/dd";
	public final static String YYYYMM = "yyyy/MM";
	public final static String YYYY = "yyyy";
	public final static String YYYYMMDD_HH_MM = "yyyy/MM/dd HH:mm";
	public final static String YYYYMMDDHHmm = "yyyyMMddHHmm";
	public final static String DEFAULT_FORMAT_DATE1 = "yyyy/MM/dd";
	public final static String DEFAULT_FORMAT_DATE = "yyyy-MM-dd";

	public final static String DEFAULT_FORMAT_TIME = "HH:mm:ss";
	public final static String DEFAULT_FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
	public static final String[] MM = { "JAN", "FEB", "MAR", "APR", "MAY",
			"JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	public static final String[] WEEK = { "SUNDAY", "MONDAY", "TUESDAY",
			"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
	public final static String DB_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH24:mi:ss";
	public final static String DB_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH24:mi";
	public final static String DB_YYYY_MM_DD_HH = "yyyy-MM-dd HH24";
	public final static String DB_YYYY_MM_DD = "yyyy-MM-dd";

	public final static String DEF_YYYY_MM_DD = "2000-01-01";
	public final static String DEF_YYYY_MM_DD_HH_MM = DEF_YYYY_MM_DD + " 00:00";
	public final static Date defDate = DateUtil.parseDate(DEF_YYYY_MM_DD_HH_MM,
			DateUtil.YYYY_MM_DD_HH_MM);
	/**
	 * 一分钟毫秒数
	 */
	public final static int millisecond = 60000;

	/**
	 * date1 是否小于或等于 date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isLessOrEquals(Date date1, Date date2) {
		if (date1 != null && date2 != null
				&& (date1.before(date2) || date1.getTime() == date2.getTime())) {
			return true;
		}
		return false;
	}

	/**
	 * date1 是否大于或等于 date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isGreaterOrEquals(Date date1, Date date2) {
		if (date1 != null && date2 != null
				&& (date1.after(date2) || date1.getTime() == date2.getTime())) {
			return true;
		}
		return false;
	}

	/**
	 * @Desription: 判断某个日期(精确到日期)是否在开始、结束日期之间(如果相等，也表示在这个区间)
	 * @param d
	 *            要判断的日期
	 * @param startDt
	 *            开始日期
	 * @param endDt
	 *            结束日期
	 * @return
	 * @Return:boolean
	 * @Author: 田红兵
	 * @CreateDate:2015-3-16
	 */
	public static boolean isAtInterval(Date d, Date startDt, Date endDt) {
		d = getDateForDay(d);
		startDt = getDateForDay(startDt);
		endDt = getDateForDay(endDt);
		return (d.getTime() >= startDt.getTime() && d.getTime() <= endDt
				.getTime());
	}

	/**
	 * @Desription:判断某个日期是否在开始、结束日期之间(如果相等，也表示在这个区间)
	 * @param sDt
	 *            判断日期开始时间
	 * @param eDt
	 *            判断日期结束时间
	 * @param startDt
	 *            开始时间
	 * @param endDt
	 *            结束时间
	 * @return
	 * @Return:boolean
	 * @Author:true
	 * @CreateDate:2015-3-19
	 */
	public static boolean isAtIntervalForTime(Date sDt, Date eDt, Date startDt,
			Date endDt) {
		if (sDt != null && eDt != null && startDt != null && endDt != null) {
			if ((sDt.getTime() >= startDt.getTime() && sDt.getTime() <= endDt
					.getTime())
					&& (eDt.getTime() >= startDt.getTime() && eDt.getTime() <= endDt
							.getTime())) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @Desription: 判断日期是否跟开始、结束日期之间重叠到
	 * @param sDt
	 *            判断日期开始时间
	 * @param eDt
	 *            判断日期结束时间
	 * @param startDt
	 *            开始时间
	 * @param endDt
	 *            结束时间
	 * @return
	 * @Return:boolean true 重叠
	 * @Author:ldb
	 * @CreateDate:2015-3-20
	 */
	public static boolean isAtIntervalForTimeNotExesit(Date sDt, Date eDt,
			Date startDt, Date endDt) {
		if (sDt != null && eDt != null && startDt != null && endDt != null) {
			if ((sDt.getTime() >= startDt.getTime() && sDt.getTime() < endDt
					.getTime())
					|| (eDt.getTime() > startDt.getTime() && eDt.getTime() <= endDt
							.getTime())
					|| (sDt.getTime() <= startDt.getTime() && eDt.getTime() >= endDt
					.getTime())) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
	
	/**
	 * @Desription: 判断日期是否跟开始、结束日期之间重叠到
	 * @param sDt
	 *            判断日期开始时间
	 * @param eDt
	 *            判断日期结束时间
	 * @param startDt
	 *            开始时间
	 * @param endDt
	 *            结束时间
	 * @return
	 * @Return:boolean true 重叠
	 * @Author:ldb
	 * @CreateDate:2015-3-20
	 */
	public static boolean isAtIntervalForTimeNotExesit2(Date sDt, Date eDt,
			Date startDt, Date endDt) {
		if (sDt != null && eDt != null && startDt != null && endDt != null) {
			if ((sDt.getTime() >= startDt.getTime() && sDt.getTime() < endDt
					.getTime())
					|| (eDt.getTime() > startDt.getTime() && eDt.getTime() <= endDt
							.getTime())
					|| (sDt.getTime() <= startDt.getTime() && eDt.getTime() >= endDt
					.getTime())) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	public static boolean equals(Date date1, Date date2, String dateFormate) {
		if (format(date1, dateFormate).equals(format(date2, dateFormate))) {
			return true;
		}
		return false;
	}

	/**
	 * 相隔天数（date1 - date2）
	 * 
	 * @param date1
	 * @param date2
	 * @return 相隔天数
	 */
	public static int intervalDays(Date date1, Date date2) {
		int days = 0;
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long aDayTime = 24 * 60 * 60 * 1000L;
		days = (int) ((time1 - time2) / aDayTime);

		return days;
	}

	/**
	 * 相隔天数（date1 - date2） 不足一天的进一法取整(计算到分钟,秒钟不会进一)
	 * 
	 * @param date1
	 * @param date2
	 * @return 相隔天数
	 */
	public static int intervalDaysCeil(Date date1, Date date2) {
		int days = 0;
		long time1 = date1.getTime();
		long time2 = date2.getTime();

		long intervalMs = time1 - time2;
		int totalDays = (int) intervalMs / (1000 * 60 * 60 * 24);// 相差总共的天数
		int totalHours = (int) intervalMs / (1000 * 60 * 60);
		int totalMinutes = (int) intervalMs / (1000 * 60);

		int remainingHours = (totalHours - (totalDays * 24));
		int remainingMinutes = (totalMinutes - (totalDays * 24 * 60) - remainingHours * 60);
		if (remainingHours != 0 || remainingMinutes != 0) {
			days = totalDays + 1;
		} else {
			days = totalDays;
		}
		return days;
	}

	/**
	 * calculate interval days between two string dates （date1 - date2）
	 * 
	 * @param date1
	 * @param date2
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static int intervalDays(String date1, String date2, String pattern,
			Locale locale) {
		Date d1 = parseDate(date1, pattern, locale);
		Date d2 = parseDate(date2, pattern, locale);
		return intervalDays(d1, d2);
	}

	/**
	 * 相隔分钟数 （date1 - date2）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int intervalMinutes(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return 0;
		}
		int minutes = 0;
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long aMinuteTime = 60 * 1000;
		minutes = (int) ((time1 - time2) / aMinuteTime);
		return minutes;
	}

	/**
	 * calculate interval minutes between two string dates
	 * 
	 * @param value1
	 * @param value2
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static int intervalMinutes(String value1, String value2,
			String pattern, Locale locale) {
		Date date1 = parseDate(value1, pattern, locale);
		Date date2 = parseDate(value2, pattern, locale);
		return intervalMinutes(date1, date2);
	}

	/**
	 * 增加或减少天数
	 * 
	 * @param date
	 * @param month
	 *            为负数时，表示减少月； 为正数时，表示增加月
	 * 
	 */
	public static Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);

		return cal.getTime();
	}

	/**
	 * 增加或减少天数
	 * 
	 * @param date
	 * @param days
	 *            为负数时，表示减少天； 为正数时，表示增加天
	 */
	public static Date addDate(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);

		return cal.getTime();
	}

	/**
	 * 增加或减少小时
	 * 
	 * @param date
	 * @param hours
	 *            为负数时，表示减少小时数； 为正数时，表示增加小时数
	 * @return
	 */
	public static Date addHour(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hours);

		return cal.getTime();
	}

	/**
	 * 增加或减少分钟数
	 * 
	 * @param date
	 * @param minutes
	 *            为负数时，表示减少分钟； 为正数时，表示增加分钟
	 */
	public static Date addMinute(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);

		return cal.getTime();
	}
	/**
	 * 增加或减少毫秒数
	 * 
	 * @param date
	 * @param minutes
	 *            为负数时，表示减少分钟； 为正数时，表示增加分钟
	 */
	public static Date addMillisecond(Date date, int millsecnd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MILLISECOND, millsecnd);

		return cal.getTime();
	}
	/**
	 * @Desription:以format格式，格式化日期为字符串
	 * @param date 日期
	 * @param format 格式
	 * @return
	 * @Return:String
	 * @Author:ZCP
	 * @CreateDate:2015-3-14
	 */
	public static String format(Date date, String format) {
		String res = "";
		if (date == null || format==null) {
			return null;
		} else if (format.equals(YYYY_MM_DD)) {
			res = SimpleDateUtil.format(date, format);
		} else if (format.equals(YYYY_MM_DD_HH_MM)) {
			res = SimpleDateUtil.format(date, format);
		} else if (format.equals(YYYY_MM_DD_HH)) {
			res = SimpleDateUtil.format(date, format);
		} else if (format.equals(HH_MM)) {
			res = SimpleDateUtil.format(date, format);
		} else {
			//SimpleDateFormat dateformat = getFormat(format);//new SimpleDateFormat(format);
			SimpleDateFormat dateformat = new SimpleDateFormat(format);
			return dateformat.format(date);
		}
		return res;
	}
	
	
	/**
	 * @Desription:以format格式，格式化日期为字符串
	 * @param date 日期
	 * @param format 格式
	 * @return
	 * @throws ParseException 
	 * @Return:String
	 * @Author:ZCP
	 * @CreateDate:2015-3-14
	 */
	public static String formatDateString(String dateString, String format) throws ParseException {
		String res = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = sdf.parse(dateString);  
		if (date == null || format==null) {
			return null;
		} else if (format.equals(YYYY_MM_DD)) {
			res = SimpleDateUtil.format(date, format);
		} else if (format.equals(YYYY_MM_DD_HH_MM)) {
			res = SimpleDateUtil.format(date, format);
		} else if (format.equals(YYYY_MM_DD_HH)) {
			res = SimpleDateUtil.format(date, format);
		} else if (format.equals(HH_MM)) {
			res = SimpleDateUtil.format(date, format);
		} else {
			SimpleDateFormat dateformat = new SimpleDateFormat(format);
			return dateformat.format(date);
		}
		return res;
	}

	/**
	 * @param dateStr
	 *            yyyy-MM-dd
	 * @param timeStr
	 *            HH:mm
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String formatYMDHM(String dateStr, String timeStr) {
		String temp = dateStr + " " + timeStr;
		return temp.trim();
	}

	/**
	 * @Desription:以format格式，格式化日期为字符串,带本地化功能
	 * @param date
	 * @param format
	 * @param locale
	 * @return
	 * @Return:String
	 * @Author:ZCP
	 * @CreateDate:2015-3-14
	 */
	public static String format(Date date, String format, Locale locale) {
		//SimpleDateFormat dateformat = getFormat(format, locale);//new SimpleDateFormat(format, locale);
		SimpleDateFormat dateformat = new SimpleDateFormat(format, locale);
		return dateformat.format(date).toUpperCase();
	}

	/**
	 * 默认日期格式（"yyyy-MM-dd"）
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormate(Date date) {
		return format(date, DEFAULT_FORMAT_DATE);
	}

	/**
	 * 默认日期时间格式（"yyyy-MM-dd HH:mm:ss"）
	 * 
	 * @param date
	 * @return
	 */
	public static String dateTimeFormate(Date date) {
		return format(date, DEFAULT_FORMAT_TIMESTAMP);
	}

	/**
	 * 默认时间格式（"HH:mm:ss"）
	 * 
	 * @param date
	 * @return
	 */
	public static String timeFormate(Date date) {
		return format(date, DEFAULT_FORMAT_TIME);
	}

	/**
	 * parse date
	 * 
	 * @param date
	 * @param pattern
	 * @param locale
	 * @return
	 */
	@SuppressWarnings("finally")
	public static Date parseDate(String date, String pattern, Locale locale) {
		date = date.replaceAll("/", "-");
		pattern = pattern.replaceAll("/", "-");
		SimpleDateFormat format = null;
		if (null != locale) {
			//format = getFormat(pattern, locale);//new SimpleDateFormat(pattern, locale);
			format = new SimpleDateFormat(pattern, locale);
		} else {
			//format = getFormat(pattern);//new SimpleDateFormat(pattern);
			format = new SimpleDateFormat(pattern);
		}
		Date dt = null;
		try {
			dt = format.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException("Parse date error.[" + date + "]");
		} finally {
			return dt;
		}
	}

	/**
	 * parse date
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String date, String pattern) {
		return parseDate(date, pattern, null);
	}

	/**
	 * @Desription:转换yyyyMMdd..格式等
	 * @param date
	 * @param pattern
	 * @return
	 * @Return:Date
	 * @Author:ldb
	 * @CreateDate:2015-3-20
	 */
	public static Date parseDateByString(String date, String pattern) {
		//SimpleDateFormat simpleDateFormat = getFormat(pattern);//new SimpleDateFormat(pattern);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		// SimpleDateFormat的parse(String time)方法将String转换为Date
		Date newDate;
		try {
			newDate = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Parse date error.[" + date + "]");
		}
		return newDate;
	}

	/**
	 * 给COD 航班计划用 SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy/MM/dd HH:mm");
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String date) {
		if (null != date && date.trim().length() > 15) {
			date = date.trim().substring(0, 16);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e.getCause());
			}
		}
		return null;
	}

	/**
	 * 日期的HH:MM 修改为当前 HH:MM
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static Date parseDateMinute(Date date, String time) {
		String str = DateUtil.format((date), DateUtil.YYYY_MM_DD) + " " + time;
		return parseDate(str, DateUtil.YYYY_MM_DD_HH_MM);
	}

	/**
	 * add minutes to the given date
	 * 
	 * @param date
	 * @param pattern
	 * @param locale
	 * @param minutes
	 * @return
	 */
	public static String addMinute(String date, String pattern, Locale locale,
			int minutes) {
		//SimpleDateFormat format = getFormat(pattern, locale);//new SimpleDateFormat(pattern, locale);
		SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(format.parse(date));
			calendar.add(Calendar.MINUTE, minutes);
			return format.format(calendar.getTime());
		} catch (ParseException e) {
			throw new RuntimeException("Parse date error.[" + date + "]");
		}
	}

	/**
	 * transfer the en month to int month, if the targetMonth is unvalid, then
	 * renturn -1
	 * 
	 * @param targetMonth
	 * @return
	 */
	public static int getNumMonth(String targetMonth) {
		int month = -1;
		for (int i = 0; i < MM.length; i++) {
			if (MM[i].equalsIgnoreCase(targetMonth)) {
				month = i + 1;
				break;
			}
		}
		return month;
	}

	/**
	 * check whether the day is valid
	 * 
	 * @param year
	 * @param mouth
	 * @param day
	 * @return
	 */
	public static boolean isValidDay(int year, int mouth, int day) {
		if (day < 1) {
			return false;
		}
		switch (mouth) {
		case 0:
			return false;
		case 2:
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
				if (day > 29) {
					return false;
				}
			} else if (day > 28) {
				return false;
			}
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if (day > 30) {
				return false;
			}
			break;
		default:
			if (day > 31) {
				return false;
			}
			break;
		}
		return true;
	}

	/**
	 * 根据日期返回星期几
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if (day == 1) {
			day = 7;
		} else {
			day = day - 1;
		}
		return day;
	}
	
	/**
	 * 
	 * @Desription: 根据时间字符串返回星期
	 * @param dateStr
	 * @return
	 * @Return:int
	 * @Author:Administrator
	 * @CreateDate:2015-7-23
	 */
	public static int getDayByStr(String dateStr) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date localDate = null;
	    try {
	        localDate = sdf.parse(dateStr);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    
	    return DateUtil.getDay(localDate);
	}

	public static String getDayWord(Date date, int size) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		String word = WEEK[day - 1];
		return word.substring(0, size);
	}

	// 把小时分钟转换为分钟
	public static int hhmm2mins(int hhmm) {
		return (hhmm / 100 * 60 + hhmm % 100);
	}

	// override
	public static int hhmm2mins(String hhmmStr) {
		return hhmm2mins(Integer.valueOf(hhmmStr));
	}

	public static int hhmmstr2mins(String hhmmstr) {
		if (hhmmstr.substring(0, 1).equals("0")) {
			hhmmstr = hhmmstr.replace(":", "").substring(1, 4);
		} else {
			hhmmstr = hhmmstr.replace(":", "");
		}
		return hhmm2mins(Integer.valueOf(hhmmstr));
	}

	// 把分钟转小时
	public static String mins2hhmm(int mins, String separator) {
		String hh = Integer.toString(mins / 60);
		String mm = Integer.toString(mins % 60);
		if (hh.length() == 1) {
			hh = "0" + hh;
		}
		if (mm.length() == 1) {
			mm = "0" + mm;
		}
		return hh + separator + mm;
	}

	public static String mins2hhmm(int mins, String separator1,
			String separator2) {
		String hh = Integer.toString(mins / 60);
		String mm = Integer.toString(mins % 60);
		if (mm.length() == 1) {
			mm = "0" + mm;
		}
		return hh + separator1 + mm + separator2;
	}

	// 时间是否大于
	public static boolean isGreater(Date date1, Date date2) {
		if (date1 != null && date2 != null && (date1.after(date2))) {
			return true;
		}
		return false;
	}

	public static java.sql.Date toSqlDate(Date date) {
		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	public static Timestamp toTimestamp(Date date) {
		if (date == null) {
			return null;
		}
		return new Timestamp(date.getTime());
	}

	public static String addZero(int value) {
		String temp = value + "";
		if (value < 10) {
			temp = "0" + value;
		}
		return temp;
	}

	/**
	 * 判断时间 所属区间
	 * 
	 * @param date
	 * @param interval
	 * @return String YYYY_MM_DD_HH_MM
	 */
	public static String intervalDateTimeStr(Calendar cal) {
		String arr[] = intervalDateTimeArrStr(cal);
		return arr[1];
	}

	public static String intervalDateTimeStrStart(Calendar cal) {
		String arr[] = intervalDateTimeArrStr(cal);
		return arr[0];
	}

	public static String intervalDateTimeStrEnd(Calendar cal) {
		String arr[] = intervalDateTimeArrStrEnd(cal);
		return arr[0];
	}

	public static String[] intervalDateTimeArrStr(Calendar cal) {
		int minute = cal.get(Calendar.MINUTE);
		int interval = Constant.interval;
		int c = minute / interval;
		int m = minute % interval;
		String st = "", ed = "";
		if (m != 0) {
			cal.set(Calendar.MINUTE, c * interval);
			st = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
			cal.set(Calendar.MINUTE, (c + 1) * interval);
			ed = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
		} else {
			st = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
			cal.add(Calendar.MINUTE, +interval);
			ed = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
		}
		String arr[] = new String[2];
		arr[0] = st;
		arr[1] = ed;
		return arr;
	}

	public static String[] intervalDateTimeArrStrEnd(Calendar cal) {
		int minute = cal.get(Calendar.MINUTE);
		int interval = Constant.interval;
		int c = minute / interval;
		int m = minute % interval;
		String st = "", ed = "";
		if (m != 0) {
			cal.set(Calendar.MINUTE, c * interval);
			st = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
			cal.set(Calendar.MINUTE, (c + 1) * interval);
			ed = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
		} else {
			cal.add(Calendar.MINUTE, -interval);
			st = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
			cal.add(Calendar.MINUTE, interval);
			ed = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
		}
		String arr[] = new String[2];
		arr[0] = st;
		arr[1] = ed;
		return arr;
	}
	
	
	public static String intervalDateTimeStrStart(Calendar cal, int interval) {
		String arr[] = intervalDateTimeArrStr(cal,interval);
		return arr[0];
	}

	public static String intervalDateTimeStrEnd(Calendar cal, int interval) {
		String arr[] = intervalDateTimeArrStrEnd(cal,interval);
		return arr[0];
	}
	
	public static String[] intervalDateTimeArrStr(Calendar cal, int interval) {
		int minute = cal.get(Calendar.MINUTE);
		//int interval = Constant.interval;
		int c = minute / interval;
		int m = minute % interval;
		String st = "", ed = "";
		if (m != 0) {
			cal.set(Calendar.MINUTE, c * interval);
			st = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
			cal.set(Calendar.MINUTE, (c + 1) * interval);
			ed = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
		} else {
			st = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
			cal.add(Calendar.MINUTE, +interval);
			ed = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
		}
		String arr[] = new String[2];
		arr[0] = st;
		arr[1] = ed;
		return arr;
	}
	
	public static String[] intervalDateTimeArrStrEnd(Calendar cal, int interval) {
		int minute = cal.get(Calendar.MINUTE);
		//int interval = Constant.interval;
		int c = minute / interval;
		int m = minute % interval;
		String st = "", ed = "";
		if (m != 0) {
			cal.set(Calendar.MINUTE, c * interval);
			st = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
			cal.set(Calendar.MINUTE, (c + 1) * interval);
			ed = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
		} else {
			cal.add(Calendar.MINUTE, -interval);
			st = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
			cal.add(Calendar.MINUTE, interval);
			ed = DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD_HH_MM);
		}
		String arr[] = new String[2];
		arr[0] = st;
		arr[1] = ed;
		return arr;
	}

	/**
	 * 判断时间 所属区间
	 * 
	 * @param date
	 * @param interval
	 * @return Date YYYY_MM_DD_HH_MM
	 * @author liaozhiyong
	 */
	public static Date intervalDate(Calendar cal) {
		String intervalDateTime = intervalDateTimeStr(cal);
		return DateUtil.parseDate(intervalDateTime, DateUtil.YYYY_MM_DD_HH_MM);
	}

	public static Date intervalDateStart(Calendar cal) {
		String intervalDateTime = intervalDateTimeStrStart(cal);
		return DateUtil.parseDate(intervalDateTime, DateUtil.YYYY_MM_DD_HH_MM);
	}

	public static Date intervalDateEnd(Calendar cal) {
		String intervalDateTime = intervalDateTimeStrEnd(cal);
		return DateUtil.parseDate(intervalDateTime, DateUtil.YYYY_MM_DD_HH_MM);
	}
	
	public static Date intervalDateStart(Calendar cal,int interval) {
		String intervalDateTime = intervalDateTimeStrStart(cal, interval);
		return DateUtil.parseDate(intervalDateTime, DateUtil.YYYY_MM_DD_HH_MM);
	}

	public static Date intervalDateEnd(Calendar cal,int interval) {
		String intervalDateTime = intervalDateTimeStrEnd(cal, interval);
		return DateUtil.parseDate(intervalDateTime, DateUtil.YYYY_MM_DD_HH_MM);
	}

	/**
	 * 区间刻度 服务标准 <br/>
	 * 两个时间 进行区间分解
	 * 
	 * @param date
	 * @param interval
	 * @return YYYY_MM_DD_HH_MM
	 * @author liaozhiyong
	 */
	public static List<String> intervalDecompositionDate(Date effeDate,
			Date lostDate, String startTime, String finishTimeStr) {
		return decompositionDate(effeDate, lostDate, startTime, finishTimeStr);
	}

	/**
	 * 区间刻度 服务标准 <br/>
	 * 时间 进行区间分解
	 * 
	 * @param date
	 * @param interval
	 * @return YYYY_MM_DD_HH_MM
	 * @author liaozhiyong
	 */
	public static List<String> intervalDecompositionDate(Date effeDate,
			String startTimeStr, String finishTimeStr) {
		return decompositionDate(effeDate, null, startTimeStr, finishTimeStr);
	}

	/**
	 * 非区间刻度 <br/>
	 * 时间 进行区间分解
	 * 
	 * @param date
	 * @param date
	 * @return 区间集合 YYYY_MM_DD_HH_MM
	 * @author liaozhiyong
	 */
	public static List<String> fmtIntervalDecDate(Date date,
			String startTimeStr, String finishTimeStr) {
		Calendar cal = Calendar.getInstance();
		{
			Date date1 = DateUtil.parseDateMinute(date, startTimeStr);
			cal.setTime(date1);
			Date effeDate = DateUtil.intervalDate(cal);
			startTimeStr = DateUtil.format(effeDate, DateUtil.HH_MM);
		}
		{
			Date date2 = DateUtil.parseDateMinute(date, finishTimeStr);
			cal.setTime(date2);
			Date lostDate = DateUtil.intervalDate(cal);
			finishTimeStr = DateUtil.format(lostDate, DateUtil.HH_MM);
		}
		return DateUtil.intervalDecompositionDate(date, startTimeStr,
				finishTimeStr);
	}

	/**
	 * 非区间刻度 <br/>
	 * 时间 进行区间分解
	 * 
	 * @param date
	 * @param date
	 * @return 区间集合 YYYY_MM_DD_HH_MM
	 * @author liaozhiyong
	 */
	public static List<String> fmtIntervalDecDate(Date startDate, Date endDate,
			String startTimeStr, String finishTimeStr) {
		Calendar cal = Calendar.getInstance();
		Date date = startDate;
		{
			Date date1 = DateUtil.parseDateMinute(date, startTimeStr);
			cal.setTime(date1);
			Date effeDate = DateUtil.intervalDate(cal);
			startTimeStr = DateUtil.format(effeDate, DateUtil.HH_MM);
		}
		{
			Date date2 = DateUtil.parseDateMinute(date, finishTimeStr);
			cal.setTime(date2);
			Date lostDate = DateUtil.intervalDate(cal);
			finishTimeStr = DateUtil.format(lostDate, DateUtil.HH_MM);
		}
		return DateUtil.intervalDecompositionDate(startDate, endDate,
				startTimeStr, finishTimeStr);
	}

	public static String intervalStrTime(String startTimeStr) {
		Calendar cal = Calendar.getInstance();
		Date date = DateUtil.parseDate("2013-01-01", DateUtil.YYYY_MM_DD);
		{
			Date date1 = DateUtil.parseDateMinute(date, startTimeStr);
			cal.setTime(date1);
			Date effeDate = DateUtil.intervalDate(cal);
			startTimeStr = DateUtil.format(effeDate, DateUtil.HH_MM);
		}
		return startTimeStr;
	}

	/**
	 * 拆分时间段为 时间区间
	 * 
	 * @param startTimeStr
	 * @param finishTimeStr
	 * @return
	 */
	public static List<String> intervalStrTime(String startTimeStr,
			String finishTimeStr) {
		Calendar cal = Calendar.getInstance();
		List<String> listTimeStr = new ArrayList<String>();
		int interval = Constant.interval;
		{
			Date dateWork1 = DateUtil.parseDate(
					"2100-01-01 " + WORK_TIME_MIN.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			Date dateWork2 = DateUtil.parseDate(
					"2100-01-01 " + WORK_TIME_MAX.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			long ltimeWork1 = dateWork1.getTime();
			long ltimeWork2 = dateWork2.getTime();

			Date date1 = DateUtil.parseDate(
					"2100-01-01 " + startTimeStr.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			Date date2 = DateUtil.parseDate(
					"2100-01-01 " + finishTimeStr.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			{
				cal.setTime(date1);
				date1 = DateUtil.intervalDateStart(cal);
				cal.setTime(date2);
				date2 = DateUtil.intervalDateEnd(cal);
			}

			long ltime2 = date2.getTime();
			cal.setTime(date1);
			for (int i = 0;; i++) {
				cal.add(Calendar.MINUTE, interval * i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2 && ltime >= ltimeWork1
						&& ltime <= ltimeWork2) {
					listTimeStr.add(DateUtil.format((cal.getTime()),
							DateUtil.HH_MM));
				} else if (ltime > ltime2) {
					break;
				}
				cal.add(Calendar.MINUTE, -interval * i);
			}
		}
		return listTimeStr;
	}

	/**
	 * @Desription:拆分时间段为 时间区间(最大范围：0点到24点)
	 * @param startTimeStr
	 * @param finishTimeStr
	 * @return
	 * @Return:List<String>
	 * @Author:huanggm
	 * @CreateDate:2015-3-19
	 */
	public static List<String> intervalDayStrTime(String startTimeStr,
			String finishTimeStr) {
		Calendar cal = Calendar.getInstance();
		List<String> listTimeStr = new ArrayList<String>();
		int interval = Constant.interval;
		{
			Date dateWork1 = DateUtil.parseDate(
					"2100-01-01 " + TIME_MIN.trim(), DateUtil.YYYY_MM_DD_HH_MM);
			Date dateWork2 = DateUtil.parseDate(
					"2100-01-01 " + TIME_MAX.trim(), DateUtil.YYYY_MM_DD_HH_MM);
			long ltimeWork1 = dateWork1.getTime();
			long ltimeWork2 = dateWork2.getTime();

			Date date1 = DateUtil.parseDate(
					"2100-01-01 " + startTimeStr.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			Date date2 = DateUtil.parseDate(
					"2100-01-01 " + finishTimeStr.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			{
				cal.setTime(date1);
				date1 = DateUtil.intervalDateStart(cal);
				cal.setTime(date2);
				cal.add(Calendar.MINUTE,1);
				date2 = DateUtil.intervalDateEnd(cal);
			}

			long ltime2 = date2.getTime();
			cal.setTime(date1);
			for (int i = 0;; i++) {
				cal.add(Calendar.MINUTE, interval * i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2 && ltime >= ltimeWork1
						&& ltime <= ltimeWork2) {
					listTimeStr.add(DateUtil.format((cal.getTime()),
							DateUtil.HH_MM));
				} else if (ltime > ltime2) {
					break;
				}
				cal.add(Calendar.MINUTE, -interval * i);
			}
		}
		return listTimeStr;
	}

	/**
	 * 一天的工作日期
	 * 
	 * @param startTimeStr
	 *            15:45
	 * @param finishTimeStr
	 *            16:00
	 * @return
	 */
	public static List<String> intervalStrTimeStart2End(String startTimeStr,
			String finishTimeStr) {
		Calendar cal = Calendar.getInstance();
		List<String> listTimeStr = new ArrayList<String>();
		int interval = Constant.interval;
		{
			Date date1 = DateUtil.parseDate(
					"2100-01-01 " + startTimeStr.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			Date date2 = DateUtil.parseDate(
					"2100-01-01 " + finishTimeStr.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			{
				cal.setTime(date1);
				date1 = DateUtil.intervalDateStart(cal);
				cal.setTime(date2);
				date2 = DateUtil.intervalDateEnd(cal);
			}
			long ltime2 = date2.getTime();
			cal.setTime(date1);
			for (int i = 0;; i++) {
				cal.add(Calendar.MINUTE, interval * i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2) {
					listTimeStr.add(DateUtil.format((cal.getTime()),
							DateUtil.HH_MM));
				} else if (ltime > ltime2) {
					break;
				}
				cal.add(Calendar.MINUTE, -interval * i);
			}
		}
		return listTimeStr;
	}

	/**
	 * @Desription:将startTime至finishTime以Constant.interval为单位分段
	 * @param startTime
	 * @param finishTime
	 * @return
	 * @Return:Map<String,List<String>>
	 * @Author:ZCP
	 * @CreateDate:2015-3-14
	 */
	public static Map<String, List<String>> intervalStrTimeStart2End(
			Date startTime, Date finishTime) {
		Calendar cal = Calendar.getInstance();
		int interval = Constant.interval;
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		{
			Date date1 = null;
			Date date2 = null;
			{
				cal.setTime(startTime);
				date1 = DateUtil.intervalDateStart(cal);
				cal.setTime(finishTime);
				date2 = DateUtil.intervalDateEnd(cal);
			}
			long ltime2 = date2.getTime();
			cal.setTime(date1);
			for (int i = 0;; i++) {
				cal.add(Calendar.MINUTE, interval * i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2) {
					String key = DateUtil.format((cal.getTime()),
							DateUtil.YYYY_MM_DD);
					List<String> listTimeStr = result.get(key);
					if (listTimeStr == null) {
						listTimeStr = new ArrayList<String>();
						result.put(key, listTimeStr);
					}
					listTimeStr.add(DateUtil.format((cal.getTime()),
							DateUtil.HH_MM));
				} else if (ltime > ltime2) {
					break;
				}
				cal.add(Calendar.MINUTE, -interval * i);
			}
		}
		return result;
	}
	
	/**
	 * @Desription:中转科图表专用——将startTime至finishTime以Constant.interval为单位分段
	 * @param startTime
	 * @param finishTime
	 * @return
	 * @Return:Map<String,List<String>>
	 * @Author:ZCP
	 * @CreateDate:2015-3-14
	 */
	public static Map<String, List<String>> intervalStrTimeStart2EndForTransit(
			Date startTime, Date finishTime) {
		Calendar cal = Calendar.getInstance();
		int interval = Constant.interval;
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		{
			Date date1 = null;
			Date date2 = null;
			{
				cal.setTime(startTime);
				date1 = DateUtil.intervalDateStart(cal);
				cal.setTime(finishTime);
				
				if(finishTime.getTime() - startTime.getTime() <= interval*60*1000)
					//对于时间点任务，04:00-04:15 ，只需要划分成04:00这个点就行了
					date2 = DateUtil.intervalDateEnd(cal);
				else
					//对于非时间点任务10:00-11:00 ,划分成10:00,10:15,...,10:45,11:00(11:00这个也要算进去)
					date2 = DateUtil.intervalDateStart(cal);
			}
			long ltime2 = date2.getTime();
			cal.setTime(date1);
			for (int i = 0;; i++) {
				cal.add(Calendar.MINUTE, interval * i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2) {
					String key = DateUtil.format((cal.getTime()),
							DateUtil.YYYY_MM_DD);
					List<String> listTimeStr = result.get(key);
					if (listTimeStr == null) {
						listTimeStr = new ArrayList<String>();
						result.put(key, listTimeStr);
					}
					listTimeStr.add(DateUtil.format((cal.getTime()),
							DateUtil.HH_MM));
				} else if (ltime > ltime2) {
					break;
				}
				cal.add(Calendar.MINUTE, -interval * i);
			}
		}
		return result;
	}

	/**
	 * 将startTime，finishTime以interval为单位划分
	 * 
	 * @author zhang
	 * @since 2014-10-11
	 * @param startTime
	 * @param finishTime
	 * @param interval
	 * @return
	 */
	public static Map<String, List<String>> intervalStrTimeStart2End(
			Date startTime, Date finishTime, int interval) {
		Calendar cal = Calendar.getInstance();
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		{
			Date date1 = null;
			Date date2 = null;
			{
				cal.setTime(startTime);
				date1 = DateUtil.intervalDateStart(cal,interval);
				cal.setTime(finishTime);
				
				if(finishTime.getTime() - startTime.getTime() <= interval*60*1000)
					//对于15分钟刻度的时间点任务，04:00-04:15 ，只需要划分成04:00这个点就行了
					date2 = DateUtil.intervalDateEnd(cal,interval);
				else
					//对于非时间点任务10:00-11:00 ,划分成10:00,10:15,...,10:45,11:00(11:00这个也要算进去)
					date2 = DateUtil.intervalDateStart(cal,interval);
			}
			long ltime2 = date2.getTime();
			cal.setTime(date1);
			for (int i = 0;; i++) {
				cal.add(Calendar.MINUTE, interval * i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2) {
					String key = DateUtil.format((cal.getTime()),
							DateUtil.YYYY_MM_DD);
					List<String> listTimeStr = result.get(key);
					if (listTimeStr == null) {
						listTimeStr = new ArrayList<String>();
						result.put(key, listTimeStr);
					}
					listTimeStr.add(DateUtil.format((cal.getTime()),
							DateUtil.HH_MM));
				} else if (ltime > ltime2) {
					break;
				}
				cal.add(Calendar.MINUTE, -interval * i);
			}
		}
		return result;
	}

	/**
	 * 日期区间 分解 以天为单位
	 * 
	 * @return
	 * @author liaozhiyong
	 */
	public static List<String> spreadDateMinDateMax(Date dateMin, Date dateMax) {
		Calendar cal = Calendar.getInstance();
		List<String> listDate = new ArrayList<String>();
		Date date1, date2;
		{
			String str = DateUtil.format((dateMin), DateUtil.YYYY_MM_DD);
			date1 = DateUtil.parseDate(str, DateUtil.YYYY_MM_DD);
			String str2 = DateUtil.format((dateMax), DateUtil.YYYY_MM_DD);
			date2 = DateUtil.parseDate(str2, DateUtil.YYYY_MM_DD);
		}
		long ltime2 = date2.getTime();
		cal.setTime(date1);
		// 包括结束时间
		for (int i = 0;; i++) {
			cal.add(Calendar.DATE, i);
			long ltime = cal.getTimeInMillis();
			if (ltime <= ltime2) {
				listDate.add(DateUtil.format(cal.getTime(), DateUtil.YYYY_MM_DD));
				cal.add(Calendar.DATE, -i);
				continue;
			} else if (ltime > ltime2) {
				break;
			}
		}
		return listDate;
	}

	/**
	 * 区间刻度 的 两个时间 进行区间分解
	 * 
	 * @param date
	 * @param interval
	 * @return YYYY_MM_DD_HH_MM
	 * @author liaozhiyong
	 */
	private static List<String> decompositionDate(Date effeDate, Date lostDate,
			String startTimeStr, String finishTimeStr) {
		Calendar cal = Calendar.getInstance();
		List<String> listDateStr = new ArrayList<String>();
		if (lostDate != null) {
			Date date1, date2;
			{
				String str = DateUtil.format((effeDate), DateUtil.YYYY_MM_DD);
				date1 = DateUtil.parseDate(str, DateUtil.YYYY_MM_DD);
				String str2 = DateUtil.format((lostDate), DateUtil.YYYY_MM_DD);
				date2 = DateUtil.parseDate(str2, DateUtil.YYYY_MM_DD);
			}
			long ltime2 = date2.getTime();
			cal.setTime(date1);
			// 包括结束时间
			for (int i = 0;; i++) {
				cal.add(Calendar.DATE, i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2) {
					listDateStr.add(DateUtil.format((cal.getTime()),
							DateUtil.YYYY_MM_DD));
					cal.add(Calendar.DATE, -i);
					continue;
				} else if (ltime > ltime2) {
					break;
				}
			}
		} else {
			listDateStr.add(DateUtil.format(effeDate, DateUtil.YYYY_MM_DD));
		}
		List<String> listStr = new ArrayList<String>();
		List<String> listTimeStr = intervalStrTime(startTimeStr, finishTimeStr);
		for (String string : listDateStr) {
			for (String string2 : listTimeStr) {
				listStr.add(string + " " + string2);
			}
		}
		listDateStr.clear();
		listTimeStr.clear();
		return listStr;
	}

	/**
	 * 格式化为 (当天开始时间 <=) 2013-09-17 00:00
	 * 
	 * @param date
	 * @return date
	 */
	public static Date parseDateTimeMin(Date date) {
		String str = DateUtil.format((date), DateUtil.YYYY_MM_DD);
		return DateUtil.parseDate(str + DateUtil.TIME_MIN,
				DateUtil.YYYY_MM_DD_HH_MM);
	}

	/**
	 * @Desription:将日期字符串(yyyy-MM-dd)格式化为Date类型其中时间是最小的
	 * @param str
	 * @return
	 * @Return:Date
	 * @Author:ZCP
	 * @CreateDate:2015-3-14
	 */
	public static Date parseStrTimeMin(String str) {
		return DateUtil.parseDate(str + DateUtil.TIME_MIN,
				DateUtil.YYYY_MM_DD_HH_MM);
	}

	/**
	 * 格式化为 (< 当天结束时间 ) 2013-09-17 23:60
	 * 
	 * @param date
	 * @return date
	 */
	public static Date parseDateTimeMax(Date date) {
		String str = DateUtil.format((date), DateUtil.YYYY_MM_DD);
		return DateUtil.parseDate(str + DateUtil.TIME_MAX,
				DateUtil.YYYY_MM_DD_HH_MM);
	}

	/**
	 * @Desription:将日期字符串(yyyy-MM-dd)格式化为Date类型其中时间是最大的
	 * @param str
	 * @return
	 * @Return:Date
	 * @Author:ZCP
	 * @CreateDate:2015-3-14
	 */
	public static Date parseStrTimeMax(String str) {
		return DateUtil.parseDate(str + DateUtil.TIME_MAX,
				DateUtil.YYYY_MM_DD_HH_MM);
	}
	/**
	 * @Desription:将格式为：yyyy/MM/dd HH:mm日期字符串转换为date类型
	 * @param str
	 * @return
	 * @Return:Date
	 * @Author:ZCP
	 * @CreateDate:2015-4-18
	 */
	public static Date parseStrToDate(String str) {
		return DateUtil.parseDate(str,
				DateUtil.YYYYMMDD_HH_MM);
	}

	/**
	 * @Desription:将格式为：yyyy/MM日期字符串转换为date类型
	 * @param str
	 * @return
	 * @Return:Date
	 * @Author:ZCP
	 * @CreateDate:2015-4-18
	 */
	public static Date parseStrToDate2(String str) {
		return DateUtil.parseDate(str,
				DateUtil.YYYYMM);
	}
	/**
	 * 以interval为单位分解得到一天中所有的时间区间
	 * 
	 * @author zhang
	 * @since 2014-10-11
	 * @param interval
	 * @return
	 */
	public static List<String> dayAllInterval00To23(int interval) {
		Calendar cal = Calendar.getInstance();
		List<String> listTimeStr = new ArrayList<String>();
		{
			Date date1 = DateUtil.parseDate("2100-01-01 00:00",
					DateUtil.YYYY_MM_DD_HH_MM);
			Date date2 = DateUtil.parseDate("2100-01-01 23:59",
					DateUtil.YYYY_MM_DD_HH_MM);
			long ltime2 = date2.getTime();
			cal.setTime(date1);
			for (int i = 0;; i++) {
				cal.add(Calendar.MINUTE, interval * i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2) {
					listTimeStr.add(DateUtil.format((cal.getTime()),
							DateUtil.HH_MM));
					cal.add(Calendar.MINUTE, -interval * i);
					continue;
				} else if (ltime > ltime2) {
					break;
				}
			}
		}
		return listTimeStr;
	}
	
	/**
	 * 分解得到当天04:00到第二天04:00中所有的时间区间
	 * 
	 * @return
	 * @author linjiajie
	 */
	public static List<String> dayAllInterval04To04(int interval) {
		Calendar cal = Calendar.getInstance();
//		int interval = Constant.interval;
		List<String> listTimeStr = new ArrayList<String>();
		{
			Date date1 = DateUtil.parseDate(
					"2100-01-01 " + WORK_TIME_MIN_FOUR.trim(),
					DateUtil.YYYY_MM_DD_HH_MM);
			Date date2 = DateUtil.parseDate(
					"2100-01-02 " + "03:59",
					DateUtil.YYYY_MM_DD_HH_MM);
			long ltime2 = date2.getTime();
			cal.setTime(date1);
			for (int i = 0;; i++) {
				cal.add(Calendar.MINUTE, interval * i);
				long ltime = cal.getTimeInMillis();
				if (ltime <= ltime2) {
					listTimeStr.add(DateUtil.format((cal.getTime()),
							DateUtil.HH_MM));
					cal.add(Calendar.MINUTE, -interval * i);
					continue;
				} else if (ltime > ltime2) {
					break;
				}
			}
		}
		return listTimeStr;
	}

	


	/**
	 * 时间：分钟 转成 数字
	 * 
	 * @param time
	 * @return
	 * @author liaozhiyong
	 */
	public static int time2int(String time) {
		time = DEF_YYYY_MM_DD + " " + time.trim();
		Date dateTemp = DateUtil.parseDate(time, DateUtil.YYYY_MM_DD_HH_MM);
		long lon1 = defDate.getTime();
		long lon2 = dateTemp.getTime();
		return (int) (lon2 - lon1) / millisecond;
	}

	/**
	 * 数字转成 时钟
	 * 
	 * @param time
	 * @return
	 * @author liaozhiyong
	 */
	public static Date int2time(int time) {
		long ltime = time < 0 ? 0L : time * 1L;
		long lon1 = defDate.getTime() + ltime * millisecond;
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(lon1);
		return c2.getTime();
	}

	/**
	 * 日期 添加 时间
	 * 
	 * @param dateStr
	 * @param time
	 * @return
	 * @author liaozhiyong
	 */
	public static Date int2time(String dateStr, int time) {
		dateStr = dateStr.trim() + " 00:00";
		Date date = DateUtil.parseDate(dateStr, DateUtil.YYYY_MM_DD_HH_MM);
		long ltime = time < 0 ? 0L : time * 1L;
		long lon1 = date.getTime() + ltime * millisecond;
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(lon1);
		return c2.getTime();
	}

	/**
	 * 日期是否是这个星期 ？是-返回日期，否-返回：null
	 * 
	 * @param date
	 * @param weeks
	 * @return
	 */
	public static Date isDateContentEqualsWeeks(Date date, Set<String> weeks) {
		DateFormat format = new SimpleDateFormat("E", Locale.CHINA);
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
		if (weeks.contains(format.format(calendar.getTime()).trim())) {
			return date;
		}
		return null;
	}

	/**
	 * 数据库 时间类型 转成 字符
	 * 
	 * @param name
	 * @param format
	 * @return
	 * @author liaozhiyong
	 */
	public static String dbformat(String name, String format) {
		return " TO_CHAR(" + name + ",'" + format + "') ";
	}

	/**
	 * 把date2的时间 覆盖 date1的时间，date1日期不变
	 */
	public static Date addHourAndMinute(Date date1, Date date2) {
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		c1.add(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
		c1.add(Calendar.MINUTE, c2.get(Calendar.MINUTE));
		return c1.getTime();
	}

	/**
	 * 根据给定的Date，得到精确到day的Date
	 */
	public static Date getDateForDay(Date date) {
	    Calendar c = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();		
		c.setTime(date);
		c1.clear();
		c1.set(Calendar.YEAR, c.get(Calendar.YEAR));
		c1.set(Calendar.MONTH, c.get(Calendar.MONTH));
		c1.set(Calendar.DATE, c.get(Calendar.DATE));
		return c1.getTime();
	}

	/**
	 * 
	 * 
	 */
	/**
	 * 根据给定的Date、time，组合成Date
	 * 
	 * @author 田红兵
	 * @since 2014-7-23
	 * @param date
	 *            2014-07-15
	 * @param time
	 *            如：5->00:05、15->00:15、830->08:30、1415->14:15
	 * @return 2014-07-15 08:30、2014-07-15 14:15
	 */
	public static Date getDateForTime(Date date, String time) {
		if (null != time && time.length() > 0 && time.length() < 5) {
			int len = time.length();
			int hour = 0;
			int minute = 0;
			if (len < 3) {
				minute = Integer.parseInt(time);
			} else {
				String hourStr = time.substring(0, len / 2);
				String minuteStr = time.substring(len / 2, len);
				hour = Integer.parseInt(hourStr);
				minute = Integer.parseInt(minuteStr);
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			Calendar c1 = Calendar.getInstance();
			c1.clear();
			c1.set(Calendar.YEAR, c.get(Calendar.YEAR));
			c1.set(Calendar.MONTH, c.get(Calendar.MONTH));
			c1.set(Calendar.DATE, c.get(Calendar.DATE));
			c1.set(Calendar.HOUR, hour);
			c1.set(Calendar.MINUTE, minute);
			c1.set(Calendar.SECOND, 0);
			return c1.getTime();
		}
		return null;
	}

	/**
	 * 判断字符串日期，格式yyyy-MM-dd是否是后天
	 * 
	 * @author zhang
	 * @since 2014-11-20
	 * @param dateStr
	 * @return
	 */
	public static boolean isDayAfterTomorrow(String dateStr) {
		DateTime dt = new DateTime(dateStr);
		DateTime now = DateTime.now(TimeZone.getDefault());
		// 日期是后天及以后
		return now.numDaysFrom(dt) >= 2;
	}
	/**
	 * @Desription:判断字符串日期，格式yyyy-MM-dd是否明天或者以后
	 * @param dateStr
	 * @Return:boolean
	 * @Author:ZCP
	 * @CreateDate:2015-5-7
	 */
	public static boolean isFuture(String dateStr) {
		DateTime dt = new DateTime(dateStr);
		DateTime now = DateTime.now(TimeZone.getDefault());
		// 日期是明天或者以后
		return now.numDaysFrom(dt) >= 1;
	}

	/**
	 * 传入周几，得到今天以后周几对应的日期。
	 * 
	 * @param dayOfweek
	 *            参数指定周几（如周一）
	 * @throws Exception
	 */
	public static Date getWeekDate(final int dayOfweek){
		Date d = null;
		DateTime now = DateTime.now(TimeZone.getDefault());
		int month = now.getMonth();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int day = 1;

		DateTime dt = null;
		do {
			dt = new DateTime(sdf.format(cal.getTime()));
			if (now.numDaysFrom(dt) >= 1
					&& cal.get(Calendar.DAY_OF_WEEK) == dayOfweek) {
				d = cal.getTime();
				break;
			}
			day++;
			cal.set(Calendar.DAY_OF_MONTH, day);
		} while (cal.get(Calendar.MONTH) + 1 == month);
		// 如果找不到，找下一个月
		if (d == null) {
			cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			day = 1;
			do {
				dt = new DateTime(sdf.format(cal.getTime()));
				if (now.numDaysFrom(dt) >= 1
						&& cal.get(Calendar.DAY_OF_WEEK) == dayOfweek) {
					d = cal.getTime();
					break;
				}
				day++;
				cal.set(Calendar.DAY_OF_MONTH, day);
			} while (true);
		}
		return d;
	}
	/**
	 * @Desription:判断时间格式 格式必须为format
	 * @param str
	 * @Return:boolean
	 * @Author:ZCP
	 * @CreateDate:2015-4-17
	 */
    public static boolean isValidDate(final String str,final String format) {  
        //DateFormat formatter = getFormat(format);//new SimpleDateFormat(format);  
    	DateFormat formatter = new SimpleDateFormat(format);  
        try{  
            Date date = (Date)formatter.parse(str);  
            return str.equals(formatter.format(date));  
        }catch(Exception e){  
            return false;  
        }  
    } 
    
    /**
     * 
     * @Desription: 根据给定的开始日期、结束日期获取区间集合
     * @param strDt 开始日期
     * @param endDt 结束日期
     * @return
     * @Return:List<Date>
     * @Author: 田红兵
     * @CreateDate:2015-4-20
     */
    public static List<Date> getDateList(Date strDt,Date endDt){
        List<Date> result = new ArrayList<Date>();
        if(!endDt.after(strDt)){
            return result;
        }
        Date tmp = getDateForDay(strDt);
        result.add(tmp);
        while(endDt.after(tmp)){
            tmp = addDate(tmp, 1);
            result.add(tmp);
        }
        return result;
    }
    
    /**
     * 
     * @Desription: 根据给定的开始日期、结束日期获取区间集合
     * @param strDt 开始日期
     * @param endDt 结束日期
     * @return
     * @Return:List<Date>
     * @Author: 田红兵
     * @CreateDate:2015-4-20
     */
    public static List<Date> getDateListAbs(Date strDt,Date endDt){
        List<Date> result = new ArrayList<Date>();
        if(!endDt.after(strDt)){
            return result;
        }
//        Date tmp = addHour(strDt, 24);
//        Date tmp = getDateForDay(strDt);
        Date tmp = (Date) strDt.clone();
        result.add(tmp);
        while(endDt.after(tmp)){
            tmp = addDate(tmp, 1);
            result.add(tmp);
        }
        return result;
    }
    /**
     * @Desription:将毫秒数转换为方便阅读的时间格式
     * @param mss 要转换的毫秒数 
     * @Return:String 该毫秒数转换为 * 天 * 小时 * 分钟 * 秒 后的格式 
     * @Author:ZCP
     * @CreateDate:2015-4-21
     */
    public final static String formatDuring(long mss) {  
	    long days = mss / (1000 * 60 * 60 * 24);  
	    long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);  
	    long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);  
	    long seconds = (mss % (1000 * 60)) / 1000;  
	    StringBuilder sb=new StringBuilder();
	    if(days>0){
	    	sb.append(days).append("天");
	    }
	    if(hours>0){
	    	sb.append(hours).append("小时");
	    }
	    
	    if(minutes>0){
	    	sb.append(minutes).append("分钟");
	    }
	    if(seconds>0){
	    	sb.append(seconds).append("秒");
	    }
	    return sb.toString();  
	}
    
    /**
     * @Desription:针对国内服务科-国内服务其他任务，分析某日期是否在国内服务类服务标准的周期里
     * @param dateStr 要判断的日期（例：2015-07-23）；  cycleStr 周期（例：1111100）
     * @Return: true or false 
     * @Author: dmao
     * @CreateDate:2015-07-23
     */
    public static boolean isInWeekCycle(String dateStr,String cycleStr){
    	DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
    	Date date = null;
		try {
			date = df.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
	    	calendar.setTime(date);
			int dayNum = calendar.get(Calendar.DAY_OF_WEEK);	//获取该日期是星期几， 1、2、3、4、5、6、7 分别代表星期日、星期一、星期二、星期三、星期四、星期五、星期六
			int len = cycleStr.length();
			int dayArr[] = new int [len];
			for(int i=0;i<len;i++){
				if(i==len-1)
					dayArr[i] = Integer.parseInt(cycleStr.substring(i));
				else
					dayArr[i] = Integer.parseInt(cycleStr.substring(i, i+1));
			}

			int transNum = 0;
			if(dayNum==1)
				transNum = 7;	//将Calendar返回的星期几，转化为1~7对应的星期一~星期日
			else
				transNum = dayNum - 1;
			
			if(dayArr[transNum-1]==1)	//等于1代表该日期在标准周期内
				return true;
			else
				return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    /*private static SimpleDateFormat getFormat(String pattern) {  
    	SimpleDateFormat sdf = threadLocal.get();  
    	if (sdf == null) {  
    		sdf = new SimpleDateFormat(pattern);  
    		threadLocal.set(sdf);  
    	}  
    	return sdf;  
    }
    private static SimpleDateFormat getFormat(String pattern, Locale locale) {  
    	SimpleDateFormat sdf = threadLocal.get();  
    	if (sdf == null) {  
    		sdf = new SimpleDateFormat(pattern, locale);  
    		threadLocal.set(sdf);  
    	}  
    	return sdf;  
    }*/
/**
 *  获取每月最后一天
 * @param sDate1
 * @return
 */
	public static Date getLastDayOfMonth(Date sDate1) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(sDate1);
		final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = cDay1.getTime();
		lastDate.setDate(lastDay);
		return lastDate;
	}
	
	/**
	 *  获取年度最后一天
	 * @param sDate1
	 * @return
	 */
	public static Date getLastDayOfYear(Date sDate1) {
			Calendar cDay1 = Calendar.getInstance();
			cDay1.setTime(sDate1);
			cDay1.add(Calendar.YEAR, 1);
		    cDay1.set(Calendar.DAY_OF_YEAR, 0);
		    //将小时至0
			cDay1.set(Calendar.HOUR_OF_DAY, 23);
			//将分钟至0
			cDay1.set(Calendar.MINUTE, 59);
			//将秒至0
			cDay1.set(Calendar.SECOND,59);
			return cDay1.getTime();
	}
	
	/**
	 *  获取年度第一天
	 * @param sDate1
	 * @return
	 */
	public static Date getFirstDayOfYear(Date sDate1) {
			Calendar cDay1 = Calendar.getInstance();
			cDay1.set(Calendar.DAY_OF_YEAR, 1);
			//将小时至0
			cDay1.set(Calendar.HOUR_OF_DAY, 0);
			//将分钟至0
			cDay1.set(Calendar.MINUTE, 0);
			//将秒至0
			cDay1.set(Calendar.SECOND,0);
			return cDay1.getTime();
	}
	
	/**
	 *  获取年度第一天
	 * @param sDate1
	 * @return
	 */
	public static Date getFirstDayOfYear2(Date sDate1) {
			Calendar cDay1 = Calendar.getInstance();
			cDay1.set(Calendar.DAY_OF_YEAR, 1);
			//将小时至0
			cDay1.set(Calendar.HOUR_OF_DAY, -1);
			//将分钟至0
			cDay1.set(Calendar.MINUTE, 0);
			//将秒至0
			cDay1.set(Calendar.SECOND,0);
			return cDay1.getTime();
	}
	
	/**
	 * 获取当前时间过去一年的日期
	 * @param date
	 * @return
	 */
	public static Date getPreviousOneYear(Date date) {
		Calendar cyear = Calendar.getInstance();
		//过去一年
		cyear.setTime(new Date());
		cyear.add(Calendar.YEAR, -1);
        Date year = cyear.getTime();
		return year;
	}

	
	/**
	 * 获取上个月份
	 * 
	 * @param sDate1
	 * @return
	 */
	public static String getPreviousMonth(Date date) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		cDay1.add(Calendar.MONTH, -1);
		Integer month = cDay1.get(Calendar.MONTH) + 1;
		String strMonth = null;
		if (month < 10) {
			strMonth = "0" + month;
		} else {
			strMonth = month.toString();
		}
		return strMonth;
	}

	/**
	 * 获取下个月份
	 * 
	 * @param sDate1
	 * @return
	 */
	public static String getNextMonth(Date date) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		cDay1.add(Calendar.MONTH, 1);
		Integer month = cDay1.get(Calendar.MONTH) + 1;
		String strMonth = null;
		if (month < 10) {
			strMonth = "0" + month;
		} else {
			strMonth = month.toString();
		}
		return strMonth;
	}
	
	/**
	 * 获取上个年份
	 * 
	 * @param sDate1
	 * @return
	 */
	public static String getPreviousYear(Date date) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		cDay1.add(Calendar.YEAR, -1);
		Integer year = cDay1.get(Calendar.YEAR);
		return String.valueOf(year);
	}
	/**
	 * 获取下个年份
	 * 
	 * @param sDate1
	 * @return
	 */
	public static String getNextYear(Date date) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		cDay1.add(Calendar.YEAR, 1);
		Integer year = cDay1.get(Calendar.YEAR);
		return String.valueOf(year);
	}
	
	
/**
 * 获取当月第一天
 * @param sDate1
 * @return
 */
	public static Date getFirstDayOfMonth() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// 获取当月的第一天
			Calendar cal_1 = Calendar.getInstance();// 获取当前日期
			cal_1.add(Calendar.MONTH, 0);
			cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			String firstDay = format.format(cal_1.getTime());
			return format.parse(firstDay);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取当月第一天
	 * @param sDate1
	 * @return
	 */
		public static Date getFirstDayOfMonth2(Date sDate1) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				// 获取当月的第一天
				Calendar cal_1 = Calendar.getInstance();// 获取当前日期
				cal_1.setTime(sDate1);
				cal_1.add(Calendar.MONTH, 0);
				cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				String firstDay = format.format(cal_1.getTime());
				return format.parse(firstDay);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	/**
	 * 获取系统时间年份
	 * 
	 * @param sDate1
	 * @return lomg
	 */
	public static Long getSystemYear() {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY);
		Date date = new Date();
		Long formatDate = Long.valueOf(sdf.format(date));
		return formatDate;
	}
	/**
	 * 获取系统时间年份
	 * 
	 * @param sDate1
	 * @return String
	 */
	public static String getSystemYearString() {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY);
		Date date = new Date();
		String formatDate = sdf.format(date);
		return formatDate;
	}
	
	/**
	 * 获取系统时间年份
	 * 
	 * @param sDate1
	 * @return String
	 */
	public static String getFormateDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE1);
		Date date = new Date();
		String formatDate = sdf.format(date);
		return formatDate;
	}
	
	/**
	 * 比较开始日期与结束日期相差天数不能超过999天
	 * 
	 * @param sDate1
	 * @return String
	 */
	public static boolean DateDiff(String dateStr, String dateEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date dateMin;
		try {
			List<String> dateList = new ArrayList<String>();
			dateMin = sdf.parse(dateStr);
			Date dateMax = sdf.parse(dateEnd);
			dateList = DateUtil.spreadDateMinDateMax(dateMin, dateMax);
			int day = DateUtil.intervalDays(dateMax, dateMin);
			if (day > 999) {
				logger.info("请不要非法查询,开始日期与结束日期相差天数不能超过999天!");
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;

	}
	
	/**
	 * 比较开始日期不能大于结束日期。
	 * 
	 * @param sDate1
	 * @return String
	 */
	public static boolean dateComparison(String dateStr, String dateEnd) {
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date dateMin = sdf.parse(dateStr);
			Date dateMax = sdf.parse(dateEnd);
			if (dateMin.getTime() > dateMax.getTime()) {
				logger.info("开始日期不能大于结束日期!");
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	
	/**
	 *  日期减天数得到日期
	 * @param sDate1
	 * @return String
	 */
	public static Date dateSubtraction(Date dateStr, int day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endDate  = new Date();
		try {
			Calendar date = Calendar.getInstance();
			date.setTime(dateStr);
			date.set(Calendar.DATE, date.get(Calendar.DATE) - day);
		    endDate = sdf.parse(sdf.format(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return endDate;
	}
	
	/**
	 *  data类型时间修改
	 * @param sDate1
	 * @return String
	 */
	public static Date  modifyDateTime(Date dateStr) {
        String time = "17:00:00";
		Date dateTime3 = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String DateStr = dateFormat.format(dateStr);
		String DateStr1 =  DateStr.substring(0, 11);
		String DateStr2 =  DateStr1+time;
		try {
			 dateTime3 = dateFormat.parse(DateStr2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateTime3;

	
	}
	
	/**
	 * date1 是否小于 date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isLess(Date date1, Date date2) {
		if (date1 != null && date2 != null
				&& (date1.before(date2))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 功能描述： 将数字的月份转成英文缩写的月份
	 * @author wuxl
	 * @date   2017-11-14
	 * @param targetMonth
	 * @return
	 */
	public static String getEnMonth(String targetMonth) {
		int index = Integer.parseInt(targetMonth);
		String month = MM[index - 1];
		return month;
	}


	
	/**
	 * 功能描述： 计算两个时间相差的毫秒数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long intervalMillisecond(Date date1, Date date2){
		if (date1 == null || date2 == null) {
			return 0L;
		}
		long millisecond = 0L;
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		millisecond = time1 - time2;
		return millisecond;
	}
	
	/**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static Integer getDaySub(String beginDateStr,String endDateStr)
    {
        Integer day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");    
        java.util.Date beginDate;
        java.util.Date endDate;
        try
        {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);    
            day=(int) ((endDate.getTime()-beginDate.getTime())/(24*60*60*1000));    
            //System.out.println("相隔的天数="+day);   
        } catch (ParseException e)
        {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }   
        return day;
    }
    
	/**
     * date2比date1多的天数
     * @param date1    
     * @param date2
     * @return    
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
       int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            
            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            return day2-day1;
        }
    }
    
	/**
	 * 获取最小日期和最大日期
	 */
	public static Map<String,Date> getDateByVacationDt(String vacationDt){
		Map<String,Date> map = new HashMap<String, Date>();
		if(StringUtils.isNotEmpty(vacationDt)){
			Date maxDate=null;
			Date minDate=null;
			String[] arr=vacationDt.split(",");
			if(arr!=null && arr.length>0){
				for(int i=0;i<arr.length;i++){
					if(StringUtils.isNotEmpty(arr[i])){ //避免传过来的字符串中有以逗号隔开的空字符串
						Date date=DateUtil.parseDate(arr[i], DateUtil.YYYYMMDD);
						maxDate=date;
						minDate=date;
						break;
					}
				}
			}

			if(arr!=null && arr.length>0){
				for(int i=0;i<arr.length;i++){
					if(StringUtils.isNotEmpty(arr[i])){
						Date date=DateUtil.parseDate(arr[i], DateUtil.YYYYMMDD);
						if(date.getTime()>maxDate.getTime()){
							maxDate=date;
						}
						if(date.getTime()<minDate.getTime()){
							minDate=date;
						}
					}
				}
			}
			if(maxDate!=null && minDate!=null){
				Date strDate = DateUtil.parseDate(DateUtil.format(minDate,DateUtil.YYYY_MM_DD) + " " + "09:00:00",DateUtil.YYYY_MM_DD_HH_MM);
				Date endDate = DateUtil.parseDate(DateUtil.format(maxDate,DateUtil.YYYY_MM_DD) + " " + "17:00:00",DateUtil.YYYY_MM_DD_HH_MM);
				map.put("max", endDate);
				map.put("min", strDate);
				return map;
			}
		}
		return map;
		
	}
	
	public static List<String> getMonthBetween(String minDate, String maxDate) {
	    ArrayList<String> result = new ArrayList<String>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

	    Calendar min = Calendar.getInstance();
	    Calendar max = Calendar.getInstance();

	    try {
			min.setTime(sdf.parse(minDate));
		    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
		    max.setTime(sdf.parse(maxDate));
		    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    Calendar curr = min;
	    while (curr.before(max)) {
	     result.add(sdf.format(curr.getTime()));
	     curr.add(Calendar.MONTH, 1);
	    }

	    return result;
	  }
	
	public static void main(String[] args) {
		String str="2019-05";
		String str2="2019/05";
		Date d=parseStrToDate2(str);
		Date d2=parseStrToDate2(str2);
		System.out.println(d);
		System.out.println(d.getTime()==d2.getTime());
		System.out.println(d==d2);
	}
}