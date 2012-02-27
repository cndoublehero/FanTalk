package project.fantalk.xmpp;

import com.google.appengine.api.xmpp.JID;
import com.google.common.base.Preconditions;

/**
 * Messase类，表示一条收到的XMPP消息，包含：内容，发送者，发送者邮件地址，消息类型
 * @author mcxiaoke
 */
public class Message {

    private Message(Builder builder) {
        this.content = Preconditions.checkNotNull(builder.content);
        this.sender = Preconditions.checkNotNull(builder.sender);
        this.email = Preconditions.checkNotNull(builder.email);
        this.type = Preconditions.checkNotNull(builder.type);
    }

    /** 消息内容 */
    public final String content;
    /** 消息发送者JID */
    public final JID sender;
    /** 消息发送者Email */
    public final String email;
    /** 消息类型 */
    public final Type type;

    static enum Type {
        XMPP, MAIL, SMS,
    }

    public static class Builder {
        public String email;
        private String content;
        private JID sender;
        private Type type;

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setSender(JID sender) {
            this.sender = sender;
            this.email = sender.getId().toLowerCase().split("/")[0];
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    @Override
    public String toString() {
        return "[Message: content = '" + content + "', gmail = " + email + "]";
    }

}
