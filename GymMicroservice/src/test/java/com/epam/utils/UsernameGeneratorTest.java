package com.epam.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

 class UsernameGeneratorTest {

    @InjectMocks
    private UsernameGenerator usernameGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testGenerateUsername() {
        String firstName = "John";
        String lastName = "Doe";
        String generatedUsername = usernameGenerator.generateUsername(firstName, lastName);
        // Check if the generated username contains the first name and last name
        assertEquals(true, generatedUsername.contains(firstName));
        assertEquals(true, generatedUsername.contains(lastName));
        // Check if the generated username contains a random number within the specified range
        String randomNumberString = generatedUsername.substring(firstName.length() + lastName.length());
        int randomNumber = Integer.parseInt(randomNumberString);
        assertEquals(true, randomNumber >= 10 && randomNumber <= 999999999);
    }
}

