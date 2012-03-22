package robot.arm.dao.core.jdbc;

import java.util.Map;

import robot.arm.dao.core.MethodDefinition;

public interface SQLInterpreter {
	
	/**
	 * 解析SQL,得到JDBC PreparedStatement需要的标准SQL和参数
	 * 
	 * @param sql
	 * @param parametersAsMap
	 * @param methodDefinition
	 * @return
	 */
	SQLInterpreterResult interpret(String sql, Map<String, Object> parametersAsMap, MethodDefinition methodDefinition);
}
