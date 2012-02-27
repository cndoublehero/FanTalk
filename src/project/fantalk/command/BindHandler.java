package project.fantalk.command;

import java.util.logging.Logger;

import project.fantalk.api.Utils;
import project.fantalk.api.common.AbstractAuth;
import project.fantalk.api.digu.DiGuService;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;


import com.google.appengine.api.xmpp.JID;
import com.google.common.base.Strings;

public class BindHandler extends BaseCommand {
	private static final Logger logger = Logger.getLogger(BindHandler.class
			.getName());

	public BindHandler() {
		super("bind", "b");
	}

	@Override
	public void doCommand(Message message, String argument) {
		if (Strings.isNullOrEmpty(argument)) {
			XMPPUtils.sendMessage(getErrorMessage(), message.sender);
			return;
		}
		JID sender = message.sender;
		String email = message.email;
		String[] args = argument.split("\\s+", 3);
		if (args.length < 3 || Utils.isEmpty(args[2])) {
			XMPPUtils.sendMessage(getErrorMessage(), sender);
			return;
		}
		String type = args[0];
		String username = args[1];
		String password = args[2];
		int userCount = Datastore.getInstance().getAllEmailCount();
		logger.info("user 数：" + userCount);
		if(userCount > 900) {
			XMPPUtils.sendMessage("用户人数超过了900人，请不要在此机器人上绑定账号了，谢谢！", email);
			return;
		}
		Member m = Datastore.getInstance().getAndCacheMember(email);
		AbstractAuth service = getService(type, username, password);
		verifyAndProcess(service, m);
	}

	public AbstractAuth getService(String type, String username, String password) {
		AbstractAuth method = null;
		if("fanfou".equals(type)) {
			method = new FanfouService(username, password);
		} else if("digu".equals(type)) {
			method = new DiGuService(username, password);
		} 
		return method;
	}
	
	public void verifyAndProcess(AbstractAuth service, Member member) {
		String email = member.getEmail();
		if (service != null) {
			if (service.verify().isOk()) {
				service.processMember(member);
				XMPPUtils.sendMessage(service.getBindOkMessage(), email);
			} else {
				XMPPUtils.sendMessage(service.getBindErrorMessage(), email);
			}
		} else {
			XMPPUtils.sendMessage(getErrorMessage(), email);
		}
	}
	
	private String getErrorMessage() {
		StringBuilder sb = new StringBuilder();
		sb
				.append("参数格式不正确，绑定帐号的命令格式为：\n")
				.append("绑定饭否帐号：-bind fanfou username password\n")
				.append("(饭否帐号如果是中文，请使用邮箱或手机号进行绑定操作\n)")
				.append(
						"推特/腾讯微博/新浪微博/豆瓣请去FanTalk主页进行帐号绑定操作：http://"+Utils.getAppID()+".appspot.com/\n")
				.append("有问题反馈请访问项目页面：http://code.google.com/p/fanfoutalk/\n");
		return sb.toString();
	}
	
	public String documentation() {
		StringBuilder sb = new StringBuilder();
		sb
				.append("-bind fanfou username password\n绑定饭否帐号(如果是中文用户名，请使用邮箱或手机号进行绑定操作)，绑定其它帐号请去FanTalk主页");
		return sb.toString();
	}

}
