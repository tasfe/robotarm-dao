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

		return isCJKCharacter(c) || isBasicLatin(c) || isOtherLegal(c);
	}

	private static boolean isCJKCharacter(char c) {

		return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;// CJK中文
	}

	private static boolean isBasicLatin(char c) {

		return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.BASIC_LATIN;// 拉丁
	}

	private static boolean isOtherLegal(char c) {
		return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;// 半角和全角
	}

	public static void main(String[] args) {
		// HIGH_SURROGATES:0xD800 through 0xDB7F,
		// LOW_SURROGATES: 0xDC00 through 0xDFFF
		String s1 = "我𦵑𦵑𦵑们是在𦵑这晨啊𦵑asdfasdQW%#￥！@#￥%……&*（）——+|~￥……%&*&%DFDDSf1231𦵑23";

		System.out.println(filter(s1));
	}
}
