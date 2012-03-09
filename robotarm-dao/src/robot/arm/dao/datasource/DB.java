package robot.arm.dao.datasource;

public class DB {
	private String type;
	private String driver;
	private String url;
	private String user;
	private String password;
	private String ispooled;
	private String minconns;
	private String initconns;
	private String maxconns;
	private String maxidletime;
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIspooled() {
		return ispooled;
	}
	public void setIspooled(String ispooled) {
		this.ispooled = ispooled;
	}
	public String getMinconns() {
		return minconns;
	}
	public void setMinconns(String minconns) {
		this.minconns = minconns;
	}
	public String getInitconns() {
		return initconns;
	}
	public void setInitconns(String initconns) {
		this.initconns = initconns;
	}
	public String getMaxconns() {
		return maxconns;
	}
	public void setMaxconns(String maxconns) {
		this.maxconns = maxconns;
	}
	public String getMaxidletime() {
		return maxidletime;
	}
	public void setMaxidletime(String maxidletime) {
		this.maxidletime = maxidletime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
