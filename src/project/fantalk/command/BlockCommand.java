package project.fantalk.command;

import java.util.ArrayList;
import java.util.List;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.api.fanfou.domain.User;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class BlockCommand extends BaseCommand {

	public BlockCommand() {
		super("block");
	}

	public String documentation() {
		return "-block\n  功能说明：获取黑名单上的用户列表";
	}

	@Override
	public void doCommand(Message message, String argument) {
		String email = message.email;
		Member m = (Member) Datastore.getInstance().getAndCacheMember(email);
		FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
		if (fanfou != null) {
			StringBuffer sb = new StringBuffer();
			int pageNo = 0;
			List<User> userList = fanfou.blockList(pageNo);
			List<User> allUserList = new ArrayList<User>();
			while (!userList.isEmpty()) {
				allUserList.addAll(userList);
				userList = fanfou.blockList(++pageNo);
			}
			if (allUserList.isEmpty()) {
				sb.append("黑名单列表为空");
			} else {
				sb.append("拉入黑名单的用户有").append(allUserList.size())
						.append("个，列表如下所示：\n");
				int count = 1;
				for (User user : allUserList) {
					sb.append("NO.")
							.append(count++)
							.append("：id：")
							.append(user.getId())
							.append("，name：")
							.append(user.getName())
							.append("\n--------------------------------------\n");

				}
			}
			XMPPUtils.sendMessage(sb.toString(), email);
		} else {
			XMPPUtils.sendMessage("未绑定饭否帐号\n", email);
			return;
		}
	}
}
