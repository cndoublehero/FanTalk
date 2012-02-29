/**
 * 
 */
package project.fantalk.api.fanfou.domain;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.Parser;

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
