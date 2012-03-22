package robot.arm.dao.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.log4j.Logger;

/**
 * 
 * JDBC具体操作相关
 * 
 * @author li.li
 * 
 * 
 */
public class JdbcImpl implements Jdbc {
	private static final Logger log = Logger.getLogger(JdbcImpl.class);

	private DataSource dataSource;

	public JdbcImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<Map<String,Object>> exeQuery(String sql, Object[] params) throws Throwable {

		return query(sql, params);
	}

	@Override
	public UpdateResult exeUpdate(String sql, Object[] params) throws Throwable {

		return update(sql, params);
	}

	// /////////////////////////////////////////////////////////////////////////////
	/**
	 * private
	 */
	// /////////////////////////////////////////////////////////////////////////////
	private List<Map<String,Object>> query(String sql, Object[] params) throws Throwable {
		log.debug("query|" + sql + "|" + Arrays.asList(params));

		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection con = getDBConnection();
		List<Map<String,Object>> result = null;
		
		try {
			pst = con.prepareStatement(sql);

			int index = 0;
			for (Object value : params) {
				pst.setObject(++index, value);
			}
			rs = pst.executeQuery();
			result = getMapsFromResultSet(rs);
		} finally {
			close(rs, pst, con);
		}

		return result;
	}

	private UpdateResult update(String sql, Object[] params) throws Throwable {
		log.debug("update|" + sql + "|" + Arrays.asList(params));
		UpdateResult rb = UpdateResult.create();

		Connection con = getDBConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			int index = 0;
			for (Object value : params) {
				pst.setObject(++index, value);
			}

			rb.setCount(pst.executeUpdate());// 数量

			rs = pst.getGeneratedKeys();
			while (rs.next()) {
				rb.addId(rs.getLong(1));// id
			}
		} finally {
			close(rs, pst, con);
		}
		return rb;
	}

	private List<Map<String, Object>> getMapsFromResultSet(ResultSet rs) throws Throwable {
		ResultSetMetaData rsmd = null;
		int colCount = 0;
		List<Map<String, Object>> resultRows = new ArrayList<Map<String, Object>>();
		try {
			rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, Object> resultRow = new HashMap<String, Object>();
				for (int i = 1; i <= colCount; ++i) {
					resultRow.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				resultRows.add(resultRow);
			}
			return resultRows;
		} finally {
		}
	}

	private Connection getDBConnection() throws Throwable {
		try {

			return dataSource.getConnection();
		} finally {
		}
	}

	private void close(ResultSet rs, PreparedStatement pst, Connection con) throws Throwable {
		close(rs);
		close(pst);
		close(con);
	}

	public void close(Wrapper wrapper) throws Throwable {
		Assert.assertNotNull("wrapper can't be null", wrapper);

		try {
			if (wrapper instanceof ResultSet)
				((ResultSet) wrapper).close();
			if (wrapper instanceof PreparedStatement)
				((PreparedStatement) wrapper).close();
			if (wrapper instanceof Connection)
				((Connection) wrapper).close();

		} finally {
			wrapper = null;
		}
	}
	
	public static void main(String[] args) {
		JdbcImpl jdbc=new JdbcImpl(null);
		try {
			jdbc.close(null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
