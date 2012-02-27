package project.fantalk.mail;

import com.google.common.base.Preconditions;

/**
 * Messase类，表示一条收到的XMPP消息，包含：内容，发送者，发送者邮件地址，消息类型
 * @author mcxiaoke
 */
public class Mail {

    private Mail(Builder builder) {
        this.sender = Preconditions.checkNotNull(builder.sender);
        this.receiver = Preconditions.checkNotNull(builder.receiver);
        this.subject = Preconditions.checkNotNull(builder.subject);
        this.content = Preconditions.checkNotNull(builder.content);
    }

    public final String sender;
    public final String receiver;
    public final String subject;
    public final String content;

    public static class Builder {
        private String sender;
        private String receiver;
        private String subject;
        private String content;
        
        public Builder setSender(String sender){
            this.sender=sender;
            return this;
        }
        
        public Builder setReceiver(String receiver){
            this.receiver=receiver;
            return this;
        }
        
        public Builder setSubject(String subject){
            this.subject=subject;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Mail build() {
            return new Mail(this);
        }
    }

    @Override
    public String toString() {
        return "";
    }

}
