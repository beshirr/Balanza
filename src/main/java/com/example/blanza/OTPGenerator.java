package com.example.blanza;

import java.util.Random;

/**
 * The type Otp generator.
 */
public class OTPGenerator {
    /**
     * Generate otp string.
     *
     * @return the string
     */
    static public String generateOTP(){
        Random rand = new Random();
        int otp = rand.nextInt(100000, 1000000);
        String OTP = String.valueOf(otp);
        return OTP;
    }
}
