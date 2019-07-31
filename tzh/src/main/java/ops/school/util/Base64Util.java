package ops.school.util;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

public class Base64Util {

    /**
     * TODO:在这里你需要注意了:你需要对整个返回的String做处理，把需要的那个手机号拿出来就ok了，你可以打印一下这个返回值然后处理，因为处理方式很多，我的处理方式不一定适合大家自身的习惯，所以这里改动了一下
     */
    public static String getPhoneNumberBeanS5(String decryptData, String key, String iv) throws Exception {
        /*
         *这里你没必要非按照我的方式写，下面打代码主要是在一个自己的类中 放上decrypts5这个解密工具，工具在下方有代码
         */
        String resultMessage = Base64Util.decryptS5(decryptData,"UTF-8",key,iv);
        if (resultMessage == null){
            for (int i = 0; i < 5; i++) {
                resultMessage = Base64Util.decryptS5(decryptData,"UTF-8",key,iv);
                if (resultMessage != null){
                    break;
                }
            }
        }
        return resultMessage;
    }

    public static String decryptS5(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] raw = decoder.decode(sKey);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(decoder.decode(ivParameter));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] myendicod = decoder.decode(sSrc);
            byte[] original = cipher.doFinal(myendicod);
            String originalString = new String(original, encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
}
