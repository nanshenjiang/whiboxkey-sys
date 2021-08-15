package com.scnu.whiboxkey.pksys.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


/**
 * byte流和各个类型互换
 */
public class ByteUtil {
    private static ByteBuffer buffer = ByteBuffer.allocate(8);

    /**
     * int转byte
     */
    public static byte intToByte(int x) {
        return (byte) x;
    }

    /**
     * byte转int
     */
    public static int byteToInt(byte b) {
        return b & 0xFF;
    }

    /**
     * byte[]转int
     */
    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
    public static int byteArrayToInt(byte[] b, int index){
        return   b[index+3] & 0xFF |
                (b[index+2] & 0xFF) << 8 |
                (b[index+1] & 0xFF) << 16 |
                (b[index+0] & 0xFF) << 24;
    }

    /**
     * int转byte[]
     */
    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * short转byte[]
     */
    public static void byteArrToShort(byte b[], short s, int index) {
        b[index + 1] = (byte) (s >> 8);
        b[index + 0] = (byte) (s >> 0);
    }

    /**
     * byte[]转short
     */
    public static short byteArrToShort(byte[] b, int index) {
        return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
    }

    /**
     * 16位short转byte[]
     */
    public static byte[] shortToByteArr(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * byte[]转16位short
     */
    public static short byteArrToShort(byte[] b){
        return byteArrToShort(b,0);
    }

    /**
     * long转byte[]
     */
    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    /**
     * byte[]转Long
     */
    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    /**
     * 从byte[]中抽取新的byte[]
     * @param data - 元数据
     * @param start - 开始位置
     * @param end - 结束位置
     * @return 新byte[]
     */
    public static byte[] getByteArr(byte[]data,int start ,int end){
        byte[] ret=new byte[end-start];
        for(int i=0;(start+i)<end;i++){
            ret[i]=data[start+i];
        }
        return ret;
    }

    /**
     * 流转换为byte[]
     */
    public static byte[] readInputStream(InputStream inStream) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            byte[] data = null;
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            return data;
        }catch (IOException e) {
            return null;
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * byte[]转inputstream
     */
    public static InputStream readByteArr(byte[] b){
        return new ByteArrayInputStream(b);
    }

    /**
     * byte数组内数字是否相同
     */
    public static boolean isEq(byte[] s1,byte[] s2){
        int slen=s1.length;
        if(slen==s2.length){
            for(int index=0;index<slen;index++){
                if(s1[index]!=s2[index]){
                    return false;
                }
            }
            return true;
        }
        return  false;
    }

    /**
     * byte数组转换为Stirng
     * @param s1 -数组
     * @param encode -字符集
     * @param err -转换错误时返回该文字
     * @return
     */
    public static String getString(byte[] s1,String encode,String err){
        try {
            return new String(s1,encode);
        } catch (UnsupportedEncodingException e) {
            return err==null?null:err;
        }
    }

    /**
     * byte数组转换为Stirng
     * @param s1-数组
     * @param encode-字符集
     * @return
     */
    public static String getString(byte[] s1,String encode){
        return getString(s1,encode,null);
    }

    /**
     * 16进制字符创转int
     */
    public static int hexStringToInt(String hexString){
        return Integer.parseInt(hexString,16);
    }

    /**
     * 十进制转二进制
     */
    public static String intToBinary(int i){
        return Integer.toBinaryString(i);
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     */
    public static Boolean isHexStr(String hexStr) {
        String regex="^[A-Fa-f0-9]+$";
        return hexStr.matches(regex);
    }

    /**
     * byte转16进制string
     */
    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String byteArrayToHexStr(byte[] byteArray, int bytelen) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytelen * 2];
        for (int j = 0; j < bytelen; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 16进制string转byte
     */
    public static byte[] hexStrToByteArray(String str)
    {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++){
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }


    /**
     * 字符串转换成为16进制(无需Unicode编码)
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789abcdef";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

}

