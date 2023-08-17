package com.epam.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class PasswordGeneratorTest {

    @InjectMocks
    private PasswordGenerator passwordGenerator;
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-+=";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGeneratePassword() {
        String generatedPassword = passwordGenerator.generatePassword();
        assertTrue(generatedPassword.length() >=7);
        // Check if the generated password contains at least one character from each category
        assertTrue(hasCharacterFromCategory(generatedPassword, UPPERCASE_CHARACTERS));
        assertTrue(hasCharacterFromCategory(generatedPassword, LOWERCASE_CHARACTERS));
        assertTrue(hasCharacterFromCategory(generatedPassword, DIGITS));
        assertTrue(hasCharacterFromCategory(generatedPassword, SPECIAL_CHARACTERS));
    }

    private boolean hasCharacterFromCategory(String password, String category) {
        for (char c : category.toCharArray()) {
            if (password.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }
}

