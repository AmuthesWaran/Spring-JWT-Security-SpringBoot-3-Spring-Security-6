package com.ammu.jwtsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ammu.jwtsecurity.model.JWTRequest;
import com.ammu.jwtsecurity.model.JWTResponse;
import com.ammu.jwtsecurity.service.AuthUserDetailsService;
import com.ammu.jwtsecurity.service.JWTUtility;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
public class AuthController {
	
	
	AuthenticationManager manager;
	
	@Autowired
	JWTUtility jwtUtility;
	
	@Autowired
	AuthUserDetailsService authUserDetailsService;

	@GetMapping("/secured")
	public String securedEndpoint() {
		return "You have accessed this secured endpoint using JWT Authentication";
	}
	
	@PostMapping("/login")
	public JWTResponse login(@RequestBody JWTRequest request) throws Exception {
		System.out.println("HI there");
		try {
			
			manager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())	
					);	
		}
		catch( BadCredentialsException e ){
			throw new Exception("Wrong_Username_or_Password");
		}		
		UserDetails userdetail = authUserDetailsService.loadUserByUsername(request.getUsername());		
		String generateToken = jwtUtility.generateToken(userdetail);
		return new JWTResponse(generateToken);
		
	}
	
	
}
