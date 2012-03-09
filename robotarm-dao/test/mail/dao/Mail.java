package mail.dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Mail {
	private String mailUser;

	private String mailSmtp;
	private String mailPassword;

	public Mail() {
	}

	public Mail(String user, String password, String smtp) {
		this.mailUser = user;
		this.mailSmtp = password;
		this.mailPassword = smtp;
	}

	public String getMailUser() {
		return mailUser;
	}

	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}

	public String getMailSmtp() {
		return mailSmtp;
	}

	public void setMailSmtp(String mailSmtp) {
		this.mailSmtp = mailSmtp;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	/**
	 * 群发邮件,显示收件人
	 */
	public void send(String receiver, String subject, String content, boolean isSSL) throws MessagingException {
		Session session = getSession(isSSL);// 获得邮件会话对象
		MimeMessage mimeMsg = new MimeMessage(session);// 创建MIME邮件对象
		if (mailUser != null) // 设置发件人地址
			mimeMsg.setFrom(new InternetAddress(mailUser));
		if (receiver != null) // 设置收件人地址
			mimeMsg.setRecipients(Message.RecipientType.TO, parse(receiver));
		if (subject != null) // 设置邮件主题
			mimeMsg.setSubject(subject, Config.UTF8);
		MimeBodyPart part = new MimeBodyPart();// mail内容部分
		part.setText(content == null ? "" : content, Config.UTF8);

		// 设置邮件格式为html cqc
		part.setContent(content.toString(), Config.CONTENT_TYPE);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(part);// 在 Multipart 中增加mail内容部分
		mimeMsg.setContent(multipart);// 增加 Multipart 到信息体
		mimeMsg.setSentDate(new Date());// 设置发送日期
		Transport.send(mimeMsg);// 发送邮件
	}

	/**
	 * 群发邮件,不显示收件人
	 */
	public void sendIgnoreRecipients(String receiver, String subject, String content, boolean isSSL) throws MessagingException {
		Session session = getSession(isSSL);// 获得邮件会话对象
		MimeMessage mimeMsg = new MimeMessage(session);// 创建MIME邮件对象
		if (mailUser != null) // 设置发件人地址
			mimeMsg.setFrom(new InternetAddress(mailUser));
		if (subject != null) // 设置邮件主题
			mimeMsg.setSubject(subject, Config.UTF8);
		MimeBodyPart part = new MimeBodyPart();// mail内容部分
		part.setText(content == null ? "" : content, Config.UTF8);

		// 设置邮件格式为html cqc
		part.setContent(content.toString(), Config.CONTENT_TYPE);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(part);// 在 Multipart 中增加mail内容部分
		mimeMsg.setContent(multipart);// 增加 Multipart 到信息体
		mimeMsg.setSentDate(new Date());// 设置发送日期
		Transport.send(mimeMsg, parse(receiver));// 发送邮件,忽略收件人设置
	}

	/**
	 * 获得邮件会话对象
	 */
	private Session getSession(boolean isSSL) throws MessagingException {
		if (mailSmtp == null)
			throw new MessagingException("smtpHost not found");
		if (mailUser == null)
			throw new MessagingException("user not found");
		if (mailPassword == null)
			throw new MessagingException("password not found");

		Properties properties = new Properties();
		properties.put(Config.mail_smtp_host, mailSmtp);// 设置smtp主机
		properties.put(Config.mail_smtp_auth, "true");// 使用smtp身份验证

		if (isSSL)
			properties.put("mail.smtp.starttls.enable", "true");// 需要SSL验证时，需要设定

		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailUser, mailPassword);
			}
		});
		return session;
	}

	/**
	 * 解析收件人字符串,群发用 ";" 分割
	 */
	private InternetAddress[] parse(String address) throws AddressException {
		List<InternetAddress> list = new ArrayList<InternetAddress>();
		StringTokenizer tokens = new StringTokenizer(address, ";");
		while (tokens.hasMoreTokens())
			list.add(new InternetAddress(tokens.nextToken().trim()));
		InternetAddress[] addressArray = new InternetAddress[list.size()];
		list.toArray(addressArray);
		return addressArray;
	}

}