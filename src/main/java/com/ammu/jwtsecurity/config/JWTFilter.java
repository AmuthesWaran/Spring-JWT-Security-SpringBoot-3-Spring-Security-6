package com.ammu.jwtsecurity.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ammu.jwtsecurity.service.AuthUserDetailsService;
import com.ammu.jwtsecurity.service.JWTUtility;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter{
	
	private final AuthUserDetailsService authUserDetailsService;
	private final JWTUtility jwtUtil;
	
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain
			)throws ServletException, IOException {
		
		String username = null;
		String token = null;
		
//		To extract the Authorization token from the HttpRequest object
		String authorization = request.getHeader("Authorization");
		
	
//		Validating if the header is not null and valid
		if (authorization != null && authorization.startsWith("Bearer ")) {
//			Extracting the token from authorization header string			
			token = authorization.substring(7);
//			Extracting the userName from the token
			username = jwtUtil.extractUsername(token);			
		}
//		else {
//	         System.out.println("Bearer String not found in token");
//	         }
		
//		Checking if the userName is not null and the authentication is not done yet
		if (null !=  username && SecurityContextHolder.getContext().getAuthentication() == null ) {
//			Creating UserDetails object to pass in the arguments of the validate token
			UserDetails userdetails = authUserDetailsService.loadUserByUsername(username);
//			Validating the UserDetails comparing to the JWT Token
			if(jwtUtil.isTokenValid(token, userdetails)) {
//				If valid then generating UserNamePasswordAuthenticationToken to denote that the user is authenticated				
				UsernamePasswordAuthenticationToken upToken = 
						new UsernamePasswordAuthenticationToken(userdetails, null, userdetails.getAuthorities());
				
//				To convert the http servelt request to spring boot context
				WebAuthenticationDetails webAuthDetails = new WebAuthenticationDetailsSource().buildDetails(request);
				
//				Injecting the details to UsernamePasswordAuthenticationToken
				upToken.setDetails(webAuthDetails);
				
//				Injecting the UsernamePasswordAuthenticationToken into the SecurityContext Holder
				SecurityContextHolder.getContext().setAuthentication(upToken);
				
			}

		}
//		After the filter process is over forword the request to the next elemnt in the chain
		filterChain.doFilter(request, response);
		
	}

}
