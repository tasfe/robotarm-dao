package mail.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import robot.arm.dao.core.DAOFactory;
import robot.arm.dao.core.jdbc.UpdateResult;

public class MailDAOTest {
	private MailDAO mailDAO = DAOFactory.getInstance().getDAO(MailDAO.class);// 创建DAO

	@Test
	public void queryAll() throws Throwable {
		List<Mail> mails = mailDAO.queryAll();
		Assert.assertTrue((mails != null && mails.size() > 0));
	}

	@Test
	public void queryById() throws Throwable {
		List<Mail> mails = mailDAO.queryById(137);
		Assert.assertTrue((mails != null && mails.size() > 0));
	}

	@Test
	public void queryByUser() throws Throwable {
		Mail mail = new Mail();
		mail.setMailPassword("1223");
		mail.setMailSmtp("123");
		mail.setMailUser("li.li");

		List<Mail> mails = mailDAO.queryByUser(mail);
		Assert.assertTrue((mails != null && mails.size() > 0));
	}

	@Test
	public void queryInIds() throws Throwable {
		List<Long> ids = Arrays.asList(137l, 139l);
		List<Mail> mails = mailDAO.queryInIds(ids);
		Assert.assertTrue((mails != null && mails.size() > 0));
	}

	@Test
	public void addMail() throws Throwable {
		Mail mail = new Mail();
		mail.setMailPassword("12334123412341");
		mail.setMailSmtp("qq.cofa;dsflj");
		mail.setMailUser("sunnymoon");

		UpdateResult result = mailDAO.addMail(mail);
		Assert.assertTrue((result != null && result.getCount() > 0) && result.ids().size() > 0);
	}

	@Test
	public void addMails() throws Throwable {
		List<Mail> mails = new ArrayList<Mail>();
		Mail mail = new Mail();
		mail.setMailPassword("12334123412341");
		mail.setMailSmtp("qq.cofa;dsflj");
		mail.setMailUser("sunnymoon");
		mails.add(mail);
		mails.add(mail);
		mails.add(mail);

		UpdateResult result = mailDAO.addMails(mails);
		Assert.assertTrue((result != null && result.getCount() > 0) && result.ids().size() > 0);
	}

	@Test
	public void delById() throws Throwable {
		UpdateResult result = mailDAO.delById(167l);
		Assert.assertTrue((result != null && result.getCount() > 0));
		System.out.println(result.getCount());
	}

	@Test
	public void delInIds() throws Throwable {
		UpdateResult result = mailDAO.delInIds(Arrays.asList(168l, 169l));
		Assert.assertTrue((result != null && result.getCount() > 0));
		System.out.println(result.getCount());
	}

	@Test
	public void upAll() throws Throwable {
		UpdateResult result = mailDAO.upAll("li.li", "1223");
		Assert.assertTrue((result != null && result.getCount() > 0));
		System.out.println(result.getCount() + "|" + result.ids().size());
	}

}
