package project.fantalk.mail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MailServlet extends HttpServlet {

    private static final long serialVersionUID = -6275997298578864765L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Properties props = new Properties(); 
        Session session = Session.getDefaultInstance(props, null); 
        try {
            MimeMessage message = new MimeMessage(session, request.getInputStream());
            Address[] senders=message.getFrom();
            String contentType=message.getContentType();
            Multipart content=(Multipart) message.getContent();
            for (int i = 0; i < content.getCount(); i++) {
                BodyPart part=content.getBodyPart(i);
            }      
            
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
