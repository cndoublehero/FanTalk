/** ****************************************************************************** */
/**
 * 饭否操作公开方法
 * @author mcxiaoke
 * @version 0.1 2010.12.06
 * @version 0.2 2010.12.10
 * @version 0.5 2010.12.13
 * @version 0.6 2010.12.15
 * @version 0.7 2010.12.16
 */
/** ****************************************************************************** */

/** FanFou.java */

package project.fantalk.api.fanfou;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.basicAuth.AbstractBasicAuth;
import project.fantalk.model.Member;

public class FanfouService extends AbstractBasicAuth{
    /** 日志工具 */
    private static final Logger logger = Logger.getLogger(FanfouService.class
            .getName());
    /** 饭否API Source */
    private static final String SOURCE = "fantalk";

    /**
     * 构造函数，饭否暂时只支持Basic认证
     * @param username 饭否用户名
     * @param password 饭否密码
     */
    public FanfouService(String username, String password) {
    	super(username, password);
    }
    
    public FanfouService(Member member) {
    	super(member.getUsername(), member.getPassword());
    }
    
    public Member processMember(Member member) {
		member.setUsername(getUsername());
		member.setPassword(getPassword());
		member.put();
		return member;
	}
	
    public String getBindErrorMessage() {
		return "饭否帐号绑定失败(如果是中文用户名，请使用邮箱或手机号进行绑定操作)，请检查帐号和密码是否正确!";
	}

	public String getBindOkMessage() {
		return "饭否帐号绑定成功!";
	}
    
    /*------------------------------饭否用户操作方法------------------------------*/
    /**
     * 获取用户资料
     * @param userId 用户ID
     * @return 返回某个用户的资料
     */
	public User showUser(String userId) {
		String data = doGet(API.SHOW_USER.url(), getParamByUserId(userId));
		logger.info(data);
		return Parser.parseUser(data);
	}

    /**
     * 获取认证用户的资料
     * @return 返回认证用户的资料
     */
    public User showMe() {
    	return showUser(null);
    }

    /**
     * 关注某用户
     * @param userId 用户的ID
     * @return 关注某个用户
     */
    public boolean follow(String userId) {
        return doSingleAction(API.FOLLOW, userId);
    }

    /**
     * 取消关注用户
     * @param userId 用户ID
     * @return 取消关注某个用户
     */
    public boolean unfollow(String userId) {
        return doSingleAction(API.UNFOLLOW, userId);
    }

	/**
	 * 获取用户黑名单资料
	 * @param pageNo
	 * 		页数
	 * 
	 * @return 黑名单用户列表
	 */
	public List<User> blockList(int pageNo) {
		String data = doGet(API.BLOCKLIST.url(), "page=" + pageNo);
		return parseUsers(data);
	}
    
    /**
     * 将某个用户加入黑名单
     * @param userId 用户ID
     * @return 加入黑名单
     */
    public boolean block(String userId) {
        return doSingleAction(API.BLOCK, userId);
    }

    /**
     * 移出黑名单
     * @param userId 用户ID
     * @return 将某个用户移出黑名单
     */
    public boolean unblock(String userId) {
        return doSingleAction(API.UNBLOCK, userId);
    }

    /**
     * 判断用户关系
     * @param usera 用户A的ID
     * @param userb 用户B的ID
     * @return 如果用户A关注了用户B，返回True
     */
	public boolean relation(String usera, String userb) {
		String params = "user_a=" + Utils.encode(usera) + "&user_b="
				+ Utils.encode(userb);
		String data = doGet(API.FRIENDSHIP.url(), params);
		return isActionSuccess(data);
	}

    /**
     * @return 返回认证用户的好友ID列表
     */
    public List<String> friendsIDs() {
        return friendsIDs(null);
    }

    /**
     * 获取好友ID列表
     * @param userId 用户ID，如果为空，则表示获取认证用户的好友列表
     * @return 返回好友ID列表
     */
	public List<String> friendsIDs(String userId) {
		String data = doGet(API.FRIENDS_IDS_LIST.url(), getParamByUserId(userId));
		logger.info(data);
		return Parser.parseFriends(data);
	}

    /**
     * @return 返回认证用户的关注者ID列表
     */
    public List<String> followersIDs() {
        return followersIDs(null);
    }

    /**
     * 获取关注者ID列表
     * @param userId 用户ID，如果为空，则表示获取认证用户的关注者列表
     * @return 返回关注者ID列表
     */
	public List<String> followersIDs(String userId) {
		String data = doGet(API.FOLLOWERS_IDS_LIST.url(), getParamByUserId(userId));
		logger.info(data);
		return Parser.parsefollowers(data);
	}

