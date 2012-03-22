package robot.arm.dao.exql;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

public class JEXLTest {
	public static void main(String[] args) throws Exception {
		JexlContext context = new MapContext();
		context.set("var1", "hello");

		String express = "var1.length()";
		JexlEngine jexl = new JexlEngine();
		Expression e =jexl.createExpression(express);

		Object obj = e.evaluate(context);
		System.out.println(obj);
	}
}
