package robot.arm.dao.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import robot.arm.dao.annotation.SQLType;


/**
 * 线程的局部变量，线程的私有存储(供得到数据源连接时使用)
 * 
 * @author li.li
 * 
 */
public class SQLThreadLocal {
	private static final ThreadLocal<SQLThreadLocal> local = new ThreadLocal<SQLThreadLocal>();

	private SQLThreadLocal() {
	}

	public static SQLThreadLocal get() {
		return local.get();
	}

	public static SQLThreadLocal set(String catalog, SQLType sqlType, List<String> sqls, Map<String, Object> parametersMap) {
		SQLThreadLocal l = SQLThreadLocal.get();

		if (l == null) {
			l = new SQLThreadLocal();
		}

		l.setCatalog(catalog);
		l.setSqlType(sqlType);
		l.setSqls(sqls);
		l.setParametersMap(parametersMap);

		local.set(l);

		return local.get();
	}

	public static SQLThreadLocal set(String catalog, SQLType sqlType, String sql, Map<String, Object> parametersMap) {

		return set(catalog, sqlType, Arrays.asList(sql), parametersMap);
	}

	public static void remove() {
		local.remove();
	}

	private SQLType sqlType;
	private List<String> sqls;
	private Map<String, Object> parametersMap;
	private String catalog;

	public SQLType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SQLType sqlType) {
		this.sqlType = sqlType;
	}

	public List<String> getSqls() {
		return sqls;
	}

	public void setSqls(List<String> sqls) {
		this.sqls = sqls;
	}

	public Map<String, Object> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(Map<String, Object> parametersMap) {
		this.parametersMap = parametersMap;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

}
