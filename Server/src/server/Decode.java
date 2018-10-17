/**
 * @author 张桓皓
 * @Time 2018-10-17
 *
 */
package server;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

public class Decode {
	private static PrivateKey privatekey;
	private static final int KEY_SIZE = 2048;
	private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	private static final String KEY_ALGORITHM = "RSA";
	private static RSAPublicKey publicKey;
	private static Cipher cipher;

	public Decode() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			privatekey = factory.generatePrivate(pkcs8EncodedKeySpec);
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privatekey);
		} catch (Exception e) {
		}
	}

	String decode(byte[] msg) {
		try {
			return new String(cipher.doFinal(msg));
		} catch (Exception e) {
		}
		return null;
	}

	byte[] getpubkey() throws UnsupportedEncodingException {
		return publicKey.getEncoded();
	}

}
