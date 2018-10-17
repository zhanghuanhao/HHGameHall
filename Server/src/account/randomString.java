/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package account;
/**
 * @author Zhh
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class randomString {
	private static int getRandom(int count) {
		return (int) (Math.round(Math.random() * (count)));
	}
	private static String string = "abcdefghijklmnopqrstuvwxyz";
	private static int id = 0;
	private static String random(int length) {
		StringBuffer sb = new StringBuffer();
		int len = string.length();
		for (int i = 0; i < length; i++) {
			sb.append(string.charAt(getRandom(len - 1)));
		}
		sb.append('$');
		String nickname = "id" + id;
		id++;
		sb.append(nickname);
		sb.append("/密保/密码");
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		File file = new File("res/randomStirng.txt");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
		long MAX = 10000;
		for (long i = 0; i < MAX; i++) {
			bw.write(random(10));
			bw.flush();
			bw.newLine();
		}
		bw.close();
	}
}
