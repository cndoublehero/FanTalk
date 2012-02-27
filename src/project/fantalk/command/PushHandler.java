package project.fantalk.command;

import project.fantalk.api.Utils;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;

public class PushHandler extends BaseCommand {
	public PushHandler() {
		super("push");
	}

	@Override
	public void doCommand(Message message, String argument) {
		JID sender = message.sender;
		String email = message.email;
		// if(!XMPPUtils.isAdmin(email)){
		// XMPPUtils.sendMessage("经测试，现在的PUSH机制性能问题严重，暂时停用", sender);
		// Member m = Datastore.getInstance().getAndCacheMember(email);
		// m.setPushType("off");
		// m.put();
		// return;
		// }
		if (Utils.isEmpty(argument)) {
			XMPPUtils.sendMessage(
					"-push home/mentions/off 设置饭否消息推送类型：主页消息/回复消息/关闭，默认关闭",
					sender);
			return;
		}
		Member m = Datastore.getInstance().getAndCacheMember(email);
		if (Utils.isEmpty(m.getUsername())
				&& Utils.isEmpty(m.getFanfouUserName())) {
			XMPPUtils.sendMessage("未绑定fanFou，无法开启消息推送服务", sender);
			return;
		}
		if (argument.equalsIgnoreCase("home")) {
			// m.setPushType("off");
			// m.put();
			m.setPushType("home");
			m.put();
			XMPPUtils.sendMessage("已开启主页消息推送", sender);

			// if (XMPPUtils.isAdmin(email)) {
			// }

			// try {
			// Queue queue = QueueFactory.getQueue("fetch");
			// // TaskHandle th = queue.add(TaskOptions.Builder.withUrl(
			// //
			// "/tasks/fanfoupush").taskName(Base64.encode(email.getBytes())).method(
			// // Method.GET).param("email", email));
			// queue.add(TaskOptions.Builder.withUrl("/tasks/fanfoupush")
			// .method(Method.GET).param("email", email));
			// } catch (TaskAlreadyExistsException e) {
			// logger.warning("task is already exist, not add");
			// }
		} else if (argument.equalsIgnoreCase("mentions")
				|| argument.equalsIgnoreCase("mention")) {
			// m.setPushType("off");
			// m.put();
			m.setPushType("mentions");
			m.put();
			XMPPUtils.sendMessage("已开启@消息推送", sender);
			// try {
			// Queue queue = QueueFactory.getQueue("fetch");
			// // TaskHandle th = queue.add(TaskOptions.Builder.withUrl(
			// //
			// "/tasks/fanfoupush").taskName(Base64.encode(email.getBytes())).method(
			// // Method.GET).param("email", email));
			// queue.add(TaskOptions.Builder.withUrl("/tasks/fanfoupush")
			// .method(Method.GET).param("email", email));
			// } catch (TaskAlreadyExistsException e) {
			// logger.warning("task is already exist, not add");
			// }
			// } else if (argument.equalsIgnoreCase("off")) {
			// m.setPushType("off");
			// m.put();
			// XMPPUtils.sendMessage("已关闭消息推送", sender);
		} else if (argument.equalsIgnoreCase("off")) {
			m.setPushType("off");
			m.put();
			XMPPUtils.sendMessage("消息推送已关闭", sender);
		} else {
			// m.setPushType("off");
			// m.put();
			// XMPPUtils.sendMessage("消息推送已关闭", sender);
			XMPPUtils
					.sendMessage(
							"参数错误，，关闭消息推送，使用-push off，开启回复消息推送，使用-push mentions，开启主页消息推送，使用-push home",
							sender);
		}
	}

	public String documentation() {
//		return "-push home/mentions/off\n设置消息推送类型：主页消息/回复消息/关闭，默认关闭";
		 return null;
	}

}
