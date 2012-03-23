package robot.arm.dao.core.jdbc;

import java.util.List;
import java.util.Map;


public interface Jdbc {
	public List<Map<String,Object>> exeQuery(String sql, Object[] params) throws Throwable;

	public UpdateResult exeUpdate(String sql, Object[] params) throws Throwable;
}
