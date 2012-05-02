/**
 * 
 */
package robot.arm.dao.util;

/**
 * @author li.li
 * 
 *         May 2, 2012
 * 
 *         UNICODE编码工具类
 * 
 */
public class UnicodeUtils {

	public static String filter(String str) {

		StringBuilder sb = new StringBuilder(str.length());
		for (char c : str.toCharArray()) {
			if (isLegal(c))
				sb.append(c);
		}

		return sb.toString();
	}

	public static boolean isLegal(char c) {

		return isCJKCharacter(c) || isBasicLatin(c);
	}

	private static boolean isCJKCharacter(char c) {

		return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
	}

	private static boolean isBasicLatin(char c) {

		return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.BASIC_LATIN;
	}

	public static void main(String[] args) {
		String s = "𦵑";// HIGH_SURROGATES:0xD800 through 0xDB7F,
		// LOW_SURROGATES: 0xDC00 through 0xDFFF
		String s1 = "我𦵑𦵑𦵑们是在𦵑这晨啊𦵑asdfasdf1231𦵑23";

		System.out.println(filter(s1));
	}
}
