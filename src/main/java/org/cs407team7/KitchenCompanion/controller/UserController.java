package org.cs407team7.KitchenCompanion.controller;

import jakarta.validation.Valid;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.UserRepository;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserController {
//    @PostMapping("/login")
//    public ResponseEntity<String> authenticateUser(@RequestBody Login login) {
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
//    }

    @Autowired
    UserRepository userRepository;

    @PostMapping(path = "/new")
    public ResponseEntity<Object> addNewUser(
            @RequestBody Map<String, String> payload
    ) {
        if (!payload.containsKey("username") || !payload.containsKey("password") || !payload.containsKey("email") ||
                payload.get("username").isBlank() || payload.get("password").isBlank() || payload.get("email").isBlank()
        ) {
            return ResponseEntity.status(401).body(new ErrorResponse(401, "Invalid Format"));
        }

        if (userRepository.findByEmail(payload.get("email")).isPresent()) {
            // 409 - Conflict
            return ResponseEntity.status(409).body(new ErrorResponse(409, "Email is already in use"));
        }
        if (userRepository.findByUsername(payload.get("username")).isPresent()) {
            // 409 - Conflict
            return ResponseEntity.status(409).body(new ErrorResponse(409, "Username is already in use"));
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        User n = new User(payload.get("username"), bCryptPasswordEncoder.encode(payload.get("password")), payload.get("email"));
        n.setToken(generateRandomString(64));

        // I'll deal with email later
        n.setVerified(true);

        userRepository.save(n);

//        emailService.sendVerificationEmail(n, n.getToken(), n.getId());

        return ResponseEntity.status(201).body(new GenericResponse("Please Verify Email"));
    }

    private String generateRandomString(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890123456789";
        StringBuilder rand = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            rand.append(str.charAt(secureRandom.nextInt(str.length())));
        }
        return rand.toString();
    }


    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid Map<String, String> payload) {
//        try {
//            Authentication authenticate = authenticationManager
//                    .authenticate(
//                            new UsernamePasswordAuthenticationToken(
//                                    request.getUsername(), request.getPassword()
//                            )
//                    );
//
//            User user = (User) authenticate.getPrincipal();
//
//            return ResponseEntity.ok()
//                    .header(
//                            HttpHeaders.AUTHORIZATION,
//                            jwtTokenUtil.generateAccessToken(user)
//                    )
//                    .body(userViewMapper.toUserView(user));
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
        return null;
    }
}