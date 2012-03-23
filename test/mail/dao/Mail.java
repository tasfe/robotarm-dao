package mail.dao;

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

}