    /**
     * @return 返回认证用户的好友列表
     */
    public List<User> friends() {
        return friends(null);
    }

    /**
     * 获取好友列表，每页100个
     * @param userId 用户ID，如果没有参数，则为认证用户
     * @return 返回User列表
     */
	public List<User> friends(String userId) {
		String data = doGet(API.FRIENDS_LIST.url(), getParamByUserId(userId));
		return parseUsers(data);
	}

    /**
     * 获取认证用户的好友列表
     * @param page 页码，从1开始
     * @return 好友列表
     */
    public List<User> friends(int page) {
        String data = doGet(API.FRIENDS_LIST.url(), "page=" + page);
        return parseUsers(data);
    }

    /**
     * @return 返回认证用户的关注者列表
     */
    public List<User> followers() {
        return followers(null);
    }

    /**
     * 获取关注者列表，每页100个
     * @param userId 用户ID，如果没有参数，则为认证用户
     * @return 返回User列表
     */
	public List<User> followers(String userId) {
		String data = doGet(API.FOLLOWERS_LIST.url(), getParamByUserId(userId));
		return parseUsers(data);
	}

    /**
     * 获取认证用户的关注者列表
     * @param page 页码，从1开始
     * @return 返回关注者列表
     */
    public List<User> followers(int page) {
        String data = doGet(API.FOLLOWERS_LIST.url(), "page=" + page);
        return parseUsers(data);
    }

    /*------------------------------饭否时间线操作方法------------------------------*/
    /**
     * 获取认证用户主页时间线
     * @return 返回认证用户主页时间线(包括自己的和好友的)
     */
    public List<Status> home() {
        return doTimelineAction(API.FRIENDS_TIMELINE, null);
    }

    public List<Status> lookAround() {
    	return doTimelineAction(API.PUBLIC_TIMELINE, null);
    }
    
    /**
     * 获取主页时间线
     * @param count 消息数，范围1-20，默认为20
     * @param sinceId 起始消息ID
     * @param maxId 最大消息ID
     * @param page 页码，从1开始
     * @param format 格式，如果有只能为html，返回解析后的消息内容
     * @return 返回主页消息列表
     */
    public List<Status> home(int count, String sinceId, String maxId, int page) {
        StringBuilder sb = new StringBuilder();
        sb.append("count=" + count);
        if (!Utils.isEmpty(sinceId)) {
            sb.append("&since_id=" + Utils.encode(sinceId));
        }
        if (!Utils.isEmpty(maxId)) {
            sb.append("&max_id=" + Utils.encode(maxId));
        }
        if (page > 1) {
            sb.append("&page=" + page);
        }
        String data = doGet(API.FRIENDS_TIMELINE.url(), sb.toString());
        return parseTimeline(data);
    }

    /**
     * 获取用户个人时间线
     * @param userId 用户的ID
     * @return 返回某个用户的时间线
     */
    public List<Status> timeline(String userId) {
        return doTimelineAction(API.USER_TIMELINE, userId);
    }

    /**
     * 获取用户时间线
     * @param userId 用户ID
     * @param count 消息数，范围1-20，默认为20
     * @param sinceId 起始消息ID
     * @param maxId 最大消息ID
     * @param page 页码，从1开始
     * @param format 格式，如果有只能为html，返回解析后的消息内容
     * @return 返回用户消息列表
     */
    public List<Status> timeline(String userId, int count, String sinceId,
            String maxId, int page) {
        StringBuilder sb = new StringBuilder();
        sb.append("id=" + Utils.encode(userId));
        sb.append("&count=" + count);
        if (!Utils.isEmpty(sinceId)) {
            sb.append("&since_id=" + Utils.encode(sinceId));
        }
        if (!Utils.isEmpty(maxId)) {
            sb.append("&max_id=" + Utils.encode(maxId));
        }
        if (page > 1) {
            sb.append("&page=" + page);
        }
        String data = doGet(API.USER_TIMELINE.url(), sb.toString());
        return parseTimeline(data);
    }

    /**
     * 获取用户回复列表
     * @return 返回给某个用户的回复列表
     */
    public List<Status> mentions() {
        return doTimelineAction(API.MENTIONS, null);
    }

