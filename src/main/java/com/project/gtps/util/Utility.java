package com.project.gtps.util;

import com.project.gtps.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by suresh on 1/9/17.
 */
public class Utility {

    public static final Logger logger = LoggerFactory.getLogger(Utility.class);

    public static String generateResetCode(String username) {

        System.out.println("generating....");
//        try {

        System.out.println("generating reset code...");
        String codeString = "" + Math.abs((username + System.currentTimeMillis()).hashCode());

        return codeString.substring(0, 6);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return "--NOT GENERATED--";
//        }

    }

    /**
     * Sends password reset info to requesting client email
     *
     * @param username  username to whom email is to send
     * @param resetCode code for password reset
     * @param emaiTo    email address of user
     * @return true if email is sent false otherwise
     */
    public static boolean sendResetCode(String username, String resetCode, String emaiTo) {
        try {

            String host = Main.settings.getProperty("smtp_host").trim();
            String port = Main.settings.getProperty("smtp_port").trim();
            String from = Main.settings.getProperty("smtp_user").trim();
            String pass = Main.settings.getProperty("smtp_pass").trim();
            String secure = Main.settings.getProperty("smtp_secure").trim();
            String auth = Main.settings.getProperty("smtp_auth").trim();

            System.out.println(from + "\n" + host + "\n" + pass + "\n" + secure + "\n" + auth);

            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", secure);
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.password", pass);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", auth);

            String[] to = {emaiTo};

            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, Main.settings.getProperty("smtp_from")));

            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            message.setSubject("[GTPS] Password Reset Code");

            //read message body from text file in server

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(Main.settings.getProperty("template_password_reset")));
            try {
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                sb.toString();
            } catch (IOException ex) {
                logger.error("Exception: {} ", ex.getMessage());
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    logger.error("Exception: {} ", ex.getMessage());
                }
            }
            String messageBody = sb.toString().replace("{username}", username).replace("{code}", resetCode);
            message.setText(messageBody);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;

        } catch (AddressException ex) {
            logger.error("AddressException: {} ", ex);
        } catch (MessagingException ex) {
            logger.error("MessagingException: {} ", ex);
        } catch (FileNotFoundException ex) {
            logger.error("FileNotFoundException: {} ", ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            logger.error("UnsupportedEncodingException: {} ", ex.getMessage());
        }
        return false;
    }

    public static long getResetCodeAge(Date toDate, Date fromDate) {
        return toDate.getTime() - fromDate.getTime();
    }

    public String getDummyCode() {
        return "abcdefg";
    }
}
