package com.ammu.jwtsecurity.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ammu.jwtsecurity.config.ApplicationConfig;
import com.ammu.jwtsecurity.entity.JWTRequest;
import com.ammu.jwtsecurity.entity.JWTResponse;
import com.ammu.jwtsecurity.entity.UserAccount;
import com.ammu.jwtsecurity.repository.UserAccountRepository;
import com.ammu.jwtsecurity.service.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	
	private final UserAccountRepository repo;

	private ApplicationConfig appConfig;	
	private UserDetailsService userDetailService;
	private AuthenticationManager manager; 
	private final JwtService jwtService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<UserAccount> login(@RequestBody UserAccount userAccount) {
		return new ResponseEntity<UserAccount>(repo.save(userAccount), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/allusers")
	public ResponseEntity<List<UserAccount>> getAllUsers(){
		return new ResponseEntity<List<UserAccount>>(repo.findAll(), HttpStatus.OK);
	}
	
	
	@PostMapping("/login")
	public JWTResponse login(@RequestBody JWTRequest request) throws Exception {
		
		try {
			
			manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUseremail(), request.getPassword()));
			
		}
		catch( BadCredentialsException e ){
			throw new Exception("Wrong_Username_or_Password");
		}		
		UserDetails userdetail = appConfig.userDetailsService().loadUserByUsername(request.getUseremail());		
		String generateToken = jwtService.generateToken(userdetail);
		return new JWTResponse(generateToken);
	}
	
	
}
