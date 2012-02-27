package project.fantalk.api.twitter;

import java.util.logging.Logger;

import project.fantalk.api.IBaseMethod;
import project.fantalk.api.ReturnCode;
import project.fantalk.model.Member;
import winterwell.jtwitter.OAuthSignpostClient;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class TwitterService implements IBaseMethod {
    private static final Logger logger = Logger.getLogger(TwitterService.class
            .getName());
    private String accessToken;
    private String accessTokenSecret;

    public TwitterService(String username, String password) {
        this.accessToken = username;
        this.accessTokenSecret = password;
        logger.info("UserInfo: (" + accessToken + "///" + accessTokenSecret
                + ")");
    }

    public TwitterService(Member member) {
        this(member.getTwitterUsername(), member.getTwitterPassword());
    }

    public ReturnCode update(String text) {
        logger.info(" status:" + text);
        if (text.length() > 139) {
            return ReturnCode.ERROR_WORDS_LIMIT_ERROR;
        }
        ReturnCode rc = ReturnCode.ERROR_FALSE;
        OAuthSignpostClient client = new OAuthSignpostClient(
                TwitterConstant.apiKey, TwitterConstant.secret, accessToken,
                accessTokenSecret);
        Twitter twitter = new Twitter(null, client);
        try {
            twitter.updateStatus(text);
            rc = ReturnCode.ERROR_OK;
        } catch (TwitterException.Repetition e) {
            logger.warning(e.getMessage());
            rc = ReturnCode.ERROR_REPETITION;
        } catch (TwitterException e) {
            logger.warning(e.getMessage());
            rc = ReturnCode.ERROR_SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            logger.warning(e.getMessage());
            rc = ReturnCode.ERROR_WORDS_LIMIT_ERROR;
        } catch (RuntimeException e) {
            logger.warning(e.getMessage());
            rc = ReturnCode.ERROR_FALSE;
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return rc;

    }

    public ReturnCode verify() {
        ReturnCode rc = ReturnCode.ERROR_FALSE;
        OAuthSignpostClient client = new OAuthSignpostClient(
                TwitterConstant.apiKey, TwitterConstant.secret, accessToken,
                accessTokenSecret);
        Twitter twitter = new Twitter(null, client);
        try {
            if (twitter.isValidLogin()) {
                rc = ReturnCode.ERROR_OK;
            }
        } catch (TwitterException e) {
            rc = ReturnCode.ERROR_SERVER_ERROR;
        }
        return rc;
    }

    public String getBindErrorMessage() {
        return null;
    }

    public String getBindOkMessage() {
        return null;
    }

    public String notPrepareedMessage() {
        return "请访问FanTalk主页进行Twitter Oauth认证!";
    }

    public Member processMember(Member member) {
        return member;
    }
}
