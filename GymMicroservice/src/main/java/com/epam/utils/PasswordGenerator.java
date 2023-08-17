package com.epam.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class PasswordGenerator {

	private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-+=";
	private static final String ALL_CHARACTERS = UPPERCASE_CHARACTERS + LOWERCASE_CHARACTERS + DIGITS
			+ SPECIAL_CHARACTERS;

	private static final int MIN_PASSWORD_LENGTH = 12;

	private static final Random random = new Random();

	public String generatePassword() {
		StringBuilder password = new StringBuilder();
		password.append(getRandomChar(UPPERCASE_CHARACTERS));
		password.append(getRandomChar(LOWERCASE_CHARACTERS));
		password.append(getRandomChar(DIGITS));
		password.append(getRandomChar(SPECIAL_CHARACTERS));
		for (int i = password.length(); i < MIN_PASSWORD_LENGTH; i++) {
			password.append(getRandomChar(ALL_CHARACTERS));
		}
		for (int i = password.length() - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			char temp = password.charAt(index);
			password.setCharAt(index, password.charAt(i));
			password.setCharAt(i, temp);
		}
		return password.toString();
	}

	private static char getRandomChar(String characters) {
		int randomIndex = random.nextInt(characters.length());
		return characters.charAt(randomIndex);
	}

}
