package com.key.password_manager.stringgenerators.otpgenerators;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component("defaultOtpGenerator")
public class DefaultOtpGenerator implements OtpGenerator {

    private final String NUMERICS = "0123456789";

    private final Random RANDOM = new Random();

    @Override
    public String generate(int length) {
        String otp = "";
        for (int i = 0; i < length; i++) {
            otp += NUMERICS.charAt(RANDOM.nextInt(NUMERICS.length()));
        }
        return otp;
    }

}
