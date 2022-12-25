package com.key.password_manager.stringgenerators;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RandomStringGeneratorBuilder {

	private List<String> characterSets = new ArrayList<>();

	public RandomStringGeneratorBuilder add(String characterSet) {
		characterSets.add(characterSet);
		return this;
	}

	public RandomStringGenerator build() {
		RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
		randomStringGenerator.setCharacterSets(characterSets);
		characterSets.clear();
		return randomStringGenerator;
	}
}
