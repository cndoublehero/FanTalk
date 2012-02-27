package project.fantalk.command;

import java.util.logging.Logger;

import project.fantalk.xmpp.Message;


public enum Command {
    /** 这个命令必须在最前面 */
    JOIN(new JoinHandler()), // 加入聊天室 -implemented.

    // 饭否API操作
    UNBIND(new UnBindHandler()), // 解除绑定 -implemented.
    // VERIFY(new VerifyCommand()),//验证用户名和密码 
     MESSAGE(new MessageCommand()),//发送私信 
    REPLY(new ReplyHandler()), // 回复消息，接消息ID -implemented.
     RETWEET(new RetweetHandler()),//转发消息，接消息ID 
    // FOLLOW(new FollowHandler()),//跟随某人
    // UNFOLLOW(new UnFollowHandler()),//取消跟随某人
    // FAVORITE(new FavoriteHandler()),//收藏消息，接消息ID
    // PROFILE(new ProfileHandler()),//显示用户资料
    // FRIENDSHIP(new FriendshipHandler()),//显示两个用户的关系
    // USERLINE(new UserlineHandler()),//某个用户的Timeline
     PUBLICLINE(new PubliclineCommand()),//随便看看 
    // FANFOUSEARCH(new FanfouSearchHandler()),//饭否消息搜索
    HOME(new HomeHandler()), // 获取自己的主页时间线 -implemented.
    MENTIONS(new MentionsHandler()),//获取@消息, -implemented.
    FANFOU(new FanfouHandler()), // 仅发送消息到饭否 -implemented.
    TWITTER(new TwitterHandler()), // 仅发送消息到Twitter -implemented.
    // 饭否API设置
    // NOTIFY(new NotifyHandler()),//通知类型设置
    PUSH(new PushHandler()), // 推送类型设置
    SYNC(new SyncHandler()), // 同步类型设置 -implemented.
    INBOX(new InBoxMessageCommand()),//查看收件箱 -implemented.
    OUTBOX(new OutBoxMessageCommand()),//查看发件箱 -implemented.
    DIRECT(new DirectMessageHandler()),//回复私信 -implemented.
    
    // 系统操作
    BROADCAST(new BroadcastHandler()), // 广播消息 admin -implemented.
    SYSINFO(new SystemHandler()),//显示系统信息 admin
     SHOWUSER(new UserInfoHandler()),//显示某个用户的信息 Admin
    // LIST(new ListHandler()),//显示用户列表 Admin
    HELP(new HelpHandler()), // 显示帮助 -implemented.
    // INVITE(new InviteHandler()),//邀请某人使用
    // KICK(new KickHandler()),//踢出某人 Admin
    STATUS(new StatusHandler()), // 显示个人状态信息 -implemented.
    FANTALK(new FanTalkHandler()), // 更新FanTalk官方帐号

    // 其它功能
    // GOOGLE(new GoogleHandler()),//Google搜索
    // WEATHER(new WeatherHandler()),//获取天气
    DEBUG(new DebugHandler()), // 调试命令 -implemented. admin
    UPGRADE(new UpgradeToolHandler()),//数据库升级命令 admin
    DICTCN(new DictHandler()), // 词典查询命令
    HotCommand(new HotCommand()),//查看fanfou热词
    SearchCommand(new SearchCommand()),//fanfou搜索命令
    BlockCommand(new BlockCommand()),//获取黑名单上的用户列表  
    
    FanfouOAuthURLCommand(new FanfouOAuthURLCommand()),//获取fanfou oauth绑定时的authURL
    FanFouOauthVeriyCommand(new FanFouOauthVeriyCommand()),//获取返回的oauth_verifier参数，完成fanfou的oauth验证过程
    
    /** 这两个命令必须在最后 */
    HANDLER(new Handler()), // 吃掉所有未知命令 -implemented.
    UPDATE(new UpdateHandler()), // 发布消息 必须在最后，默认就是发布消息 -implemented.
    ;

    public final CommandHandler commandHandler;
    private static final Logger logger = Logger.getLogger(Command.class
            .getName());

    private Command(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public static CommandHandler getCommandHandler(Message message) {
        for (Command command : Command.values()) {
            if (command.commandHandler.matches(message)) {
                logger.info(command.name());
                return command.commandHandler;
            }
        }
        throw new RuntimeException(
                "getCommandHandler should never return null, "
                        + "but we can't find a match. msg = "
                        + message.toString());
    }

}
