package robot.arm.dao.core.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 结果集Bean
 * 
 * @author li.li
 * 
 */
public class UpdateResult {
	private int count;// 更新的数量
	private List<Long> list;// 插入的id

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Long> ids() {
		if (list == null)
			list = Collections.emptyList();

		return list;
	}

	public void addId(Long id) {
		if (list == null)
			list = new ArrayList<Long>();

		list.add(id);
	}

	public static UpdateResult create() {
		return new UpdateResult();
	}

}
