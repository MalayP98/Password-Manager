package com.key.password_manager.stringgenerators;

public class RandomStringGeneratorBuilder {

	private RandomStringGenerator randomStringGenerator;

	public RandomStringGeneratorBuilder() {
		this.randomStringGenerator = new RandomStringGenerator();
	}

	public RandomStringGeneratorBuilder add(String characterSet) {
		this.randomStringGenerator.addCharacterSet(characterSet);
		return this;
	}

	public RandomStringGenerator build() throws Exception {
		if (this.randomStringGenerator.characterSetSize() == 0)
			throw new Exception("Cannot build " + RandomStringGenerator.class.getName()
					+ " with empty character set.");
		return this.randomStringGenerator;
	}
}
