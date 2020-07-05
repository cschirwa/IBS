package com.kt.ibs.security;

import com.kt.ibs.entity.Customer;
import com.kt.ibs.entity.User;
import com.kt.ibs.repository.CustomerRepository;
import com.kt.ibs.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        //log.debug("DB search for username {}", username);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            User customer = user.get();
            if (!customer.getEnabled()) {
                throw new UsernameNotFoundException(String.format("Customer with username '%s' is not active.", username));
            }
            if (customer.getLocked() != null && customer.getLocked()) {
                throw new UsernameNotFoundException(String.format("Customer with username '%s' is locked.", username));
            }
           // log.debug("Creating with user {}", user.get());
            JwtUser create = JwtUserFactory.create(user.get());
           // log.debug("Created JWT USER {}", create.getAuthorities());
            return create;

        }
        throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
    }
}
