package project.fantalk.api.fanfou.domain;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.fanfou.Parser;

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
