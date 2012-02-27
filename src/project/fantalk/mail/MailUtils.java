package project.fantalk.mail;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import project.fantalk.api.Utils;



public final class MailUtils {
    
    private MailUtils(){}

    public static void sendMail(String receiver,String subject, String body
            ) {
        if(Utils.isEmpty(receiver)){
            return;
        }
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@google.com"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);

        }catch (Exception e) {
        }

    }
    
    public static void sendMail(Mail mail){
        
    }
}
