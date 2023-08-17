package com.epam.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
	Random random=new Random();
	public String generateUsername(String firstName, String lastName)
	{
		String randomNumber=String.valueOf(random.nextInt(1000000-99999)+1)+999;
		return firstName+lastName+randomNumber;
		
	}

}