    /**
     * 获取回复消息列表
     * @param count 消息数，范围1-20，默认为20
     * @param sinceId 起始消息ID
     * @param maxId 最大消息ID
     * @param page 页码，从1开始
     * @param format 格式，如果有只能为html，返回解析后的消息内容
     * @return 返回回复列表
     */
    public List<Status> mentions(int count, String sinceId, String maxId,
            int page) {
        StringBuilder sb = new StringBuilder();
        sb.append("count=" + count);
        if (!Utils.isEmpty(sinceId)) {
            sb.append("&since_id=" + Utils.encode(sinceId));
        }
        if (!Utils.isEmpty(maxId)) {
            sb.append("&max_id=" + Utils.encode(maxId));
        }
        if (page > 1) {
            sb.append("&page=" + page);
        }
        String data = doGet(API.MENTIONS.url(), sb.toString());
        return parseTimeline(data);
    }

    /*------------------------------饭否消息操作方法------------------------------*/
    /**
     * 发布消息
     * @param text 消息内容
     * @return 发送成功则返回True
     */
	public ReturnCode update(String text) {
		String params = "status=" + Utils.encode(text) + "&source="
				+ Utils.encode(getSource());
		String data = doPost(API.UPDATE_STATUS.url(), params);
		return getActionCode(data);
	}

    /**
     * 回复消息
     * @param text 回复内容
     * @param inReplyToStatusId 回复的消息ID
     * @return 回复成功则返回True
     */
	public boolean reply(String text, String inReplyToStatusId) {
		String params = "status=" + Utils.encode(text) + "&source="
				+ Utils.encode(getSource()) + "&in_reply_to_status_id="
				+ Utils.encode(inReplyToStatusId);
		String data = doPost(API.UPDATE_STATUS.url(), params);
		return isActionSuccess(data);
	}

    /**
     * 转发消息
     * @param text 转发评论内容
     * @param inRepostStatusId 转发的消息ID
     * @return 转发成功则返回True
     */
	public boolean repost(String text, String inRepostStatusId) {
		String params = "status=" + Utils.encode(text) + "&source="
				+ Utils.encode(getSource()) + "&repost_status_id="
				+ Utils.encode(inRepostStatusId);
		String data = doPost(API.UPDATE_STATUS.url(), params);
		return isActionSuccess(data);
	}

    /**
     * 删除一条消息
     * @param statusId 消息ID
     * @return 删除消息
     */
    public boolean delete(String statusId) {
        return doSingleAction(API.DELETE_STATUS, statusId);
    }

    /**
     * 收藏一条消息
     * @param statusId 消息ID
     * @return 收藏消息
     */
    public boolean favorite(String statusId) {
        return doSingleAction(API.FAVORITE_STATUS, statusId);
    }

    /**
     * 取消收藏一条消息
     * @param statusId 消息ID
     * @return 取消收藏消息
     */
    public boolean unfavorite(String statusId) {
        return doSingleAction(API.UNFAVORITE_STATUS, statusId);
    }

    /*------------------------------饭否私信操作方法------------------------------*/
    /**
     * 获取收到的私信列表，默认最近20条
     */
    public List<Message> inbox() {
        String data = doGet(API.DIRECT_MESSAGES_INBOX.url());
        return parseMessages(data);
    }

    /**
     * 获取收件箱私信列表
     * @param count 私信数，范围1-20，默认20
     * @param sinceId 私信起始ID
     * @param maxId 私信最大ID
     * @param page 页码，从1开始
     * @return 返回收件箱私信列表
     */
    public List<Message> inbox(int count, String sinceId, String maxId, int page) {
        StringBuilder sb = new StringBuilder();
        sb.append("count=" + count);
        if (!Utils.isEmpty(sinceId)) {
            sb.append("&since_id=" + Utils.encode(sinceId));
        }
        if (!Utils.isEmpty(maxId)) {
            sb.append("&max_id=" + Utils.encode(maxId));
        }
        if (page > 1) {
            sb.append("&page=" + page);
        }
        String data = doGet(API.DIRECT_MESSAGES_INBOX.url(), sb.toString());
        return parseMessages(data);
    }

    /**
     * 获取发送的私信列表，默认为最近20条
     */
    public List<Message> outbox() {
        String data = doGet(API.DIRECT_MESSAGES_OUTBOX.url());
        return parseMessages(data);
    }

