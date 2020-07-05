package com.kt.ibs.service;

import com.kt.ibs.entity.Customer;
import com.kt.ibs.repository.CustomerRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final static Logger LOGGER = Logger.getLogger(AuthenticationService.class);

    private static int workload = 12;
    @Autowired
    private CustomerRepository customerRepository;

    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(workload));
    }

    public boolean validateUserCredentials(String password, String username) {
        Optional<Customer> user = customerRepository.findByUsername(username);
        if (user == null || !user.isPresent()) {
            return false;
        }
        String hash = user.get().getPassword();
        return BCrypt.checkpw(password, hash);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<Customer> user = customerRepository.findByUsername(username);
        if (user == null || !user.isPresent()) {
            return false;
        }
        String hash = user.get().getPassword();
        if (BCrypt.checkpw(oldPassword, hash)) {
            LOGGER.debug("Updating password ");
            String newHash = hash(newPassword);
            user.get().setPassword(newHash);
            customerRepository.save(user.get());
            return true;
        }
        return false;
    }
}
