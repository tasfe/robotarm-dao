package robot.arm.dao.exql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import mail.dao.Mail;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.junit.Test;

import robot.arm.dao.exql.impl.ExqlContextImpl;
import robot.arm.dao.exql.impl.ExqlPatternImpl;

public class ExqlPatternTest {
	private String sql;
	private Map<String, Object> parametersAsMap;

	public void test() {
		sql = "select * from mail where id in (:1)";
		parametersAsMap = new HashMap<String, Object>();
		List<Long> ids = Arrays.asList(139l, 1234l);
		parametersAsMap.put(":1", ids);

		ExqlPattern pattern = ExqlPatternImpl.compile(sql);
		ExqlContextImpl context = new ExqlContextImpl();

		try {
			String result = pattern.execute(context, parametersAsMap, null);
			Assert.assertTrue(result != null && result.length() > 0);

		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	@Test
	public void test2() {
		sql = "insert into mail (mail_user,mail_password,mail_smtp) values ('l', '434', :1.mailSmtp)";
		parametersAsMap = new HashMap<String, Object>();

		List<Mail> mails = new ArrayList<Mail>();
		Mail mail = new Mail();
		mail.setMailPassword("passwordhe");
		mail.setMailSmtp("rr");
		mail.setMailUser("sunnymoon");
		mails.add(mail);

		parametersAsMap.put(":1", mails);

		ExqlPattern pattern = ExqlPatternImpl.compile(sql);
		ExqlContextImpl context = new ExqlContextImpl();

		try {
			String result = pattern.execute(context, parametersAsMap, null);
			Assert.assertTrue(result != null && result.length() > 0);

		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	public static void main(String[] args) {
		try {
			JexlContext context = new MapContext();
			Map<String, Object> mapVars = new HashMap<String, Object>();
			context.set("_mapVars", mapVars);

			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put(":1", 123);

			mapVars.putAll(paramsMap);
			
			JexlEngine jexl = new JexlEngine();
			Expression express = jexl.createExpression("_mapVars[':1']");
			Object o = express.evaluate(context);
			System.out.println(o);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
