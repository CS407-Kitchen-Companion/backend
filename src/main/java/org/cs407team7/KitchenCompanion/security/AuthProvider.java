package org.cs407team7.KitchenCompanion.security;

import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Adapted from CS 307
 */
@Component
public class AuthProvider {

//public class AuthProvider implements AuthenticationProvider {
//    @Autowired
//    private SecurityUserService securityUserService;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public Authentication authenticate(Authentication auth) throws AuthenticationException {
////        System.out.println(auth);
//
//        String email = auth.getName();
//        String password = auth.getCredentials().toString();
//
//        User u = userRepository.findByEmail(email).orElse(null);
//        if (u == null || !u.getPassword().equals(password)) { // || u.getProvider() != UserProvider.LOCAL
//            System.out.println("Failed to authenticate `" + email + "`.");
//            throw new BadCredentialsException("Incorrect user credentials.");
//        }
//
////        if (u.getDeleted()) {
////
////        }
//
////        if (u.getDisabled()) {
////            if (u.getDisabledUntil().isBefore(LocalDateTime.now())) {
////                u.setDisabled(false);
////            } else {
////                throw new AccountStatusException("User Account is Disabled") {};
////            }
////        }
//
//        if (!u.getVerified()) {
//            System.out.println("User `" + email + "` is not verified.");
//            throw new AccountStatusException("User not verified.") {
//            };
//        }
//
//        System.out.println("Authenticated `" + email + "`.");
//        UsernamePasswordAuthenticationToken n = new UsernamePasswordAuthenticationToken(
//                email, password,
//                null // TODO: change this to have users authorities ("USER" role)
//        );
//        return n;
//    }
//
//    @Override
//    public boolean supports(Class<?> auth) {
//
//        return auth.equals(UsernamePasswordAuthenticationToken.class);
//    }
}