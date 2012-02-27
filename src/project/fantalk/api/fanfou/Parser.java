package project.fantalk.api.fanfou;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.Utils;

/**
 * @author mcxiaoke JSON数据解析工具类
 */
public final class Parser {

    /**
     * @param data
     * @return 解析User
     */
    public static User parseUser(String data) {
        return User.parse(data);
    }

    /**
     * @param data
     * @return 解析Status
     */
    public static Status parseStatus(String data) {
        return Status.parse(data);
    }

    /**
     * @param data
     * @return 解析Message
     */
    public static Message parseMessage(String data) {
        return Message.parse(data);
    }

    /**
     * @param data
     * @return 解析私信收件箱
     */
    public static List<Message> parseDirectMessages(String data) {
        return parseMessages(data);
    }

    /**
     * @param data
     * @return 解析私信发件箱
     */
    public static List<Message> parseDirectMessagesSent(String data) {
        return parseMessages(data);
    }

    /**
     * @param data
     * @return 解析Friends
     */
    public static List<String> parseFriends(String data) {
        return parseIDs(data);
    }

    /**
     * @param data
     * @return 解析Followers
     */
    public static List<String> parsefollowers(String data) {
        return parseIDs(data);
    }

    /**
     * @param data
     * @return 解析收藏列表
     */
    public static List<Status> parseFavorites(String data) {
        return parseStatuses(data);
    }

    public static List<Status> parsePublicSearch(String data) {
        return parseStatuses(data);
    }

    /**
     * @param data
     * @return 解析时间线
     */
    public static List<Status> parseTimeline(String data) {
        return parseStatuses(data);
    }

    /**
     * @param data
     * @return 解析消息列表
     */
    public static List<Status> parseStatuses(String data) {
        List<Status> statuses = new ArrayList<Status>();
        try {
            JSONArray a = new JSONArray(data);
//            if(a==null){
//            	return null;
//            }
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                Status status = Status.parse(o);
                statuses.add(status);
            }
//            return statuses;
        } catch (JSONException e) {
//            return null;
        }
        return statuses;
    }

    /**
     * @param data
     * @return 解析私信列表
     */
    public static List<Message> parseMessages(String data) {
        List<Message> messages = new ArrayList<Message>();
        try {
            JSONArray a = new JSONArray(data);
//            if(a==null){
//            	return null;
//            }
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                Message message = Message.parse(o);
                messages.add(message);
            }
//            return messages;
        } catch (JSONException e) {
//            return null;
        }
        return messages;
    }

    /**
     * @param data
     * @return 解析ID列表
     */
    public static List<String> parseIDs(String data) {
        List<String> ids = new ArrayList<String>();
        try {
            JSONArray a = new JSONArray(data);
//            if(a==null){
//            	return null;
//            }
            for (int i = 0; i < a.length(); i++) {
                String id = a.getString(i);
                ids.add(id);
            }
//            return ids;
        } catch (JSONException e) {
//            return null;
        }
        return ids;
    }

    /**
     * 解析用户列表
     * @param data
     * @return
     */
    public static List<User> parseUsers(String data) {
        List<User> users = new ArrayList<User>();
        try {
            JSONArray a = new JSONArray(data);
//            if(a==null){
//            	return null;
//            }
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                User user = User.parse(o);
                users.add(user);
            }
//            return users;
        } catch (JSONException e) {
//            return null;
        }
        return users;
    }

    /**
     * @param s 代表饭否日期和时间的字符串
     * @return 字符串解析为对应的Date类实例
     */
    public static Date parseDate(String s) {
        // Fanfou Date String example --> "Mon Dec 13 03:10:21 +0000 2010"
        final String fanfouDateString = "EEE MMM dd HH:mm:ss Z yyyy";
        final SimpleDateFormat FANFOU_DATE_FORMAT = new SimpleDateFormat(
                fanfouDateString, Locale.US);
        final ParsePosition position = new ParsePosition(0);// 这个如果放在方法外面的话，必须每次重置Index为0
        Date date = FANFOU_DATE_FORMAT.parse(s, position);
        return date;
    }

	public static List<Trends> parseTrends(String data) throws JSONException {
		if (Utils.isEmpty(data)) {
			return null;
		}
		List<Trends> list = new ArrayList<Trends>();
		JSONObject o = new JSONObject(data);
		JSONArray jsonArray = o.getJSONArray("trends");
		for (int i = 0; i < jsonArray.length(); ++i) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Trends trends = new Trends();
			trends.setName(jsonObject.getString("name"));
			trends.setQueryWord(jsonObject.getString("query"));
			trends.setUrl(jsonObject.getString("url"));
			list.add(trends);
		}
		return list;
	}

	public static Notification parseNotification(String data)
			throws JSONException {
		Notification notification = new Notification();
		JSONObject o = new JSONObject(data);
		notification.setMentions(o.getString("mentions"));
		notification.setDirect_messages(o.getString("direct_messages"));
		notification.setFriend_requests(o.getString("friend_requests"));
		return notification;
	}
}
