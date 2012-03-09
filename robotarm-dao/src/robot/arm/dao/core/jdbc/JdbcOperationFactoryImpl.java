package robot.arm.dao.core.jdbc;

import java.util.regex.Pattern;

import robot.arm.dao.annotation.SQL;
import robot.arm.dao.annotation.SQLType;
import robot.arm.dao.core.DataAccess;
import robot.arm.dao.core.JdbcOperation;
import robot.arm.dao.core.MethodDefinition;
import robot.arm.dao.core.SelectOperation;
import robot.arm.dao.core.UpdateOperation;


public class JdbcOperationFactoryImpl implements JdbcOperationFactory {

	private static Pattern[] SELECT_PATTERNS = new Pattern[] {
			//
			Pattern.compile("^\\s*SELECT\\s+", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^\\s*SHOW\\s+", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^\\s*DESC\\s+", Pattern.CASE_INSENSITIVE), //
			Pattern.compile("^\\s*DESCRIBE\\s+", Pattern.CASE_INSENSITIVE), //
	};

	@Override
	public JdbcOperation createOperation(DataAccess dataAccess, MethodDefinition methodDefinition) {

		SQL sql = methodDefinition.getMethod().getAnnotation(SQL.class);
		String sqlValue = sql.value();
		SQLType sqlType = sql.type();

		// 判断SQL类型，决定是否使用select
		for (int i = 0; i < SELECT_PATTERNS.length; i++) {
			if (SELECT_PATTERNS[i].matcher(sqlValue).find()) {
				sqlType = SQLType.READ;
				break;
			}
		}

		if (SQLType.READ == sqlType) {

			return new SelectOperation(dataAccess, sqlValue, methodDefinition);
		} else if (SQLType.WRITE == sqlType) {

			return new UpdateOperation(dataAccess, sqlValue, methodDefinition);
		} else {

			throw new AssertionError("未知的SQL操作类型:" + sqlType);
		}

	}

}
