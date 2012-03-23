package robot.arm.dao.core;

import java.util.Map;

import robot.arm.dao.annotation.SQLType;
import robot.arm.dao.core.jdbc.UpdateResult;

public class UpdateOperation implements JdbcOperation {
	private DataAccess dataAccess;
	private String sql;
	private MethodDefinition methodDefinition;

	public UpdateOperation(DataAccess dataAccess, String sql, MethodDefinition methodDefinition) {
		this.dataAccess = dataAccess;
		this.sql = sql;
		this.methodDefinition = methodDefinition;
	}

	@Override
	public Object execute(Map<String, Object> parametersMap) throws Throwable {
		try {
			SQLThreadLocal.set(methodDefinition.getDaoDefinition().getCatalog(), SQLType.WRITE, sql, parametersMap);
			
			UpdateResult upResultBean = dataAccess.update(sql, parametersMap, methodDefinition);

			return upResultBean;
		} finally {
			if (SQLThreadLocal.get() != null)
				SQLThreadLocal.remove();
		}
	}
}
