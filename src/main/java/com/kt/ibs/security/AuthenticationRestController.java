package com.kt.ibs.security;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.configuration.ApplicationConfigurationProperties;
import com.kt.ibs.entity.AuditType;
import com.kt.ibs.entity.Authority;
import com.kt.ibs.entity.AuthorityName;
import com.kt.ibs.entity.CustomerAudit;
import com.kt.ibs.entity.Session;
import com.kt.ibs.entity.User;
import com.kt.ibs.repository.SessionRepository;
import com.kt.ibs.repository.UserRepository;

//import io.swagger.annotations.Api;

//@Api
@RestController
@RequestMapping("/ibs/api/")
public class AuthenticationRestController {

    private static Logger log = LoggerFactory.getLogger(AuthenticationRestController.class);

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository customerSessionRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ApplicationConfigurationProperties securityProperties;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(@RequestBody final JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
//        log.debug("Auth from device [{}]", device.toString());
        try {
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            Optional<User> user = userRepository.findByUsername(authenticationRequest.getUsername());
            if (user.isPresent()) {
                User successUser = user.get();
                Session session = new Session(token, successUser, null);
//                session.setDevicePlatform(device.getDevicePlatform().name());
//                session.setMobile(device.isMobile());

                successUser.setLastLoginTime(new Date());
                successUser.setFailedLogins(0);
                userRepository.save(successUser);
                List<AuthorityName> authorities = successUser.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
                customerSessionRepository.save(session);
                // Return the token

                applicationEventPublisher.publishEvent(new CustomerAudit( null, authenticationRequest.getUsername(), null, AuditType.LOGIN.name()));
                return ResponseEntity.ok(new JwtAuthenticationResponse(token, "success", authorities));
            }
        } catch (BadCredentialsException e) {
            Optional<User> user = userRepository.findByUsername(authenticationRequest.getUsername());
            log.debug("Auth denied for {}, {}", authenticationRequest.getUsername(), authenticationRequest.getPassword());
            if (user.isPresent()) {
                User failedCustomer = user.get();
                int failedLogins = failedCustomer.getFailedLogins() + 1;
                failedCustomer.setFailedLogins(failedLogins);
                if (securityProperties.getMaxLogInAttempts() == failedLogins) {
                    failedCustomer.setLocked(true);
                    failedCustomer.setLockedDate(new Date());
                    applicationEventPublisher.publishEvent(new CustomerAudit(null, authenticationRequest.getUsername(), null, AuditType.ACCOUNT_LOCKED.name()));
                    userRepository.save(failedCustomer);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new JwtAuthenticationResponse(null, "Account Locked", null));
                } else {
                    applicationEventPublisher.publishEvent(new CustomerAudit( null, authenticationRequest.getUsername(), null, AuditType.LOGIN_FAILURE.name()));
                    userRepository.save(failedCustomer);
                    //return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                      //      .body(new JwtAuthenticationResponse(null, "Invalid credentials"));
                }
            }

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new JwtAuthenticationResponse(null, "Invalid credentials", null));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(final HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            User successUser = userRepository.findByUsername(username).get();
            List<AuthorityName> authorities = successUser.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, "refreshed", authorities));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
