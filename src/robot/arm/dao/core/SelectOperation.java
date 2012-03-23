package robot.arm.dao.core;

import java.util.List;
import java.util.Map;

import robot.arm.dao.annotation.SQLType;
import robot.arm.dao.util.BeanUtils;

public class SelectOperation implements JdbcOperation {
	private DataAccess dataAccess;
	private String sql;
	private MethodDefinition methodDefinition;

	public SelectOperation(DataAccess dataAccess, String sql, MethodDefinition methodDefinition) {
		this.dataAccess = dataAccess;
		this.sql = sql;
		this.methodDefinition = methodDefinition;
	}

	@Override
	public Object execute(Map<String, Object> parametersMap) throws Throwable {
		try {
			SQLThreadLocal.set(methodDefinition.getDaoDefinition().getCatalog(), SQLType.READ, sql, parametersMap);

			Class<?> returnType = methodDefinition.getGenericReturnTypes()[0];// 方法需要的返回类型

			List<?> resultList = dataAccess.select(sql, parametersMap, methodDefinition);

			return BeanUtils.toBeans(resultList, returnType);// 转成需要的类型
		} finally {
			if (SQLThreadLocal.get() != null)
				SQLThreadLocal.remove();
		}
	}

}
