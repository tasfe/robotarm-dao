package robot.arm.dao.core;

import java.util.List;
import java.util.Map;

import robot.arm.dao.core.jdbc.UpdateResult;


/**
 * 持久层数据访问接口
 * 
 * @author li.li
 * 
 * @param <T>
 */
public interface DataAccess {

	public List<?> select(String sql, Map<String, Object> parametersMap,MethodDefinition methodDefinition) throws Throwable;

	public UpdateResult update(String sql, Map<String, Object> parametersMap,MethodDefinition methodDefinition) throws Throwable;

}