    /**
     * 获取发件箱私信列表
     * @param count 私信数，范围1-20，默认20
     * @param sinceId 私信起始ID
     * @param maxId 私信最大ID
     * @param page 页码，从1开始
     * @return 返回发件箱私信列表
     */
    public List<Message> outbox(int count, String sinceId, String maxId,
            int page) {
        StringBuilder sb = new StringBuilder();
        sb.append("count=" + count);
        if (!Utils.isEmpty(sinceId)) {
            sb.append("&since_id=" + Utils.encode(sinceId));
        }
        if (!Utils.isEmpty(maxId)) {
            sb.append("&max_id=" + Utils.encode(maxId));
        }
        if (page > 1) {
            sb.append("&page=" + page);
        }
        String data = doGet(API.DIRECT_MESSAGES_OUTBOX.url(), sb.toString());
        return parseMessages(data);
    }

    /**
     * 发送私信
     * @param userId 私信接收者ID
     * @param text 私信内容
     * @return 发送成功则返回True
     */
	public boolean sendMessage(String userId, String text) {
		String params = "user=" + Utils.encode(userId) + "&text="
				+ Utils.encode(text);
		String data = doPost(API.SEND_DIRECT_MESSAGE.url(), params);
		return isActionSuccess(data);
	}

    /**
     * 回复私信
     * @param userId 私信接收者ID
     * @param inReplyToMessageId 回复的私信ID
     * @param text 私信内容
     * @return 发送成功则返回True
     */
    public boolean replyMessage(String userId, String inReplyToMessageId,
			String text) {
		String params = "user=" + Utils.encode(userId) + "&text="
				+ Utils.encode(text) + "&in_reply_to_id="
				+ Utils.encode(inReplyToMessageId);
		String data = doPost(API.SEND_DIRECT_MESSAGE.url(), params);
		return isActionSuccess(data);
	}

    /**
     * 删除一条私信
     * @param messageId 私信的ID
     * @return 删除成功则返回True
     */
    public boolean deleteMessage(String messageId) {
        return doSingleAction(API.DELETE_DIRECT_MESSAGE, messageId);
    }

    /*------------------------------饭否其它操作方法------------------------------*/
    /**
     * 验证帐号密码是否正确
     * @return 如果正确，返回True
     */
	public ReturnCode verify() {
		String data = doGet(API.VERIFY_ACCOUNT.url());
		return getActionCode(data);
	}

    /**
     * 搜索公开时间线
     * @param keyword 搜索关键词
     * @return 返回搜索结果列表
     */
    public List<Status> search(String keyword) {
        String data = doGet(API.SEARCH.url(), "q=" + Utils.encode(keyword));
        logger.info(data);
        return Parser.parseStatuses(data);
    }

    /**
     * 上传照片
     * @param photo 照片文件
     * @param status 消息内容
     * @return 成功的话返回True
     */
    public boolean upload(File photo, String status) {
        return false;
    }

    /**
     * 搜索热词
     * @param
     * @return 返回搜索结果列表
     */
    public List<Trends> trends() {
    	 try {
             String data = doGet(API.TRENDS.url());
             logger.info(data);
             return Parser.parseTrends(data);
         } catch (JSONException e) {
        	 logger.log(Level.WARNING, e.getMessage(), e);
         }
    	return null;
    }
    
