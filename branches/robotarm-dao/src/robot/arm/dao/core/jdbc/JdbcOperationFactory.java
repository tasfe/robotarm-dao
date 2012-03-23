package robot.arm.dao.core.jdbc;

import robot.arm.dao.core.DataAccess;
import robot.arm.dao.core.JdbcOperation;
import robot.arm.dao.core.MethodDefinition;


public interface JdbcOperationFactory {
	JdbcOperation createOperation(DataAccess dataAccess, MethodDefinition methodDefinition);
}
