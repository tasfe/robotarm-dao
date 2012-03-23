package robot.arm.dao.datasource;

import javax.sql.DataSource;

public interface DataSourceFactory {
	public DataSource getDataSource(String dsName);
}