	/**
	 * 返回未读的mentions, direct message,以及关注请求数量
	 * 
	 * @return 返回未读的mentions, direct message,以及关注请求数量
	 */
	public Notification notification() {
		try {
			String data = doGet(API.NOTIFICATION.url());
			logger.info(data);
			return Parser.parseNotification(data);
		} catch (JSONException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return null;
	}
    
    /*------------------------------饭否通用操作方法------------------------------*/
    /**
     * 封装只有单个ID参数的POST操作
     * 比如follow/unfollow/delete/favorite/unfavorite/block/unblock
     * @param api 远程地址
     * @param id 参数：用户/消息/私信的ID
     * @return 成功就返回True
     */
	private boolean doSingleAction(API api, String id) {
		String data = doPost(api.url(), "id=" + Utils.encode(id));
		return isActionSuccess(data);
	}

	/**
	 * 判断操作是否成功，若返回的json数据（data）中包含有id字段，则表示成功了，返回true；否则表示失败了，返回false
	 * 
	 * @param data
	 *            json数据
	 * @return 成功就返回True；否则返回false
	 */
	private boolean isActionSuccess(String data) {
		if (!Utils.isEmpty(data)) {
			try {
				logger.info(data);
				JSONObject o = new JSONObject(data);
				if (o.has("id")) {
					return true;
				}
			} catch (JSONException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return false;
	}
    
	/**
	 * 判断操作是否成功，若返回的json数据（data）中包含有id字段，则表示成功了，返回ERROR_OK；否则表示失败了，返回ERROR_FALSE
	 * 
	 * @param data
	 *            json数据
	 * @return 成功就返回ERROR_OK；否则返回ERROR_FALSE
	 */
	private ReturnCode getActionCode(String data) {
		return isActionSuccess(data) ? ReturnCode.ERROR_OK
				: ReturnCode.ERROR_FALSE;
	}
	
	/**
	 * 根据userId获取返回的参数
	 * 
	 * @param userId
	 * @return
	 */
	private String getParamByUserId(String userId) {
		return userId == null ? null : "id=" + Utils.encode(userId);
	}
	
	private List<Status> doTimelineAction(API api, String userId) {
		String data = doGet(api.url(), getParamByUserId(userId));
		return parseTimeline(data);
	}

	/**
	 * @param data
	 * @return 解析时间线
	 */
	private List<Status> parseTimeline(String data) {
		logger.info(data);
		return Parser.parseTimeline(data);
	}

	/**
	 * @param data
	 * @return 解析私信列表
	 */
	private List<Message> parseMessages(String data) {
		logger.info(data);
		return Parser.parseMessages(data);
	}

	/**
	 * 解析用户列表
	 * 
	 * @param data
	 * @return
	 */
	private List<User> parseUsers(String data) {
		logger.info(data);
		return Parser.parseUsers(data);
	}
	
    /**
     * 枚举，饭否API方法接口URL列表
     * @author mcxiaoke
     */
    static enum API {
        UPDATE_STATUS("statuses/update"), // POST 发布消息
        SHOW_STATUS("statuses/show"), // GET 显示指定消息
        DELETE_STATUS("statuses/destroy"), // POST 删除消息
        FAVORITE_STATUS("favorites/create"), // POST 收藏消息
        UNFAVORITE_STATUS("favorites/destroy"), // POST 取消收藏

        FOLLOW("friendships/create"), // POST 添加好友
        UNFOLLOW("friendships/destroy"), // POST 删除好友
        FRIENDSHIP("friendships/exists"), // GET 好友关系

        DIRECT_MESSAGES_INBOX("direct_messages"), // GET 收到的私信列表
        DIRECT_MESSAGES_OUTBOX("direct_messages/sent"), // GET 发出的私信列表

        SEND_DIRECT_MESSAGE("direct_messages/new"), // POST 发送私信
        DELETE_DIRECT_MESSAGE("direct_messages/destroy"), // POST 删除私信

        /**
         * 消息操作和私信操作可选参数：id,count,page,since_id,max_id,
         */
        PUBLIC_TIMELINE("statuses/public_timeline"), // GET 随便看看
        FRIENDS_TIMELINE("statuses/friends_timeline"), // GET
        // 好友消息，可选page参数，每页20条
        USER_TIMELINE("statuses/user_timeline"), // GET 用户消息，可选page参数，每页20条
        MENTIONS("statuses/replies"), // GET 回复消息

        SHOW_USER("users/show"), // GET 用户资料

        FRIENDS_LIST("users/friends"), // GET 好友列表，可选page参数，每页100条
        FOLLOWERS_LIST("users/followers"), // GET 关注者列表，可选page参数，每页100条

        FRIENDS_IDS_LIST("friends/ids"), // GET 好友ID列表
        FOLLOWERS_IDS_LIST("followers/ids"), // GET 关注者ID列表

        FAVORITES_LIST("favorites"), // GET 收藏列表

        UPLOAD_PHOTO("photos/upload"), // POST 上传照片

        SEARCH("search/public_timeline"), // GET 公开搜素
        BLOCK("blocks/create"), // POST 加入黑名单
        UNBLOCK("blocks/destroy"), // POST 删除黑名单
        BLOCKLIST("blocks/blocking"), // 查看黑名单用户资料
        
        VERIFY_ACCOUNT("account/verify_credentials"), // GET 验证帐号密码
        
		NOTIFICATION("account/notification"), // 返回未读的mentions, direct message,以及关注请求数量
        
        TRENDS("trends"),
		;

        private String value;
        private static final String API_URL = "http://api.fanfou.com/";// 饭否API地址
        private static final String JSON = ".json";// JSON格式

        API(String value) {
            this.value = value;
        }

        public String url() {
            return new StringBuilder().append(API_URL).append(value).append(
                    JSON).toString();
        }
    }

	@Override
	public String getSource() {
		return SOURCE;
	}
	
	@Override
	public String getSNSName() {
		return "fanfou";
	}
}
