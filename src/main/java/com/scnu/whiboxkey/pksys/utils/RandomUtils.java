package com.scnu.whiboxkey.pksys.utils;

import java.util.Random;

public class RandomUtils {

    private static Random strGen = new Random();

    private static Random numGen = new Random();

    private static Random hexGen = new Random();

//    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz").toCharArray();

    private static char[] numbers = ("0123456789").toCharArray();

    private static char[] numbersAndHex = ("0123456789abcdef").toCharArray();

    /**
     * 产生随机字符串
     **/
    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[strGen.nextInt(35)];
        }
        return new String(randBuffer);
    }

    /**
     * 产生随机数值字符串
     **/
    public static final String randomNumStr(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbers[numGen.nextInt(9)];
        }
        return new String(randBuffer);
    }

    /**
     * 产生随机16进制串
     **/
    public static final String random16Hex(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndHex[hexGen.nextInt(15)];
        }
        return new String(randBuffer);
    }
}
