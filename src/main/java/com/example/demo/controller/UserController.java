package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/user-api")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User userData) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User userExist = userRepo.findFirstByUsername(userData.getUsername());
			boolean successLogin = false;
			if (userExist != null) {
				if (userExist.getPassword().equalsIgnoreCase(userData.getPassword())) {
					successLogin = true;
				}
			}

			if (successLogin) {
				map.put("status", "Success");
				map.put("message", "Login Success");
			} else {
				map.put("status", "Failed");
				map.put("message", "Username or Password is wrong or not exist");
			}

			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("status", "Failed");
			map.put("message", "Internal Error");
			map.put("messageError", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login-email")
	public ResponseEntity<?> loginEmailUser(@RequestBody User userData) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User userExist = userRepo.findFirstByEmail(userData.getEmail());
			boolean successLogin = false;
			if (userExist != null) {
				successLogin = true;
			}

			if (successLogin) {
				map.put("status", "Success");
				map.put("message", "Login Success");
			} else {
				map.put("status", "Failed");
				map.put("message", "Email not register");
			}

			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("status", "Failed");
			map.put("message", "Internal Error");
			map.put("messageError", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody User userData) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			User userExist = userRepo.findFirstByUsername(userData.getUsername());
			if (userExist != null) {
				map.put("status", "Failed");
				map.put("message", "Username already exist");
			} else {
				map.put("status", "Success");
				map.put("message", "User Created");
				userRepo.save(userData);
			}

			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("status", "Failed");
			map.put("message", "Internal Error");
			map.put("messageError", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
