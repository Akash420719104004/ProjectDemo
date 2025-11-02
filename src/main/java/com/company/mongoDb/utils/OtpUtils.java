package com.company.mongoDb.utils;
import java.security.SecureRandom;
public class OtpUtils {
//    private static final String OTP_CHARS = "0123456789";
//    private static final int OTP_LENGTH = 6;
//    private SecureRandom random = new SecureRandom();
//    public String generateOtp() {
//        StringBuilder otp = new StringBuilder(OTP_LENGTH);
//        for (int i = 0; i < OTP_LENGTH; i++) {
//            otp.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
//        }
//        return otp.toString();
//    }
    public String generateOtp(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(chars.charAt(random.nextInt(chars.length())));
        }
        return otp.toString();
    }

}