package mail.dao;

import java.util.Collection;
import java.util.List;

import robot.arm.dao.annotation.DAO;
import robot.arm.dao.annotation.SQL;
import robot.arm.dao.core.jdbc.UpdateResult;

@DAO(catalog = "c1")
public interface MailDAO {
	// 查询
	@SQL(value = "select * from mail")
	public List<Mail> queryAll() throws Throwable;

	@SQL(value = "select * from mail where id=:1")
	public List<Mail> queryById(long id) throws Throwable;

	@SQL(value = "select * from mail where id in (:1)")
	public List<Mail> queryInIds(List<Long> ids) throws Throwable;

	@SQL(value = "select * from mail where mail_user = :1.mailUser and mail_smtp=:1.mailSmtp")
	public List<Mail> queryByUser(Mail mail) throws Throwable;

	// 更新
	@SQL(value = "update mail set mail_user=:1, mail_password=:2")
	public UpdateResult upAll(String mailUser, String mailPassword) throws Throwable;

	@SQL(value = "update mail set mail_user='sunnymoon', mail_password='passwordhe' where id=:1")
	public UpdateResult upById(long id) throws Throwable;

	@SQL(value = "update mail set mail_password='passwordhe' where mail_name=:1")
	public UpdateResult upByName(String name) throws Throwable;

	// 插入
	@SQL(value = "insert into mail (mail_user,mail_password,mail_smtp,mail_ssl) values ('ad','ff','dd','1')")
	public UpdateResult addMail() throws Throwable;

	@SQL(value = "insert into mail (mail_user,mail_password,mail_smtp) values (:1.mailUser, :1.mailPassword, :1.mailSmtp)")
	public UpdateResult addMail(Mail mail) throws Throwable;

	@SQL(value = "insert into mail (mail_user,mail_password,mail_smtp) values #for(mail in :1) { #if(:current){,} (:mail.mailUser, :mail.mailPassword, :mail.mailSmtp)}")
	public UpdateResult addMails(Collection<Mail> mails) throws Throwable;

	// 删除
	@SQL(value = "delete from mail where id=:1")
	public UpdateResult delById(long id) throws Throwable;

	@SQL(value = "delete from mail where id in (:1)")
	public UpdateResult delInIds(List<Long> ids) throws Throwable;

}
