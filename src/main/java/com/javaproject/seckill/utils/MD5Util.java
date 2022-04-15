package com.javaproject.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;



@Component
public class MD5Util {

    private static final String salt = "1a2b3c4d";

    public static String md5(String src){

        return DigestUtils.md5Hex(src);

    }

    public static String fisrtEncode(String input){

        String str = ""+salt.charAt(0) + salt.charAt(2) + input + salt.charAt(5) + salt.charAt(4);

        return md5(str);

    }

    public static String secondEncode(String input, String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + input + salt.charAt(5) + salt.charAt(4);

        return md5(str);
    }

   public static String getDBPassword(String input, String salt){

       String firencode = fisrtEncode(input);
       String secencode = secondEncode(firencode, salt);

       return secencode;
   }

    public static void main(String[] args) {
        System.out.println(fisrtEncode("123456"));
        System.out.println(secondEncode(fisrtEncode("123456"), salt));
        System.out.println(getDBPassword("123456", salt));
    }


}
