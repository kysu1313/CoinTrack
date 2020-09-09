/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coinClasses;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Kyle
 */
public class RecoveryEmail {
    
    public RecoveryEmail(String _toEmail, String _uname, String _code) {
        //eppfiabpywnqnvwp is a single app password
        final String username = "cointrack13@gmail.com";
        final String password = "eppfiabpywnqnvwp";
        // Create the properties header for the gmail SMTP server
        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {  //eppfiabpywnqnvwp
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("cointrack13@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(_toEmail));
            message.setSubject("Coin Track Recovery Code");
            message.setText("Dear " + _uname + ", \n\n"
                            + "Stop being so forgetful... \n\n"
                            + "Anyways, heres the code you need: \n\n" 
                            + "====>   " + _code);
            Transport.send(message);
            System.out.println("Recovery email sent");
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }
    
}
