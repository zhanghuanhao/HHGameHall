/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package gameclient;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Decode {
	private static PrivateKey privatekey;
	public static final int KEY_SIZE = 2048;
	public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	public static final String KEY_ALGORITHM = "RSA";
	private static RSAPublicKey publicKey ;
public Decode() {
	 KeyPairGenerator keyPairGenerator;
	 try {
		keyPairGenerator = KeyPairGenerator
		         .getInstance(KEY_ALGORITHM);
		keyPairGenerator.initialize(KEY_SIZE);
     KeyPair keyPair = keyPairGenerator.generateKeyPair();
     publicKey = (RSAPublicKey) keyPair.getPublic();
     RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
     
     PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
     try {
         KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
         privatekey = factory.generatePrivate(pkcs8EncodedKeySpec);
     } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
         e.printStackTrace();
     }
     
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	}
}
//解密
public String decode(byte[]msg) {
    try {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privatekey);
        return new String(cipher.doFinal(msg));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException
            | InvalidKeyException | IllegalBlockSizeException
            | BadPaddingException e) {
        e.printStackTrace();
    }
    return null;

}
//返回byte[]形式的公钥
public byte[] getpubkey() throws UnsupportedEncodingException {
	return publicKey.getEncoded();
}

}
