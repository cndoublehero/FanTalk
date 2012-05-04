package project.fantalk.command;

import java.util.List;
import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.api.fanfou.domain.Status;
import project.fantalk.api.fanfou.domain.Trends;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class HotCommand extends BaseCommand {

	public HotCommand() {
		super("hot", "hotmessage", "trend");
	}

	@Override
	public void doCommand(Message message, String content) {
		String email = message.email;
		Member m = (Member) Datastore.getInstance().getAndCacheMember(email);
		FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
		if (fanfou != null) {
			List<Trends> list = fanfou.trends();
			StringBuffer sb = new StringBuffer();
			sb.append("foufou 热词有：");
			
			for (int i = 0; i < list.size(); i++) {
				sb.append(i).append(" ").append(list.get(i).getName()).append("   ");
			}
			sb.append("\n").append(" 使用 -hot index(热词序号) 查看对应消息列表\n");
			if (!Utils.isEmpty(content)) {
				int number = Utils.toInt(content);
				if (number < 0 || number >= list.size()) {
					XMPPUtils.sendMessage("查看饭否热词格式错误，参数2取值范围为0-5\n", email);
					return;
				}
				String keyword = list.get(number).getQueryWord();
				List<Status> statusList = fanfou.search(keyword);
				sb.append("搜索结果如下：\n ");
				sb.append(processStatuses(m, statusList));
			}
			XMPPUtils.sendMessage(sb.toString(), email);
		} else {
			XMPPUtils.sendMessage("未绑定饭否帐号\n", email);
			return;
		}
	}

	public String documentation() {
		return "-hot index\n  功能说明：查看饭否热词。用法：-hot index(热词序号)，查看fanfou热词对应的查询结果";
	}

	public static StringBuilder processStatuses(Member m, List<Status> statuses) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < statuses.size(); i++) {
			Status s = statuses.get(i);
			String name = s.getUserName();
			String text = Utils.encodeHtmlToText(s.getText());
			String time = Utils.getInterval(s.getCreatedAt());
			sb.append(name).append("：").append(text).append("    [Time:")
					.append(time).append(" ]\n\n");
		}
		if (!Utils.isEmpty(sb.toString())) {
			sb.append("------------------------------\n");
		}
		return sb;
	}

}
