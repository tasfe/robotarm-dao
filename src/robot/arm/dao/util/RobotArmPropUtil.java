package robot.arm.dao.util;

import java.util.ResourceBundle;

public class RobotArmPropUtil {
	private static final String ROBOTARM = "robotarm";
	private static final ResourceBundle bundle;

	static {
		bundle = ResourceBundle.getBundle(ROBOTARM);
	}

	public static String getPropValue(String key) {

		return bundle.getString(key);
	}

	public static void main(String[] args) {
		System.out.println(RobotArmPropUtil.getPropValue("db-config"));
	}

}
