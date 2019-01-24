package com.thunisoft.zsfy.utils;

/**
 * 
 */

import com.thunisoft.dzfy.security.RSAUtil;
import com.thunisoft.summer.component.crypto.CryptConsts;
import com.thunisoft.summer.component.crypto.CryptUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 
 */
public class ZsfyRSAUtil {

    public static final String LOGIN_WEB_PUBLIC = "30819f300d06092a864886f70d010101050003818d003081890281810092126b06bdd5e61ad789fd137cc281aaa91febb26f687d8a94f4189647aa1ac4b71cf9d0ba5e66ba1a46032cc4e0c6444c125268470b6ac28157f7f507f60a858d95f733bcb85fc2cbe4ab3cd3e6b9d523bbbd41810e73400c79da5b52a3d091e3e219322d7baf89de2245f3c69bd11af759cec50e0d4837d9c5453490830cf70203010001";

    private static final String SYSTEM_ID = "zsfy";

    private static final String SECURITY_CODE = "iTf9oHN9s19u";
    /**
     * 私钥串
     */
    public static final String SGIN = "THUNISOFT_WSFY_9527_MMP";
    /**
     * 此实例只可创建一个，否则会有内存泄露
     */
    public static final BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
    public static String encrypt(PublicKey pubKey, String source) throws UnsupportedEncodingException, Exception {
        String reversString = StringUtils.reverse(source);
        byte[] bytes = encrypt(pubKey, reversString.getBytes("utf-8"));
        return bytesToHexString(bytes);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            final int num0xFF = 0xFF;
            int v = src[i] & num0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * * 加密 *
     *
     *   加密的密钥 *
     * @param data
     *            待加密的明文数据 *
     * @return 加密后的数据 *
     * @throws Exception
     */
    public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA", bouncyCastleProvider);
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
            // 加密块大小为127
            // byte,加密后为128个byte;因此共有2个加密块，第一个127
            // byte第二个为1个byte
            int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
                }

                i++;
            }
            return raw;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static String getAuthcode () {
        String authcode = CryptUtil.encrypt(CryptConsts.ALGORITHM_AES, SYSTEM_ID, SECURITY_CODE);
        authcode = DigestUtils.md5Hex(authcode);
        return authcode;
    }

    public static String decrypt(PrivateKey privateKey, String mi) {
        String result = "";
        if (StringUtils.isBlank(mi)) {
            return mi;
        }
        byte[] en_result = hexStringToBytes(mi);
        try {
            byte[] de_result = RSAUtil.decrypt(privateKey, en_result);
            StringBuffer sb = new StringBuffer();
            sb.append(new String(de_result));
            result = sb.reverse().toString();
            result = URLDecoder.decode(result, "UTF-8");
            return result;
        } catch (Exception e) {
//            logger.error("解码出错！", e);
        }
        return "";
    }

    /**
     * 16进制 To byte[]
     *
     * @param hexString
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << ZsfyNumberConsts.NUM_4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c
     *            char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
