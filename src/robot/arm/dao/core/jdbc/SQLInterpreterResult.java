package robot.arm.dao.core.jdbc;

public interface SQLInterpreterResult {

    String getSQL();

    Object[] getParameters();
}
