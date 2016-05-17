package rsea.lib.util.etc;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordEncryption {

	// Length 16byte
	private static String KEY = "SHOP12345678ABCD";

	public static String encrypt(String message) throws Exception {

		// use key coss2
		SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] encrypted = cipher.doFinal(message.getBytes());
		return Base64.encodeToString(encrypted, Base64.DEFAULT);
	}

	/** 
	 * AES 방식의 복호화
	 *
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encrypted) throws Exception {

		// use key coss2
		SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
		String originalString = new String(original);
		return originalString;
	}

}
