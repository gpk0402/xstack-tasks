package com.epam.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.request.UpdatePassword;
import com.epam.service.UserServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("gym/user")
@Slf4j
@AllArgsConstructor
public class UserController {
	private final UserServiceImpl userServiceImpl;

	@PutMapping("/updatePassword")
	@ResponseStatus(code = HttpStatus.OK)
	public void changeUserLogin(@Valid @RequestBody UpdatePassword updatePassword) {
		log.info("Entered into chnage Login {} Method RestController :{}", updatePassword);
		userServiceImpl.changeLogin(updatePassword);
	} 

}
