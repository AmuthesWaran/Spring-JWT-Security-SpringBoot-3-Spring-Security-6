package com.ammu.jwtsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ammu.jwtsecurity.model.JWTRequest;
import com.ammu.jwtsecurity.model.JWTResponse;

@RestController
public class AuthController {

	@GetMapping("/secured")
	public String securedEndpoint() {
		return "You have accessed this secured endpoint using JWT Authentication";
	}
	
	@PostMapping("/login")
	public JWTResponse login(@RequestBody JWTRequest jwtRequest) {
		return null;
	}
	
	
}
