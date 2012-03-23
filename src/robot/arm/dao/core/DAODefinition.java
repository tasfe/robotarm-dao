package robot.arm.dao.core;

import robot.arm.dao.annotation.DAO;

public class DAODefinition {
	private Class<?> clazz;
	private String catalog;

	public DAODefinition(Class<?> clazz) {
		this.clazz = clazz;
		this.catalog=clazz.getAnnotation(DAO.class).catalog();
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	
	

}
