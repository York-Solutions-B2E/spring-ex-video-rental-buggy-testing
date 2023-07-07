package com.example.video_rental.utility;

import java.util.Random;

public class TokenUtility
{
    private static final char[] ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();
    private static final Random rand = new Random();
    public static String GenerateRandomString(Integer length)
    {
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length;i++)
        {
            sb.append( ALPHANUMERIC[rand.nextInt(ALPHANUMERIC.length-1)]);
        }
        return sb.toString();
    }
}
