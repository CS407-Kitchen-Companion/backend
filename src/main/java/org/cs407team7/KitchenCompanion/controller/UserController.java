package org.cs407team7.KitchenCompanion.controller;

import jakarta.validation.Valid;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.UserRepository;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.cs407team7.KitchenCompanion.responseobject.PublicUserDataResponse;
import org.cs407team7.KitchenCompanion.security.JwtResponse;
import org.cs407team7.KitchenCompanion.security.JwtTokenUtil;
import org.cs407team7.KitchenCompanion.security.JwtUserDetailsService;
import org.cs407team7.KitchenCompanion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private JwtUserDetailsService userDetailsService;

    private UserService userService;


    @Autowired
    public UserController(UserRepository userRepository, JwtUserDetailsService userDetailsService,
                          JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, UserService userservice) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userservice;
    }


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

        PublicUserDataResponse user = new PublicUserDataResponse(n.getId(), n.getUsername(), n.getEmail(), n.getCreatedAt());

//        emailService.sendVerificationEmail(n, n.getToken(), n.getId());

        return ResponseEntity.status(201).body(new GenericResponse("Please Verify Email", user));
    }

    @PostMapping(path = "/updatePassword")
    public ResponseEntity<Object> updatePassword(
            @RequestBody Map<String, String> payload
    ) {
        if (!payload.containsKey("newPassword") || !payload.containsKey("oldPassword") || !payload.containsKey("userId") ||
                payload.get("newPassword").isBlank() || payload.get("oldPassword").isBlank() || payload.get("userId").isBlank()
        ) {
            return ResponseEntity.status(401).body(new ErrorResponse(401, "Invalid Format"));
        }
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(payload.get("userId")));


        if (!optionalUser.isPresent()) {
            // 409 - Conflict
            return ResponseEntity.status(409).body(new ErrorResponse(409, "user is not in use"));
        }
        User user = optionalUser.get();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        System.out.println(user.getPassword());
        System.out.println(payload.get("oldPassword"));
        if (!bCryptPasswordEncoder.matches(payload.get("oldPassword"), user.getPassword())) {
            // 409 - Conflict
            return ResponseEntity.status(409).body(new ErrorResponse(409, "Invalid Password"));
        }
        user.setPassword(bCryptPasswordEncoder.encode(payload.get("newPassword")));


        //todo  Pending confirmation: Whether the method of updating based on id is called updateById
        userRepository.save(user);


        //todo Pending confirmation: The specific format of the return parameters
        return ResponseEntity.status(201).body(new GenericResponse("Please Verify Email"));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(
            @PathVariable Long id
    ) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "A user with that Id could not found"));
        }
        PublicUserDataResponse preparedData = new PublicUserDataResponse(user.getId(), user.getUsername(),
                user.getEmail(), user.getCreatedAt());
        return ResponseEntity.ok(new GenericResponse(preparedData));
    }

    @RequestMapping(value = "/{id}/username", method = RequestMethod.GET)
    public ResponseEntity<?> getUsername(
            @PathVariable Long id
    ) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "A user with that Id could not found"));
        }
        return ResponseEntity.ok(new GenericResponse(user.getUsername()));
    }

    @GetMapping(value = "/saved")
    public ResponseEntity<?> getSavedRecipes() {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(401, "You must be logged in to see saved recipes."));
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(user.getSavedRecipes()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error"));

        }
    }

    @GetMapping(value = "/folders")
    public ResponseEntity<?> getCreatedFolders() {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(401, "You must be logged in to see saved folders."));
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(user.getFolders()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error"));

        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> payload) throws Exception {

//        System.out.println("auth1");
        authenticate(payload.get("username"), payload.get("password"));
//        System.out.println("auth2");

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(payload.get("username"));

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            System.out.println("auth2a");
        } catch (DisabledException e) {
//            System.out.println("auth3");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
//            System.out.println("auth4");
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
//            System.out.println("auth5 " + e);
            throw e;
        }
    }

    @GetMapping(path = "/testauth")
    public @ResponseBody ResponseEntity<Object> testAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            System.out.println("Anonymous User, not auth.");
            return ResponseEntity.ok().body(new GenericResponse("Not Auth"));
        }
        User user = (User) auth.getPrincipal();
        System.out.println(user.getEmail()); // email
        System.out.println(auth.getCredentials()); // password (should be null)
        System.out.println(auth.getAuthorities()); // authorities (roles)
        System.out.println(auth.getDetails()); // WebAuthenticationDetails
        System.out.println(auth.isAuthenticated()); // is authenticated?
        return ResponseEntity.ok().body(new GenericResponse("AuthSuccess for " + user.getUsername()));
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

}