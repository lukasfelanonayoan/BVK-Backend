package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class CheckerController {

	// Check API Ready to Use
	@GetMapping
	public ResponseEntity<Object> getStatus() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			HttpStatus statusResult = HttpStatus.CONTINUE;
			map.put("status", 200);
			map.put("message", "API Ready to Use");
			return new ResponseEntity<Object>(map, statusResult);
		} catch (Exception error) {
			Map<String, Object> map = new HashMap<String, Object>();
			HttpStatus statusResult = HttpStatus.BAD_GATEWAY;
			map.put("status", statusResult.value());
			map.put("exceptionMessage", error.getMessage());
			map.put("message", "Error Get Data Movie");
			return new ResponseEntity<Object>(map, statusResult);
		}
	}
}
