package com.key.password_manager.key;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {

    private final String UPPERCASE_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final String LOWERCASE_ALPHABETS = "abcdefghijklmnopqrstuvwxyz";

    private final String NUMERICS = "0123456789";

    private final String SPECIAL_CHARACTERS = "!@#&()–[{}]:;_,?/*~$^+=<>";

    private final String[] PASSWORD_CHARACTERS =
            {UPPERCASE_ALPHABETS, LOWERCASE_ALPHABETS, NUMERICS, SPECIAL_CHARACTERS};

    private final Random RANDOM = new Random();

    private final int PASSWORD_LENGTH = 12;

    private int[] getRandomIndices() {
        int passwordCharIdx = RANDOM.nextInt(4);
        return new int[] {passwordCharIdx,
                RANDOM.nextInt(PASSWORD_CHARACTERS[passwordCharIdx].length())};
    }

    public String generate() {
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int[] indices = getRandomIndices();
            passwordBuilder.append(PASSWORD_CHARACTERS[indices[0]].charAt(indices[1]));
        }
        return passwordBuilder.toString();
    }
}
