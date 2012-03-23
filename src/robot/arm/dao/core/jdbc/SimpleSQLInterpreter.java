package robot.arm.dao.core.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mail.dao.Mail;
import robot.arm.dao.core.MethodDefinition;
import robot.arm.dao.exql.ExqlPattern;
import robot.arm.dao.exql.impl.ExqlContextImpl;
import robot.arm.dao.exql.impl.ExqlPatternImpl;

public class SimpleSQLInterpreter implements SQLInterpreter {

	@Override
	public SQLInterpreterResult interpret(final String sql, final Map<String, Object> parametersAsMap, final MethodDefinition methodDefinition) {
		ExqlPattern pattern = ExqlPatternImpl.compile(sql);
		ExqlContextImpl context = new ExqlContextImpl();

		try {
			pattern.execute(context, parametersAsMap, null);

		} catch (Exception e) {
			throw new AssertionError(e);
		}

		return context;

	}

	public static void main(String[] args) throws Exception {
		// 转换语句中的表达式
		String sql = "insert ignore into table_name " + "(`mailUser`,`mailSmtp`,`mailPassword`) " + "values (:1.mailUser,:1.mailUser,now())";
		ExqlPattern pattern = ExqlPatternImpl.compile(sql);
		ExqlContextImpl context = new ExqlContextImpl();
		
		List<Mail> mails=new ArrayList<Mail>();
		Mail mail = new Mail();
		mail.setMailPassword("passwordhe");
		mail.setMailSmtp("rr");
		mail.setMailUser("sunnymoon");
		mails.add(mail);
		mails.add(mail);
		mails.add(mail);

		Map parametersAsMap = new HashMap();
		parametersAsMap.put(":1", mails);
		// parametersAsMap.put(":2", 123);

		String result = pattern.execute(context, parametersAsMap);
		System.out.println(result);
	}

}
