package com.kt.ibs.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

//@Api("Customer services")
@RestController
@RequestMapping("/ibs/api/users")
@Slf4j
public class UserRestController {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public JwtUser getAuthenticatedUser(
//			@ApiParam(value = "Authorization token", required = true) @RequestHeader("Authorization") String authorization,
			HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		
		String authToken = token.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(authToken);
		log.debug("user name from token is {}", username);
		JwtUser loadUserByUsername = (JwtUser) userDetailsService.loadUserByUsername(username);
		log.debug("Returning user as {}", loadUserByUsername);
		return loadUserByUsername;
	}

}
