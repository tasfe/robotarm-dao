package robot.arm.dao.exql.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mail.dao.Mail;

import org.apache.log4j.Logger;

import robot.arm.dao.exql.ExprResolver;
import robot.arm.dao.exql.ExqlContext;
import robot.arm.dao.exql.ExqlPattern;
import robot.arm.dao.exql.ExqlUnit;

/**
 * 实现语句的执行接口。
 * 
 * @author han.liao
 */
public class ExqlPatternImpl implements ExqlPattern {
	private static final Logger log = Logger.getLogger(ExqlPatternImpl.class);

	// 语句的缓存
	private static final ConcurrentHashMap<String, ExqlPattern> cache = new ConcurrentHashMap<String, ExqlPattern>();

	// 编译的语句
	protected final String pattern;

	// 输出的单元
	protected final ExqlUnit unit;

	/**
	 * 构造语句的执行接口。
	 * 
	 * @param pattern
	 *            - 编译的语句
	 * @param unit
	 *            - 输出的单元
	 */
	protected ExqlPatternImpl(String pattern, ExqlUnit unit) {
		this.pattern = pattern;
		this.unit = unit;
	}

	/**
	 * 从语句编译: ExqlPattern 对象。
	 * 
	 * @param pattern
	 *            - 待编译的语句
	 * 
	 * @return ExqlPattern 对象
	 */
	public static ExqlPattern compile(String pattern) {

		// 从缓存中获取编译好的语句
		ExqlPattern compiledPattern = cache.get(pattern);
		if (compiledPattern == null) {

			log.debug("\n	EXQL pattern compiling:\n	pattern: " + pattern);

			// 重新编译语句
			ExqlCompiler compiler = new ExqlCompiler(pattern);
			compiledPattern = compiler.compile();

			// 语句的缓存
			cache.putIfAbsent(pattern, compiledPattern);
		}

		return compiledPattern;
	}

	@Override
	public String execute(ExqlContext context, Map<String, ?> map) throws Exception {

		// 执行转换
		return execute(context, new ExprResolverImpl(map));
	}

	@Override
	public String execute(ExqlContext context, Map<String, ?> mapVars, Map<String, ?> mapConsts) throws Exception {

		// 执行转换
		return execute(context, new ExprResolverImpl(mapVars, mapConsts));
	}

	// 执行转换
	protected String execute(ExqlContext context, ExprResolver exprResolver) throws Exception {
		// 转换语句内容
		unit.fill(context, exprResolver);

		String flushOut = context.flushOut();

		// 输出日志
		log.debug("\n	EXQL pattern executing:\n	origin: " + pattern + "\n	result: " + flushOut + "\n	params: " + Arrays.toString(context.getParams()));

		return flushOut;
	}

	// 进行简单测试
	public static void main(String... args) throws Exception {

		// 编译下列语句
		ExqlPattern pattern = ExqlPatternImpl.compile("SELECT :expr1, :expr2.class.name," + " ##(:expr3) WHERE #if(:expr4) {e = :expr4} #else {e IS NULL}"
				+ "#for(variant in :expr5.bytes) { AND c = :variant}" // NL
				+ " GROUP BY #!(:expr1) ASC");

		ExqlContext context = new ExqlContextImpl();

		HashMap<String, Object> map = new HashMap<String, Object>();

		List<Mail> mails = new ArrayList<Mail>();
		Mail mail = new Mail();
		mail.setMailPassword("passwordhe");
		mail.setMailSmtp("rr");
		mail.setMailUser("sunnymoon");
		mails.add(mail);
		mails.add(mail);
		mails.add(mail);

		map.put("expr1", mails);
		map.put("expr2", "b");
		map.put("expr3", "c");
		map.put("expr4", "d");
		map.put("expr5", "e");

		System.out.println("@@@@@@@@@@@" + pattern.execute(context, map));
		System.out.println("@@@@@@@@@@@" + Arrays.toString(context.getParams()));
	}
}
