package robot.arm.dao.core;

import javax.sql.DataSource;

import robot.arm.dao.core.jdbc.Jdbc;
import robot.arm.dao.core.jdbc.JdbcImpl;
import robot.arm.dao.datasource.DataSourceFactory;
import robot.arm.dao.datasource.XMLDataSourceFactory;

/**
 * robotarm DAO Factory
 * 
 * @author li.li
 * 
 */
public class DAOFactory {
	private static final DAOFactory instance = new DAOFactory();
	private DataSourceFactory dsf = XMLDataSourceFactory.getInstance();

	private DAOFactory() {
	}

	public <T> T getDAO(Class<T> daoClazz) {
		DAODefinition daoDefinition = new DAODefinition(daoClazz);

		DataSource dataSource = dsf.getDataSource(daoDefinition.getCatalog());

		if (dataSource == null) {
			throw new AssertionError("数据源不能为空");
		}

		Jdbc jdbc = new JdbcImpl(dataSource);

		DataAccess dataAccess = new DataAccessImpl(jdbc);

		T proxy = new DataAccessInvocationHandler(dataAccess, daoDefinition).getProxy();

		return proxy;
	}

	public static DAOFactory getInstance() {
		return instance;
	}

}
