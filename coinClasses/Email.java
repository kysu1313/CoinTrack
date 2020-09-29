package coinClasses;

/**
 * Simple program that sends an email containing a code used to reset a
 * password.
 *
 * This uses the free gmail SMTP server.
 *
 * - Kyle
 */
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    //eppfiabpywnqnvwp is a single app password
    private final String username = "cointrack13@gmail.com";
    private final String password = "eppfiabpywnqnvwp";
    // Create the properties header for the gmail SMTP server
    Properties prop = new Properties();

    /**
     * Constructor. - Makes a connection to SMTP server. - Creates an email. -
     * Authenticates it. - Sends the email to the recipient containing recovery
     * code.
     *
     * @param _toEmail
     * @param _uname
     * @param _code
     */
    public Email(String _toEmail, String _uname, String _code) {
        connecting();
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

    /**
     * Constructor. - Makes a connection to SMTP server. - Creates an email. -
     * Authenticates it. - Sends the email to the recipient containing welcome
     * message.
     *
     * @param _toEmail
     * @param _uname
     */
    public Email(String _toEmail, String _uname) {
        connecting();
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
            message.setSubject("Coin Track Recovery Username");
            message.setText("Dear " + _uname + ", \n\n"
                    + "Stop being so forgetful... \n\n"
                    + "Anyways, heres your username: \n\n"
                    + "====>   " + _uname);
            Transport.send(message);
            System.out.println("Recovery email sent");
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    /**
     * A method to make a connection
     */
    private void connecting() {
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

}
