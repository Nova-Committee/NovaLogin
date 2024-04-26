package committee.nova.mods.novalogin.utils;

import committee.nova.mods.novalogin.Const;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * MailUtils
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/25 上午12:10
 */
public class MailUtils {
    public static record MailProperties(String protocol, String host, int port, boolean auth, boolean ssl, String username, String password) {}


    public static void mailSend(MailProperties mailProperties, String title, Path html, String toAddress, String code){
        try {
            Properties properties = new Properties();
            properties.put("mail.transport.protocol", mailProperties.protocol);
            properties.put("mail.smtp.host", mailProperties.host);
            properties.put("mail.smtp.port", mailProperties.port);
            properties.put("mail.smtp.auth", mailProperties.auth);
            properties.put("mail.smtp.ssl.enable", mailProperties.ssl);
            //javaEmail Debug
            properties.put("mail.debug", "false");
            Session session = Session.getInstance(properties);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailProperties.username));
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(toAddress)});
            message.setSubject(title);

            // 读取HTML文件内容
            String htmlContent = new String(Files.readAllBytes(html));

            // 设置验证码
            htmlContent = htmlContent.replace("%Email_Code%", code);

            // 设置 HTML 内容
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport transport = session.getTransport();
            transport.connect(mailProperties.username, mailProperties.password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception var5) {
            Const.LOGGER.error("SMTP服务器无法连接，或者邮件无法发送");
        }
    }
}
