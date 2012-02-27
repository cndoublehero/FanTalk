package project.fantalk.api.fanfou;

import java.util.Date;


import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.Utils;

public class Message {
    private String id;
    private String text;
    private String senderID;
    private String recipientID;
    private String senderScreenName;
    private String recipientScreenName;
    private Date createdAt;

    private Message() {}

	public String process() {
		StringBuilder sb = new StringBuilder();
		sb.append(senderID).append(Utils.SPLIT_STR).append(recipientID).append(
				Utils.SPLIT_STR).append(id);
		return sb.toString();
	}
    
    public static Message parse(String data) {
        try {
            JSONObject o = new JSONObject(data);
            return parse(o);
        } catch (JSONException e) {
            return null;
        }
    }

    public static Message parse(JSONObject json) {
        try {
            JSONObject o = json;
            Message m = new Message();
            m.id = o.getString("id");
            m.text = o.getString("text");
            m.senderID = o.getString("sender_id");
            m.recipientID = o.getString("recipient_id");
            m.senderScreenName = o.getString("sender_screen_name");
            m.recipientScreenName = o.getString("recipient_screen_name");
            m.createdAt = Parser.parseDate(o.getString("created_at"));
            return m;
        } catch (JSONException e) {
            return null;
        }
    };

    {
        /** 私信返回JSON数据 */
        // [
        // -{
        // id: "ULJClJ5k03M"
        // text: "加油开发安卓相关呀，我打算入安卓手机了 ◕‿◕"
        // sender_id: "alizuzu"
        // recipient_id: "mcxiaoke"
        // created_at: "Thu Dec 02 15:02:09 +0000 2010"
        // sender_screen_name: "柒海"
        // recipient_screen_name: "小可"
        // -sender: {
        // id: "alizuzu"
        // name: "柒海"
        // screen_name: "柒海"
        // location: ""
        // gender: "女"
        // birthday: ""
        // description: " 关注多为往来频繁的友人， 部分因爱其感受生活的态度， 部分因自觉其读书摘录受用。
        // 加不起牛叉哄哄的人，做不起微博型的粉丝。 web：i5land.cn foto：eurema.cn
        // db：http://is.gd/i1C0V"
        // profile_image_url:
        // "http://avatar.fanfou.com/s0/00/3b/5n.jpg?1290995908"
        // url: "http://eurema.cn"
        // protected: false
        // followers_count: 348
        // friends_count: 61
        // favourites_count: 363
        // statuses_count: 563
        // following: true
        // notifications: true
        // created_at: "Tue Jul 10 10:04:47 +0000 2007"
        // utc_offset: 28800
        // profile_background_color: "#372A33"
        // profile_text_color: "#757F89"
        // profile_link_color: "#A5B7B5"
        // profile_sidebar_fill_color: "#F0ECDE"
        // profile_sidebar_border_color: "#F5F5F5"
        // profile_background_image_url:
        // "http://avatar.fanfou.com/b0/00/3b/5n_1290996966.jpg"
        // profile_background_tile: false
        // }
        // -recipient: {
        // id: "mcxiaoke"
        // name: "小可"
        // screen_name: "小可"
        // location: "江苏 南京"
        // gender: "男"
        // birthday: "0000-10-20"
        // description: "命由我作，福自己求，喜欢简单和自由的生活"
        // profile_image_url:
        // "http://avatar.fanfou.com/s0/00/3a/s3.jpg?1189761303"
        // url: "http://wheremylife.com/"
        // protected: true
        // followers_count: 38
        // friends_count: 36
        // favourites_count: 95
        // statuses_count: 2806
        // following: false
        // notifications: false
        // created_at: "Fri Sep 14 07:54:05 +0000 2007"
        // utc_offset: 28800
        // profile_background_color: "#3a9dcf"
        // profile_text_color: "#4b585e"
        // profile_link_color: "#3a9dcf"
        // profile_sidebar_fill_color: "#000000"
        // profile_sidebar_border_color: "#006fa6"
        // profile_background_image_url:
        // "http://static.fanfou.com/img/bg/10.png"
        // profile_background_tile: false
        // }
        // }
        // -{
        // id: "OR6z6XiF8u0"
        // text: "好的，谢谢！"
        // sender_id: "Tushen"
        // recipient_id: "mcxiaoke"
        // created_at: "Fri Nov 26 08:12:01 +0000 2010"
        // sender_screen_name: "小小坤"
        // recipient_screen_name: "小可"
        // +sender: { … }
        // +recipient: { … }
        // }
        // ]
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public String getSenderScreenName() {
        return senderScreenName;
    }

    public String getRecipientScreenName() {
        return recipientScreenName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
