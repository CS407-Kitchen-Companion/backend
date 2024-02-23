package org.cs407team7.KitchenCompanion.service;

import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.UserRepository;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getAuthUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth instanceof AnonymousAuthenticationToken) {
//            return null;
//        }
//
//        try {
//            User user = (User) (auth.getPrincipal());
//            return user;
//        } catch (ClassCastException e) {
//            return null;
//        }
        return userRepository.findById(1L).orElse(null);
//        return userRepository.findByEmail(email).orElse(null);
    }
}
