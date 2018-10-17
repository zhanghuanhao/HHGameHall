/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package account;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
/**
 * @author Zhh
 */
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypt {

	private static final String ALGORITHM = "AES";
	private static SecretKey secretKey = readKey();
	/**
	 * 生成密钥
	 * 
	 * @param seed
	 *            设置加密用的种子，密钥
	 */
	public static SecretKey geneKey() throws Exception {
		String seed = "254892464254892464254892464254892464254892464";
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		SecureRandom random = new SecureRandom();
		random.setSeed(seed.getBytes());
		keyGenerator.init(random);
		SecretKey secretKey = keyGenerator.generateKey();
		// 把上面的密钥存起来
		Path keyPath = Paths.get("Log/aes.key");
		Files.write(keyPath, secretKey.getEncoded());
		return secretKey;
	}

	/**
	 * 读取存储的密钥
	 */

	public static SecretKey readKey() {
		// 读取存起来的密钥
		try {
			Path keyPath = Paths.get("Log/aes.key");
			byte[] keyBytes = Files.readAllBytes(keyPath);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
			return keySpec;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 加密测试
	 * 
	 * @param content
	 *            需要加密的内容
	 */
	public static String Encrypt(String content) throws Exception {
		// 1、指定算法、获取Cipher对象
		Cipher cipher = Cipher.getInstance(ALGORITHM);// 算法是AES
		// 2、生成/读取用于加解密的密钥
		if (secretKey == null)
			return content;
		// 3、用指定的密钥初始化Cipher对象，指定是加密模式，还是解密模式
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		// 4、更新需要加密的内容
		cipher.update(content.getBytes());
		// 5、进行最终的加解密操作
		byte[] result = cipher.doFinal();// 加密后的字节数组
		// 也可以把4、5步组合到一起，但是如果保留了4步，同时又是如下这样使用的话，加密的内容将是之前update传递的内容和doFinal传递的内容的和。
		// byte[] result = cipher.doFinal(content.getBytes());
		String base64Result = Base64.getEncoder().encodeToString(result);// 对加密后的字节数组进行Base64编码
		return base64Result;
	}

	/**
	 * 解密测试
	 * 
	 * @param content
	 *            经过Base64加密的待解密的内容
	 */
	public static String Decrpyt(String content) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		SecretKey secretKey = readKey();
		if (secretKey == null)
			return content;
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] encodedBytes = Base64.getDecoder().decode(content.getBytes());
		byte[] result = cipher.doFinal(encodedBytes);// 对加密后的字节数组进行解密
		return new String(result);
	}
	
	public static void main(String[] args) throws Exception
	{
		File file = new File("Log/administrator");
		String pass = AESEncrypt.Encrypt("admin&123456");
		FileOutputStream fos = new FileOutputStream(file);
		PrintWriter pw = new PrintWriter(fos);
		pw.println(pass);
		pw.close();
	}
}