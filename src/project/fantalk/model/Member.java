/**
 * 
 */
package project.fantalk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.xmpp.JID;

/**
 * Member类，代表使用此XMPP服务的成员
 * @author mcxiaoke
 * @date 2010.12.18
 */

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public final class Member implements Serializable {
    private static final long serialVersionUID = 2282589174585035352L;
    /** 自动生成的ID */
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private long id;

    @Persistent
    @PrimaryKey
    /** 主键，用户的Email地址 */
    private String email;

    @Persistent
    /** 是否第一次使用 */
    private boolean newer;

    @Persistent
    /**
     * 是否开启同步发送到其它微博
     */
    private boolean sync;

    /** 饭否属性 */
    @Persistent
    /**
     * 饭否消息推送类型 全部：home/all -- 包括home mentions dm 回复：mentions//tome --
     * 包括mentions dm
     */
    private String pushType;

    @Persistent
    /** 饭否名字 */
    private String name;

    @Persistent
    /** 饭否帐号 */
    private String username;

    @Persistent
    /** 饭否密码 */
    private String password;
    @Persistent
    private String lastStatusId;
    @Persistent
    private String lastMentionId;
    @Persistent
    private String lastMessageId;
    @Persistent
    private List<String> lastStatus;

    /**
     * 以下的username和password在使用Basic认证时为帐号和密码
     * 在使用Oauth认证时为AccessToken和AccessTokenSecret
     */

    @Persistent
    private String fanfouUserName;
	@Persistent
    private String fanfouPassWord;
    
	@Persistent
    private String token;
	@Persistent
    private String tokenSecret;
    
    /** 其它服务属性 */
    @Persistent
    private String twitterUsername;
    @Persistent
    private String twitterPassword;
    @Persistent
    private String buzzUsername;
    @Persistent
    private String buzzPassword;
    @Persistent
    private String doubanUsername;
    @Persistent
    private String doubanPassword;
    @Persistent
    private String tencentUsername;
    @Persistent
    private String tecentPassword;
    @Persistent
    private String sinaUsername;
    @Persistent
    private String sinaPassword;
    @Persistent
    private String neteaseUsername;
    @Persistent
    private String neteasePassword;
    @Persistent
    private String zuosaUsername;
    @Persistent
    private String zuosaPassword;
    @Persistent
    private String follow5Username;
    @Persistent
    private String follow5Password;
    @Persistent
    private String diguUsername;
    @Persistent
    private String diguPassword;
    @Persistent
    private String renrenUsername;//oauth_token
    @Persistent
    private String renrenPassword;//sessionId
    @Persistent
    private String sohuUsername;
	@Persistent
    private String sohuPassword;
    @Persistent
    private Date lastActive;

    /**
     * 写入持久存储
     */
    public void put() {
        Datastore.getInstance().put(this);
        cache();
    }

    /**
     * 从持久存储删除
     */
    public void delete() {
    	removeCache();
        Datastore.getInstance().delete(this.email);
        
    }

    /**
     * 添加到缓存
     */
    private void cache() {
        Datastore.getInstance().addToCache(email, this);
    }

    /**
     * 从缓存移除
     */
    private void removeCache() {
        Datastore.getInstance().removeCache(email);
    }

    /**
     * 获取用户的JID
     * @return 返回对应的JID
     */
    public JID getJID() {
        return new JID(email);
    }

    /**
     * 返回包含Member类信息的字符串
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Member Info\n[").append(", id=").append(id).append(
                ", Email=").append(email).append(", SyncType=").append(sync)
                .append(", PushType").append(pushType).append(
                        ", Fanfou Username=").append(username).append(
                        ", Fanfou Password=").append(password).append("]\n");
        return "";

    }

    public Member(String gmail) {
        this.email = gmail;
        this.newer = true;
        this.lastStatus = new ArrayList<String>();
        this.lastActive = new Date();
        this.sync = false;
        this.pushType = "";
        this.name = "";
        this.username = "";
        this.password = "";
        this.lastStatusId = "";
        this.lastMessageId = "";
        this.twitterUsername = "";
        this.twitterPassword = "";
        this.buzzUsername = "";
        this.buzzPassword = "";
        this.doubanUsername = "";
        this.doubanPassword = "";
        this.tencentUsername = "";
        this.tecentPassword = "";
        this.sinaUsername = "";
        this.sinaPassword = "";
        this.neteaseUsername = "";
        this.neteasePassword = "";
        this.zuosaUsername = "";
        this.zuosaPassword = "";
        this.follow5Username = "";
        this.follow5Password = "";
        this.diguUsername = "";
        this.diguPassword = "";
        this.renrenUsername = "";
        this.renrenPassword = "";
        this.fanfouUserName = "";
        this.fanfouPassWord = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isNewer() {
        return newer;
    }

    public void setNewer(boolean newer) {
        this.newer = newer;
    }

    public boolean isSyncOn() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastStatusId() {
        return lastStatusId;
    }

    public void setLastStatusId(String lastStatusId) {
        this.lastStatusId = lastStatusId;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public List<String> getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(List<String> lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    public String getTwitterPassword() {
        return twitterPassword;
    }

    public void setTwitterPassword(String twitterPassword) {
        this.twitterPassword = twitterPassword;
    }

    public String getBuzzUsername() {
        return buzzUsername;
    }

    public void setBuzzUsername(String buzzUsername) {
        this.buzzUsername = buzzUsername;
    }

    public String getBuzzPassword() {
        return buzzPassword;
    }

    public void setBuzzPassword(String buzzPassword) {
        this.buzzPassword = buzzPassword;
    }

    public String getDoubanUsername() {
        return doubanUsername;
    }

    public void setDoubanUsername(String doubanUsername) {
        this.doubanUsername = doubanUsername;
    }

    public String getDoubanPassword() {
        return doubanPassword;
    }

    public void setDoubanPassword(String doubanPassword) {
        this.doubanPassword = doubanPassword;
    }

    public String getTencentUsername() {
        return tencentUsername;
    }

    public void setTencentUsername(String tencentUsername) {
        this.tencentUsername = tencentUsername;
    }

    public String getTecentPassword() {
        return tecentPassword;
    }

    public void setTecentPassword(String tecentPassword) {
        this.tecentPassword = tecentPassword;
    }

    public String getSinaUsername() {
        return sinaUsername;
    }

    public void setSinaUsername(String sinaUsername) {
        this.sinaUsername = sinaUsername;
    }

    public String getSinaPassword() {
        return sinaPassword;
    }

    public void setSinaPassword(String sinaPassword) {
        this.sinaPassword = sinaPassword;
    }

    public String getNeteaseUsername() {
        return neteaseUsername;
    }

    public void setNeteaseUsername(String neteaseUsername) {
        this.neteaseUsername = neteaseUsername;
    }

    public String getNeteasePassword() {
        return neteasePassword;
    }

    public void setNeteasePassword(String neteasePassword) {
        this.neteasePassword = neteasePassword;
    }

    public String getZuosaUsername() {
        return zuosaUsername;
    }

    public void setZuosaUsername(String zuosaUsername) {
        this.zuosaUsername = zuosaUsername;
    }

    public String getZuosaPassword() {
        return zuosaPassword;
    }

    public void setZuosaPassword(String zuosaPassword) {
        this.zuosaPassword = zuosaPassword;
    }

    public String getFollow5Username() {
        return follow5Username;
    }

    public void setFollow5Username(String follow5Username) {
        this.follow5Username = follow5Username;
    }

    public String getFollow5Password() {
        return follow5Password;
    }

    public void setFollow5Password(String follow5Password) {
        this.follow5Password = follow5Password;
    }

    public String getDiguUsername() {
        return diguUsername;
    }

    public void setDiguUsername(String diguUsername) {
        this.diguUsername = diguUsername;
    }

    public String getDiguPassword() {
        return diguPassword;
    }

    public void setDiguPassword(String diguPassword) {
        this.diguPassword = diguPassword;
    }

    public String getRenrenUsername() {
        return renrenUsername;
    }

    public void setRenrenUsername(String renrenUsername) {
        this.renrenUsername = renrenUsername;
    }

    public String getRenrenPassword() {
        return renrenPassword;
    }

    public void setRenrenPassword(String renrenPassword) {
        this.renrenPassword = renrenPassword;
    }
    
    public String getSohuUsername() {
		return sohuUsername;
	}

	public void setSohuUsername(String sohuUsername) {
		this.sohuUsername = sohuUsername;
	}

	public String getSohuPassword() {
		return sohuPassword;
	}

	public void setSohuPassword(String sohuPassword) {
		this.sohuPassword = sohuPassword;
	}

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }

	public String getLastMentionId() {
		return lastMentionId;
	}

	public void setLastMentionId(String lastMentionId) {
		this.lastMentionId = lastMentionId;
	}
	public String getFanfouUserName() {
		return fanfouUserName;
	}

	public void setFanfouUserName(String fanfouUserName) {
		this.fanfouUserName = fanfouUserName;
	}

	public String getFanfouPassWord() {
		return fanfouPassWord;
	}

	public void setFanfouPassWord(String fanfouPassWord) {
		this.fanfouPassWord = fanfouPassWord;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
}
