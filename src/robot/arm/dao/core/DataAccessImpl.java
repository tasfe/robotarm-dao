package robot.arm.dao.core;

import java.util.List;
import java.util.Map;

import robot.arm.dao.core.jdbc.Jdbc;
import robot.arm.dao.core.jdbc.SQLInterpreter;
import robot.arm.dao.core.jdbc.SQLInterpreterResult;
import robot.arm.dao.core.jdbc.SimpleSQLInterpreter;
import robot.arm.dao.core.jdbc.UpdateResult;

/**
 * 持久层数据访问接口实现
 * 
 * @author li.li
 * 
 * @param <T>
 */
public class DataAccessImpl implements DataAccess {
	private Jdbc jdbc;
	private SQLInterpreter interpreter;

	public DataAccessImpl(Jdbc jdbc) {
		this.jdbc = jdbc;
		this.interpreter = new SimpleSQLInterpreter();

	}

	// /////////////////////////////////////////////////////////////////////////////
	/**
	 * select
	 */
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public List<?> select(String sql, Map<String, Object> parametersMap, MethodDefinition methodDefinition) throws Throwable {

		SQLInterpreterResult sqlResult = interpreter.interpret(sql, parametersMap, methodDefinition);

		return jdbc.exeQuery(sqlResult.getSQL(), sqlResult.getParameters());
	}

	// /////////////////////////////////////////////////////////////////////////////
	/**
	 * update
	 */
	// /////////////////////////////////////////////////////////////////////////////
	@Override
	public UpdateResult update(String enhanceSQL, Map<String, Object> parametersMap, MethodDefinition methodDefinition) throws Throwable {
		SQLInterpreterResult sqlResult = interpreter.interpret(enhanceSQL, parametersMap, methodDefinition);

		return jdbc.exeUpdate(sqlResult.getSQL(), sqlResult.getParameters());
	}

}
