package py.com.sodep.mobileforms.license.crypto;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Helper methods to do Public Key (asymmetric Encryption)
 * 
 * @author jmpr
 * 
 */
public class CryptoUtils {

	private static final String ALGORITHM = "RSA";

	private static final int KEY_SIZE = 1024;

	private static final String CHARSET = "UTF-8";

	/**
	 * Generates a new Key Pair
	 * 
	 * @return new KeyPair
	 */
	public static KeyPair generate() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			keyGen.initialize(KEY_SIZE, random);
			return keyGen.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the bytes of a file saved in "Hex String format" (an Hex String in
	 * the first line)
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static byte[] getBytes(File file) throws FileNotFoundException, IOException {
		return getBytes(new FileInputStream(file));
	}

	/**
	 * A String in Hex format is expected to be read from the InputStream. The
	 * hex String will be converted to the bytes it represents.
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream in) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			return fromHexString(line);
		} finally {
			in.close();
		}
	}

	/**
	 * 
	 * @param hexPrivateKey
	 * @param hexPublicKey
	 * @return
	 * @throws InvalidKeySpecException
	 */
	public static KeyPair getKeyPair(String hexPrivateKey, String hexPublicKey) throws InvalidKeySpecException {
		byte[] encodedPrivatekey = fromHexString(hexPrivateKey);
		byte[] encodedPublicKey = fromHexString(hexPublicKey);

		PrivateKey privateKey = getPrivateKey(encodedPrivatekey);
		PublicKey publicKey = getPublicKey(encodedPublicKey);

		return new KeyPair(publicKey, privateKey);
	}

	/**
	 * Get the public key from the byte array
	 * 
	 * @param bytes
	 * @return
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(byte[] bytes) throws InvalidKeySpecException {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes);
			return keyFactory.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the private key from the byte array
	 * 
	 * @param bytes
	 * @return
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(byte[] bytes) throws InvalidKeySpecException {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytes);
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] encryptStr(String str, Key key) {
		try {
			return encrypt(str.getBytes(CHARSET), key);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("FATAL!, " + CHARSET + " is not supported... GAME OVER!");
		}
	}

	public static String decryptStr(byte[] encryptedBytes, Key key) {
		try {
			byte[] decryptedBytes = decrypt(encryptedBytes, key);
			return new String(decryptedBytes, CHARSET);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static byte[] encrypt(byte[] bytes, Key key) {
		try {
			byte[] encryptedBytes = blockCipher(bytes, Cipher.ENCRYPT_MODE, key);
			return encryptedBytes;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static byte[] decrypt(byte[] encryptedBytes, Key key) {
		try {
			byte[] decryptedBytes = blockCipher(encryptedBytes, Cipher.DECRYPT_MODE, key);
			return decryptedBytes;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private static byte[] blockCipher(byte[] bytes, int mode, Key key) throws IllegalBlockSizeException,
			BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(mode, key);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// if we encrypt we use 100 byte long blocks. Decryption requires 128
		// byte long blocks (because of RSA)
		// TODO WHY?
		int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;
		byte[] buffer = new byte[length];
		byte[] temp = null;

		int i = 0;
		for (; i < bytes.length; i++) {
			int mod = i % length;
			if ((mod == 0) && (i > 0)) {
				temp = cipher.doFinal(buffer);
				bos.write(temp);
				fillWithZeros(buffer);
			}
			buffer[mod] = bytes[i];
		}

		if (mode == Cipher.ENCRYPT_MODE && (i % length != 0)) {
			temp = cipher.doFinal(buffer, 0, i % length);
		} else {
			temp = cipher.doFinal(buffer);
		}
		bos.write(temp);

		return bos.toByteArray();
	}

	private static void fillWithZeros(byte[] buffer) {
		for (int j = 0; j < buffer.length; j++) {
			buffer[j] = 0;
		}
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			sb.append(String.format("%02x", b[i]));
		}
		return sb.toString();
	}

	public static byte[] fromHexString(String hexStr) {
		int strLength = hexStr.length();
		if (strLength % 2 != 0) {
			throw new RuntimeException("Invalid " + hexStr);
		}
		int byteLength = strLength / 2;
		byte[] bytes = new byte[byteLength];

		for (int i = 0; i < byteLength; i++) {
			int index = i * 2;
			String currentByte = hexStr.substring(index, index + 2);
			bytes[i] = (byte) Short.parseShort(currentByte, 16);
		}

		return bytes;
	}
}