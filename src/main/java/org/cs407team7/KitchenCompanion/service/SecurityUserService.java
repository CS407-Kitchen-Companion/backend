package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * From CS 307
 */
@Service
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException(""));

        // Yes, I know
        return userRepository.findByEmail(username).orElse(null);
    }
}
