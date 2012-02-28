/**
 * 
 */
package project.fantalk.api.fanfou;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.Utils;

/**
 * @author mcxiaoke
 * @data 2010.12.06
 * @version 0.1 Fanfou API for Java
 */
public class Status {
    private Date createdAt;
    private String id;
    private String text;
    private String source;
    private boolean truncated;
    private String inReplyToStatusId;
    private String inReplyToUserId;
    private boolean favorited;
    private String inReplyToScreenName;
    private String photoUrl;

    private String userId;
    private String userName;
    private String userScreenName;
    private boolean userProtected;

    /**
     * 
     */
    private Status() {}

    /**
     * @param data
     * @return
     */
    public static Status parse(String data) {
        try {
            JSONObject o = new JSONObject(data);
            return parse(o);
        } catch (JSONException e) {
            return null;
        }
    }

	public String process() {
		StringBuilder sb = new StringBuilder();
		sb.append(id).append(Utils.SPLIT_STR).append(userName).append(Utils.SPLIT_STR).append(text);
		return sb.toString();
	}
    
    public static Status parse(JSONObject json) {
        try {
            JSONObject o = json;
            if (!o.has("id")) {
                return null;
            }
            Status s = new Status();
            s.createdAt = Parser.parseDate(o.getString("created_at"));
            s.id = o.getString("id");
            s.text = o.getString("text");
			s.source = Utils.getSource(o.getString("source"));
            s.truncated = o.getBoolean("truncated");
            s.inReplyToStatusId = o.getString("in_reply_to_status_id");
            s.inReplyToUserId = o.getString("in_reply_to_user_id");
            s.favorited = o.getBoolean("favorited");
            s.inReplyToScreenName = o.getString("in_reply_to_screen_name");

            if (o.has("photo")) {
                s.photoUrl = o.getJSONObject("photo").getString("largeurl");
            } else {
                s.photoUrl = "";
            }

            JSONObject user = o.getJSONObject("user");
            s.userId = user.getString("id");
            s.userName = user.getString("name");
            s.userScreenName = user.getString("screen_name");
            s.userProtected = user.getBoolean("protected");
            return s;
        } catch (JSONException e) {
            return null;
        }
    }

    {
        /** 消息返回JSON数据 */
        // created_at: "Sun Dec 12 04:25:50 +0000 2010"
        // id: "2Lv-arZpSew"
        // text: "@小土 如果没有小土爸妈，小土肯定不会出现，哈哈哈哈哈哈~~今晚雷雨加油！"
        // source: "网页"
        // truncated: false
        // in_reply_to_status_id: "ZTa6pwj3E6I"
        // in_reply_to_user_id: "xiaotu2007"
        // favorited: false
        // in_reply_to_screen_name: "小土"
        // -user: {
        // id: "小鬼小小帅"
        // name: "小鬼"
        // screen_name: "小鬼"
        // location: "广东"
        // gender: "女"
        // birthday: "0000-01-00"
        // description: "Me-无厘头的小鬼一个！知道天有多高，知道地有多大，知道海有多深，知道自己有多渺小；
        // 但不知道自己有多“抵死”！也许笑是我与生俱来的本事，亦或是上帝无意间赐予我的礼物！"
        // profile_image_url:
        // "http://avatar1.fanfou.com/s0/00/d3/mg.jpg?1243428454"
        // url: "http://sunnytimexiao.blog.163.com/"
        // protected: false
        // followers_count: 19
        // friends_count: 27
        // favourites_count: 0
        // statuses_count: 306
        // following: false
        // notifications: false
        // created_at: "Tue May 05 09:51:13 +0000 2009"
        // utc_offset: 28800
        // profile_background_color: "#3a9dcf"
        // profile_text_color: "#4b585e"
        // profile_link_color: "#3a9dcf"
        // profile_sidebar_fill_color: "#000000"
        // profile_sidebar_border_color: "#006fa6"
        // profile_background_image_url:
        // "http://avatar.fanfou.com/b0/00/d3/mg_1290787860.jpg"
        // profile_background_tile: true
        // }
    }

    {
        /** 收藏列表返回JSON数据 */
        // [
        // -{
        // created_at: "Fri Dec 10 01:03:41 +0000 2010"
        // id: "589k5ZiDaVM"
        // text: "我家四大宝贝：橡皮，蘑菇，灰太郎，懒羊羊，还有一盆不知名的顽强活着的小鱼儿"
        // source: "网页"
        // truncated: false
        // in_reply_to_status_id: ""
        // in_reply_to_user_id: ""
        // favorited: true
        // in_reply_to_screen_name: ""
        // +user: { … }
        // }
        // +{ … }
        // ]
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getSource() {
        return source;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public String getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public String getInReplyToUserId() {
        return inReplyToUserId;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public boolean isUserProtected() {
        return userProtected;
    }

}
