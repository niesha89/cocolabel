package ns.coco.cocolabel.utils;

import com.alibaba.fastjson.JSON;

import java.util.Base64;

public class ByteUtils {

    //private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToBase64(byte[] bytes) {

//        String encodedText = JSON.toJSONString(bytes);
//        return encodedText.substring(1,encodedText.length()-2);
        final Base64.Encoder encoder = Base64.getEncoder();
        //编码
        final String encodedText = encoder.encodeToString(bytes);
        return encodedText;
    }

    public static byte[] base64ToBytes(String encodedText) {

        final Base64.Decoder decoder = Base64.getDecoder();

        //解码
        return decoder.decode(encodedText);
    }

    public static void main(String[] args) {
        System.out.println(bytesToBase64("iojwe98".getBytes()));
    }
}
