package com.key.password_manager.stringgenerators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultRandomStringGeneratorConfigurer {

	@Bean("passwordGenerator")
	public RandomStringGenerator passwordGenerator() throws Exception {
		return new RandomStringGeneratorBuilder().add(CharacterSets.UPPERCASE_ALPHABETS)
				.add(CharacterSets.LOWERCASE_ALPHABETS).add(CharacterSets.SPECIAL_CHARACTERS)
				.add(CharacterSets.NUMERICS).build();
	}

	@Bean("simpleStringGenerator")
	public RandomStringGenerator otpGenerator() throws Exception {
		return new RandomStringGeneratorBuilder().add(CharacterSets.UPPERCASE_ALPHABETS)
				.add(CharacterSets.NUMERICS).build();
	}
}
