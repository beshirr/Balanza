package com.example.blanza;

import java.util.Random;

public class OTPGenerator {
    static public String generateOTP(){
        Random rand = new Random();
        int otp = rand.nextInt(100000, 1000000);
        String OTP = String.valueOf(otp);
        return OTP;
    }
}
