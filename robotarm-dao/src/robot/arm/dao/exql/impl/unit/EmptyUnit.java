package robot.arm.dao.exql.impl.unit;


import robot.arm.dao.exql.ExprResolver;
import robot.arm.dao.exql.ExqlContext;
import robot.arm.dao.exql.ExqlUnit;

/**
 * 输出空白的语句单元, 代替空的表达式。
 * 
 */
public class EmptyUnit implements ExqlUnit {

	@Override
	public boolean isValid(ExprResolver exprResolver) {
		// Empty unit is always valid.
		return true;
	}

	@Override
	public void fill(ExqlContext exqlContext, ExprResolver exprResolver) throws Exception {
		// Do nothing.
	}
}
