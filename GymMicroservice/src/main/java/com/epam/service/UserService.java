package com.epam.service;

import com.epam.dto.request.UpdatePassword;

public interface UserService {
	void changeLogin(UpdatePassword updatePassword);
	

}
