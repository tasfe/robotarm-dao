package robot.arm.dao.core;

import java.util.Map;

public interface JdbcOperation {
	/**
	 * 数据库操作。
	 * 
	 * @return
	 */
	public Object execute(Map<String, Object> parametersMap) throws Throwable;
}
