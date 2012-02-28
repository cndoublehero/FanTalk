package project.fantalk.api.common.oauth;

import project.fantalk.api.common.AbstractAuth;
import project.fantalk.model.Member;

public abstract class AbstractOAuth extends AbstractAuth {

	@Override
	public String getBindErrorMessage() {
		return null;
	}

	@Override
	public String getBindOkMessage() {
		return null;
	}

	@Override
	public Member processMember(Member member) {
		return member;
	}

	public abstract String getApiKey();

	public abstract String getApiSecret();

	public AbstractOAuth(String username, String password) {
		super(username, password);
	}
}
