package robot.arm.dao.core;

import java.lang.reflect.Method;

import robot.arm.dao.util.BeanUtils;


public class MethodDefinition {
	private DAODefinition daoDefinition;

	private Method method;

	private Class<?>[] genericReturnTypes;
	
	public MethodDefinition(DAODefinition daoDefinition, Method method){
		this.daoDefinition=daoDefinition;
		this.method=method;
		this.genericReturnTypes = BeanUtils.getActualClass(method.getGenericReturnType());
		
	}

	public DAODefinition getDaoDefinition() {
		return daoDefinition;
	}

	public void setDaoDefinition(DAODefinition daoDefinition) {
		this.daoDefinition = daoDefinition;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Class<?>[] getGenericReturnTypes() {
		return genericReturnTypes;
	}

	public void setGenericReturnTypes(Class<?>[] genericReturnTypes) {
		this.genericReturnTypes = genericReturnTypes;
	}
	
	
}
