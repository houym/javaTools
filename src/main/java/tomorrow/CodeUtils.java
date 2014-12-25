package tomorrow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.common.base.Throwables;

/**
 * 编码/加解密
 * @author houym
 * @version $Revision 1.0 $ 2014-12-25
 */
public final class CodeUtils {
    /**
     * 私有默认构造函数
     */
    private CodeUtils() {
    }

    /**
     * Base64编码
     * @param encodeMsg 要编码的消息
     * @return 编码后字符串
     */
    public static String base64Encode(byte[] encodeMsg) {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();     
        return encoder.encode(encodeMsg);
    }

    /**
     * Base64解码
     * @param decodeMsg 要解码字符串
     * @return 解码后信息
     */
    public static byte[] base64Decode(String decodeMsg) {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            return decoder.decodeBuffer(decodeMsg);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        return new byte[0];
    }

    /**
     * MD5 编码
     * @param msg 被编码信息
     * @return 编码后内容
     */
    public static byte[] md5(byte[] msg) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Throwables.propagate(e);
        }
        md.reset();
        md.update(msg);
        return md.digest();
    }

    
    /**
     * iv向量长度
     */
    private static final int IV_LENGTH = 8;
    /**
     * 对指定加密字节进行解密<br/>
     * 实现3DES算法,能够支持16位的密钥,以及"CBC"运算模式,"PKCS7Padding"填充模式等多种模式
     * @param keyValue 解密密钥
     * @param encryptedData 待解密的字节信息
     * @return 解密后信息
     */
    public static byte[] tripleDESDecode(String keyValue, byte[] encryptedData) {
        byte[] out = null;
        final StringBuilder transformation = new StringBuilder("DESede/CBC/PKCS7Padding");
        Security.addProvider(new BouncyCastleProvider());
        try {
            Cipher cipher = Cipher.getInstance(transformation.toString());
            Key key = new SecretKeySpec(strToBytes(keyValue), "DESede");
            AlgorithmParameterSpec params = new IvParameterSpec(new byte[CodeUtils.IV_LENGTH]);
            cipher.init(Cipher.DECRYPT_MODE, key, params);
            out = cipher.doFinal(encryptedData);
        } catch (InvalidKeyException e) {
            Throwables.propagate(e);
        } catch (InvalidAlgorithmParameterException e) {
            Throwables.propagate(e);
        } catch (NoSuchAlgorithmException e) {
            Throwables.propagate(e);
        } catch (NoSuchPaddingException e) {
            Throwables.propagate(e);
        } catch (IllegalBlockSizeException e) {
            Throwables.propagate(e);
        } catch (BadPaddingException e) {
            Throwables.propagate(e);
        }
        return out;
    }

    /**
     * 字符串转换为bytes
     * @param str 字符串
     * @return bytes
     */
    private static byte[] strToBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对指定信息进行加密
     * 实现3DES算法,能够支持16位的密钥,以及"CBC"运算模式,"PKCS7Padding"填充模式等多种模式
     * @param data 要加密的数据
     * @param keyValue 加密密钥
     * @return 加密后信息
     */
    public static byte[] tripleDESEncode(String keyValue, byte[] data) {
        byte[] out = null;
        final StringBuilder transformation = new StringBuilder("DESede/CBC/PKCS7Padding");
        Security.addProvider(new BouncyCastleProvider());
        try {
            Cipher cipher = Cipher.getInstance(transformation.toString());
            Key key = new SecretKeySpec(strToBytes(keyValue), "DESede");
            AlgorithmParameterSpec params = new IvParameterSpec(new byte[CodeUtils.IV_LENGTH]);
            cipher.init(Cipher.ENCRYPT_MODE, key, params);
            out = cipher.doFinal(data);
        } catch (IllegalBlockSizeException e) {
            Throwables.propagate(e);
        } catch (BadPaddingException e) {
            Throwables.propagate(e);
        } catch (InvalidKeyException e) {
            Throwables.propagate(e);
        } catch (InvalidAlgorithmParameterException e) {
            Throwables.propagate(e);
        } catch (NoSuchAlgorithmException e) {
            Throwables.propagate(e);
        } catch (NoSuchPaddingException e) {
            Throwables.propagate(e);
        }
        return out;
    }
}
