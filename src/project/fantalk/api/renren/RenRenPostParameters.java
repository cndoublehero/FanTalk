package project.fantalk.api.renren;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import project.fantalk.api.common.oauth.PostParameter;


public class RenRenPostParameters {
	private String secretKey;
	private List<PostParameter> list;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public RenRenPostParameters(String secretKey) {
		this.secretKey = secretKey;
		list = new ArrayList<PostParameter>();
	}

	public RenRenPostParameters addParameter(PostParameter parameter) {
		list.add(parameter);
		return this;
	}

	@SuppressWarnings("unchecked")
	public String generateUrl() {
		StringBuilder sb = new StringBuilder();
		Collections.sort(list);
		StringBuilder url = new StringBuilder();
		for (PostParameter parameter : list) {
			sb.append(parameter.getName()).append("=").append(
					parameter.getValue());
			url.append(parameter.getName()).append("=").append(
					parameter.getValue()).append("&");
		}
		sb.append(secretKey);
		String sigValue = md5(sb.toString());
		url.append("sig=").append(sigValue);
		return url.toString();
	}

	private String md5(String str) {
		return DigestUtils.md5Hex(str);
	}

	public static void main(String[] args) {
		System.out
				.println(DigestUtils
						.md5Hex("api_key=ec9e57913c5b42b282ab7b743559e1b0call_id=1232095295656method=xiaonei.users.getLoggedInUsersession_key=L6Xe8dXVGISZ17LJy7GzZaeYGpeGfeNdqEPLNUtCJfxPCxCRLWT83x+s/Ur94PqP-700001044v=1.07fbf9791036749cb82e74efd62e9eb38"));
		// expect value : 66f332c08191b8a5dd3477d36f3af49f
		PostParameter a1 = new PostParameter("v", "1.0");
		PostParameter a2 = new PostParameter("api_key",
				"ec9e57913c5b42b282ab7b743559e1b0");
		PostParameter a3 = new PostParameter("method",
				"xiaonei.users.getLoggedInUser");
		PostParameter a4 = new PostParameter("call_id", "1232095295656");
		PostParameter a5 = new PostParameter("session_key",
				"L6Xe8dXVGISZ17LJy7GzZaeYGpeGfeNdqEPLNUtCJfxPCxCRLWT83x+s/Ur94PqP-700001044");
		RenRenPostParameters ps = new RenRenPostParameters(
				"7fbf9791036749cb82e74efd62e9eb38");
		ps.addParameter(a1);
		ps.addParameter(a2);
		ps.addParameter(a3);
		ps.addParameter(a4);
		ps.addParameter(a5);
		System.out.println(ps.generateUrl());
		// Collections.sort(ps.list);
	}
}
