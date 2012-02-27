package project.fantalk.api.fanfou;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.oauth.FanFouServiceOAuth;
import project.fantalk.model.Member;

public class FanfouServiceFactory {
	public static FanfouService newFanFouService(Member m) {
		if (m == null)
			return null;
		FanfouService fanfou = null;
		if (!Utils.isEmpty(m.getFanfouUserName())) {
			fanfou = new FanFouServiceOAuth(m);
		}
		return fanfou;
	}
}
