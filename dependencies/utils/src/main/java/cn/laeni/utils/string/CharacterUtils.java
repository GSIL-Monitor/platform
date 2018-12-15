package cn.laeni.utils.string;

import java.util.Random;

/**
 * 生成随机字符串
 * @author laeni.cn
 *
 */
public class CharacterUtils {
	
	// 由Random生成随机数
	Random random = new Random();
	
	/**
	 * 返回子随机字符串中加入了一些自定义规则:<br/>
	 * 1.去除一些不想出现的字符<br/>
	 * 2.第一个一定为字母
	 * @param length
	 * @return
	 */
	public String getRandomString(int length) {
		// 将承载的字符转换成字符串
		return this.getRandomString(length, "zxcvbnmlkjhgfdsaqwertyuiop0123456789");
	}
	
	/**
	 * 从指定字符串"mode"中随机取length此字符责成一个新字符串返回
	 * @param length
	 * @return
	 */
	public String getRandomString(int length,String mode) {
		
		char[] charArray = mode.toCharArray();

		StringBuffer sb = new StringBuffer();
		// 长度为几就循环几次
		for (int i = 0; i < length; ++i) {
			// 将产生的字符存入字符数组
			sb.append(charArray[random.nextInt(charArray.length)]);
		}
		// 将字符数组转换成字符串
		return sb.toString();
	}

}
