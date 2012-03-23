package robot.arm.dao.util;

import java.util.Arrays;
import java.util.List;

public class StringUtils2 {
	public static String joinAsString(List<?> array, String separator) {
		StringBuilder sb = new StringBuilder(array.size());

		for (int i = 0; i < array.size(); i++) {
			sb.append("'");
			sb.append(array.get(i));
			sb.append("'");

			if (i != array.size() - 1)
				sb.append(separator);
		}

		return sb.toString();
	}
	
	public static void main(String[] args) {
		List<String> a=Arrays.asList("ggg","fds","435");
		
		System.out.println(StringUtils2.joinAsString(a, ","));
	}
}
