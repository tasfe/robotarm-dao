package robot.arm.dao.datasource;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
	private String name;
	private List<DB> dbs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DB> getDbs() {
		return dbs;
	}

	public void setDbs(List<DB> dbs) {

		this.dbs = dbs;
	}
	
	public void addDb(DB db){
		if (dbs == null) {
			dbs = new ArrayList<DB>();
		}
		
		dbs.add(db);
	}

}
