package com.key.password_manager.stringgenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStringGenerator {

	private List<String> characterSets = new ArrayList<>();

	private final Random RANDOM = new Random();

	private int[] getRandomIndices() {
		int passwordCharIdx = RANDOM.nextInt(characterSets.size());
		return new int[] {passwordCharIdx,
				RANDOM.nextInt(characterSets.get(passwordCharIdx).length())};
	}

	public String generate(int length) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int[] indices = getRandomIndices();
			stringBuilder.append(characterSets.get(indices[0]).charAt(indices[1]));
		}
		return stringBuilder.toString();
	}

	public void setCharacterSets(List<String> characterSets) {
		this.characterSets = characterSets;
	}
}
