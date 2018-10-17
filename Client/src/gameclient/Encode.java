/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gameclient;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encode {
	private static final String CIPHER_ALGORITHM ="RSA/ECB/PKCS1Padding";
	private static PublicKey publickey;
	private static byte[]data;
	public static final String KEY_ALGORITHM = "RSA";
	public Encode(byte[] key,String msg) throws UnsupportedEncodingException {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
	     try {
	         KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
	         publickey = factory.generatePublic(x509EncodedKeySpec);
	     } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	         e.printStackTrace();
	     }
		
		data=msg.getBytes();
	}
	//加密
public byte[] encode() {

	        try {
	            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
	            cipher.init(Cipher.ENCRYPT_MODE, publickey);
	            return cipher.doFinal(data);
	        } catch (NoSuchAlgorithmException | NoSuchPaddingException
	                | InvalidKeyException | IllegalBlockSizeException
	                | BadPaddingException e) {
	            e.printStackTrace();
	        } 
	        return null;

	    }

}
