package robot.arm.dao.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import robot.arm.dao.annotation.SQLType;
import robot.arm.dao.core.SQLThreadLocal;

import junit.framework.Assert;

public class MasterSlaverDataSource implements DataSource {
	private List<DataSource> masters = Collections.emptyList();

	private List<DataSource> slavers = Collections.emptyList();

	private Random random = new Random();

	public MasterSlaverDataSource(List<? extends DataSource> master, List<? extends DataSource> slavers) {
		this.masters = new ArrayList<DataSource>(master);
		this.slavers = new ArrayList<DataSource>(slavers);
	}

	@Override
	public Connection getConnection() throws SQLException {

		return getDataSource().getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return getDataSource().getConnection(username, password);
	}

	protected DataSource getDataSource() throws SQLException {
		SQLThreadLocal local = SQLThreadLocal.get();

		Assert.assertNotNull("SQLThreadLocal为空", local);

		boolean isWrite = true;

		if (local.getSqlType().equals(SQLType.READ))
			isWrite = false;

		DataSource dataSource;

		if (isWrite) {
			dataSource = randomGet(masters);
		} else {
			dataSource = randomGet(slavers);
		}

		Assert.assertNotNull("数据源为空，不能得到数据源"+ (isWrite ? "Write" : "Read"), dataSource);

		return dataSource;
	}

	protected DataSource randomGet(List<DataSource> dataSources) {
		if (dataSources.size() == 0) {
			return null;
		}
		int index = random.nextInt(dataSources.size()); // 0.. size
		return dataSources.get(index);
	}

	// ---------------------------------------
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
