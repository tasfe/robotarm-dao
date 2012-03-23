package robot.arm.dao.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import robot.arm.dao.core.jdbc.JdbcOperationFactory;
import robot.arm.dao.core.jdbc.JdbcOperationFactoryImpl;
import robot.arm.dao.exql.util.ExqlUtils;

public class DataAccessInvocationHandler implements InvocationHandler {
	private static final Object[] EMPTY_PARAMS = new Object[0];
	private DataAccess dataAccess;
	private DAODefinition daoDefinition;
	private JdbcOperationFactory operationFactory = new JdbcOperationFactoryImpl();
	private volatile HashMap<Method, JdbcOperation> jdbcOperations = new HashMap<Method, JdbcOperation>();// cache

	public DataAccessInvocationHandler(DataAccess dataAccess, DAODefinition daoDefinition) {
		this.dataAccess = dataAccess;
		this.daoDefinition = daoDefinition;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {

		JdbcOperation jdbcOperatoin = getJdbcOperatoin(method);

		if (parameters == null)// 参数为空
			parameters = EMPTY_PARAMS;

		Map<String, Object> parametersMap = new HashMap<String, Object>();// map
																			// params
		for (int i = 0; i < parameters.length; i++) {
			parametersMap.put(ExqlUtils.PARAM_TAG + (i + 1), parameters[i]);
		}
		return jdbcOperatoin.execute(parametersMap);

	}

	@SuppressWarnings("unchecked")
	public <T> T getProxy() {
		Class<?>[] interfaces = { daoDefinition.getClazz() };

		return (T) Proxy.newProxyInstance(daoDefinition.getClazz().getClassLoader(), interfaces, this);
	}

	private JdbcOperation getJdbcOperatoin(Method method) {

		JdbcOperation jdbcOperatoin = jdbcOperations.get(method);

		if (jdbcOperatoin == null) {// double check
			synchronized (jdbcOperations) {
				if (jdbcOperatoin == null) {
					MethodDefinition methodDefinition = new MethodDefinition(daoDefinition, method);
					jdbcOperatoin = operationFactory.createOperation(dataAccess, methodDefinition);
					jdbcOperations.put(method, jdbcOperatoin);
				}
			}
		}

		return jdbcOperatoin;
	}

}
