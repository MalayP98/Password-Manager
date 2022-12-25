package com.key.password_manager.stringgenerators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomStringGeneratorConfigurer {

	@Autowired
	private RandomStringGeneratorBuilder randomStringGeneratorBuilder;

	@Bean("passwordGenerator")
	public RandomStringGenerator passwordGenerator() {
		randomStringGeneratorBuilder.add(CharacterSets.UPPERCASE_ALPHABETS)
				.add(CharacterSets.LOWERCASE_ALPHABETS).add(CharacterSets.SPECIAL_CHARACTERS)
				.add(CharacterSets.NUMERICS);
		return randomStringGeneratorBuilder.build();
	}

	@Bean("otpGenerator")
	public RandomStringGenerator otpGenerator() {
		randomStringGeneratorBuilder.add(CharacterSets.UPPERCASE_ALPHABETS)
				.add(CharacterSets.NUMERICS);
		return randomStringGeneratorBuilder.build();
	}
}
