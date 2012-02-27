/**
 * 
 */
package project.fantalk.api.fanfou;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author mcxiaoke
 * @data 2010.12.06
 * @version 0.1 Fanfou API for Java
 */
public class User {
    private String id;
    private String name;
    private String screenName;
    private String location;
    private String gender;
    private String birthday;
    private String description;
    private String profileImageUrl;
    private String url;
    private int followersCount;
    private int friendsCount;
    private int statusesCount;
    private int favoritesCount;
    private boolean protect;
    private boolean following;
    private boolean notifications;
    private Date createdAt;
    private int utcOffset;

    /**
     * 
     */
    private User() {}

    public static User parse(String data) {
        try {
            JSONObject o = new JSONObject(data);
            return parse(o);
        } catch (JSONException e) {
            return null;
        }
    }

    public static User parse(JSONObject json) {
        try {
            JSONObject o = json;
            if (!o.has("id")) {
                return null;
            }
            User u = new User();
            u.id = o.getString("id");
            u.name = o.getString("name");
            u.screenName = o.getString("screen_name");
            u.location = o.getString("location");
            u.gender = o.getString("gender");
            u.birthday = o.getString("birthday");
            u.description = o.getString("description");
            u.profileImageUrl = o.getString("profile_image_url");
            u.url = o.getString("url");
            u.protect = o.getBoolean("protected");
            u.friendsCount = o.getInt("friends_count");
            u.followersCount = o.getInt("followers_count");
            u.favoritesCount = o.getInt("favourites_count");
            u.statusesCount = o.getInt("statuses_count");
            u.following = o.getBoolean("following");
            u.createdAt = Parser.parseDate(o.getString("created_at"));
            u.notifications = o.getBoolean("notifications");
            u.utcOffset = o.getInt("utc_offset");
            return u;
        } catch (JSONException e) {
            return null;
        }
    }

    {
        /** 个人信息返回JSON数据 */
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
    }

    {
        /** 好友列表和关注者列表返回JSON数据 */
        // [
        // "wangxing"
        // "fanfou"
        // "fannin"
        // "alizuzu"
        // "lovearro"
        // "yiliang"
        // "颜夏"
        // "亦亦"
        // "faye723"
        // "wanfanhua"
        // "hj__"
        // "wubuntust"
        // "yixiaoai"
        // "miufox"
        // "angel_an"
        // ]
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getLocation() {
        return location;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUrl() {
        return url;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public boolean isProtect() {
        return protect;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

}
