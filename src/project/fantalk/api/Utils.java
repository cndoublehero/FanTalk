package project.fantalk.api;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public final class Utils {
	public static final String SPLIT_STR = ":::";
	
	public static String[] process(String str) {
		if (str == null)
			throw new RuntimeException("传递字符串错误");
		String[] ss = str.split(SPLIT_STR);
		if (ss == null || ss.length != 3)
			throw new RuntimeException("传递的字符串不符合要求");
		return ss;
	}
    
    /**
     * @param s 原始消息字符串
     * @return 自动截断超过140个字符的消息，取前面133个字符，并添加...，预留转发的字符位置
     */
    public static String cut(String s) {
        String str = s.trim();
        StringBuilder sb = new StringBuilder();
        if (str.length() > 140) {
            return sb.append(str.substring(0, 135)).append("...").toString();
        } else {
            return str;
        }
    }
    
    /**
     * @param s 原始消息字符串
     * @return 是否超过字数限制
     */
    public static boolean isOverLimit(String s){
    	return s.length()>139;
    }

    /**
     * @param s 原始字符串
     * @return 判断字符串是否为空
     */
    public static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    /**
     * @param s 原始字符串
     * @return 转义后的字符串
     */
    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }

    /** 以秒为单位计算时间间隔 */
    private static final long MIN = 60;
    private static final long HOUR = MIN * 60;
    private static final long DAY = HOUR * 24;
    private static final long YEAR = DAY * 1000;

    /**
     * 返回指定时间与当前时间的间隔
     * @param date 指定的日期
     * @return 返回字符串类型的时间间隔
     */
    public static String getInterval(Date date) {
        long now = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
                .getTimeInMillis();
        long seconds = (now - date.getTime()) / 1000;
        if (seconds < MIN) {
            return seconds + " secs ago";
        } else if (seconds < HOUR * 2) {
            return seconds / MIN + " mins ago";
        } else if (seconds < DAY * 2) {
            return seconds / HOUR + " hours ago";
        } else if (seconds < YEAR * 2) {
            return seconds / DAY + " days ago";
        } else {
            return seconds / YEAR + " years ago";
        }
    }

    /**
     * 返回指定时间与当前时间的间隔，单位为秒
     * @param date 指定日期
     * @return 返回时间间隔，单位为秒
     */
    public static long interval(Date date) {
        return (Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
                .getTimeInMillis() - date.getTime()) / 1000;
    }

    /**
     * 获取HTTP请求的Base URL
     * @param request HTTP请求
     * @return 返回Base URL
     */
    public static String getBaseURL(HttpServletRequest request) {
        if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
            return request.getScheme() + "://" + request.getServerName()
                    + request.getContextPath();
        else
            return request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath();
    }

    /**
     * 限制操作时间为10秒一次
     * @param date 上次操作日期
     * @return 是否可以继续操作
     */
    public static boolean canDo(Date date) {
        return true;
    }

    /**
     * 字符串转化为数字
     * @param s 字符串参数
     * @return 字符串代表的数字，如果无法转换，返回0
     */
    public static int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public static String[] parseToken(String response) {
		String[] arrays = null;
		if (response == null || response.equals("")) {
			return arrays;
		}

		String[] tokenArray = response.split("&");

		if (tokenArray.length < 2) {
			return arrays;
		}

		String strTokenKey = tokenArray[0];
		String strTokenSecrect = tokenArray[1];

		String[] token1 = strTokenKey.split("=");
		if (token1.length < 2) {
			return arrays;
		}
		String tokenKey = token1[1];

		String[] token2 = strTokenSecrect.split("=");
		if (token2.length < 2) {
			return arrays;
		}
		String tokenSecrect = token2[1];

		return new String[] { tokenKey, tokenSecrect };
	}
    
    /**
     * 返回消息中的source来源信息
     * @param source 
     * @return 
     */
	public static String getSource(String source) {
		Pattern p = Pattern.compile("<a.*href=\".*\".*target=\".*\">(.+?)</a>");
		Matcher m = p.matcher(source);
		if (m.matches()) {
			return m.group(1);
		} else {
			return source;
		}
	}
    
    public static String getAppID(){
        return System.getProperty("appid");
    }
    
    public static Date getPreDate(Date date, int before) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1 * before);
		return calendar.getTime();
	}
}
