package com.key.password_manager.stringgenerators.otpgenerator;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component("defaultOtpGenerator")
public class DefaultOtpGenerator implements OtpGenerator {

    private final String UPPERCASE_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final String LOWERCASE_ALPHABETS = "abcdefghijklmnopqrstuvwxyz";

    private final String NUMERICS = "0123456789";

    private final String[] PASSWORD_CHARACTERS =
            {UPPERCASE_ALPHABETS, LOWERCASE_ALPHABETS, NUMERICS};

    private final Random RANDOM = new Random();

    private int[] getRandomIndices() {
        int passwordCharIdx = RANDOM.nextInt(3);
        return new int[] {passwordCharIdx,
                RANDOM.nextInt(PASSWORD_CHARACTERS[passwordCharIdx].length())};
    }

    @Override
    public String generate(int length) {
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int[] indices = getRandomIndices();
            passwordBuilder.append(PASSWORD_CHARACTERS[indices[0]].charAt(indices[1]));
        }
        return passwordBuilder.toString();
    }
}
