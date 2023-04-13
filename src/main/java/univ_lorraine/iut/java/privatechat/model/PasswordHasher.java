package univ_lorraine.iut.java.privatechat.model;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class PasswordHasher {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int KEY_SIZE = 128;
    private static byte[] iv;

    public static String hashPassword(String password, String salt) throws Exception {
        SecretKeySpec secretKeySpec = generateSecretKey(password, salt.getBytes(StandardCharsets.UTF_8));
        IvParameterSpec ivParameterSpec = generateIvParameterSpec(salt.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedPasswordBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        String encryptedPassword = Base64.getEncoder().encodeToString(encryptedPasswordBytes);

        return encryptedPassword;
    }

    private static SecretKeySpec generateSecretKey(String password, byte[] salt) throws Exception {
        byte[] keyBytes = generateKeyBytes(password, salt);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        return secretKeySpec;
    }

    private static byte[] generateKeyBytes(String password, byte[] salt) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.reset();
        digest.update(salt);

        byte[] keyBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        keyBytes = copyOfRange(keyBytes, 0, KEY_SIZE / 8);

        return keyBytes;
    }

    private static IvParameterSpec generateIvParameterSpec(byte[] salt) {
        iv = new byte[16];
        System.arraycopy(salt, 0, iv, 0, Math.min(salt.length, iv.length));
        return new IvParameterSpec(iv);
    }

    public static String saveIv() {
        return new String(iv);
    }
    private static byte[] copyOfRange(byte[] src, int start, int end) {
        int length = end - start;
        byte[] dest = new byte[length];
        System.arraycopy(src, start, dest, 0, length);
        return dest;
    }
}

