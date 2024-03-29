package org.cs407team7.KitchenCompanion.controller;

import jakarta.validation.Valid;
import org.cs407team7.KitchenCompanion.entity.Recipe;
import org.cs407team7.KitchenCompanion.entity.User;
import org.cs407team7.KitchenCompanion.repository.UserRepository;
import org.cs407team7.KitchenCompanion.requestobject.UserUpdateRequest;
import org.cs407team7.KitchenCompanion.responseobject.ErrorResponse;
import org.cs407team7.KitchenCompanion.responseobject.GenericResponse;
import org.cs407team7.KitchenCompanion.responseobject.PublicUserDataResponse;
import org.cs407team7.KitchenCompanion.security.JwtResponse;
import org.cs407team7.KitchenCompanion.security.JwtTokenUtil;
import org.cs407team7.KitchenCompanion.security.JwtUserDetailsService;
import org.cs407team7.KitchenCompanion.service.EmailService;
import org.cs407team7.KitchenCompanion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    private final UserService userService;

    private final EmailService emailService;


    @Autowired
    public UserController(UserRepository userRepository, JwtUserDetailsService userDetailsService,
                          JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, UserService userservice,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userservice;
        this.emailService = emailService;
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
//        n.setVerified(true);

        userRepository.save(n);


//        String base = "https://kitchencompanion.eastus.cloudapp.azure.com/api/v1/user";
        String base = "http://localhost:3000";
        String url = base + "/verify?uid=" + n.getId() + "&token=" + n.getToken();
        String contents = "    <p>Hi there,</p>\n" +
                "    <p>Welcome to Kitchen Companion! To get started, please verify your email address by clicking the button below:</p>\n" +
                "    <div style=\"text-align: center; margin-bottom: 20px;\">\n" +
                "        <a href=\"" + url + "\" style=\"display: inline-block; padding: 10px 20px; background-color: #2D3566; color: #fff; text-decoration: none; border-radius: 5px;\">Verify Email</a>\n" +
                "    </div>\n" +
                "    <p>If you didn't create an account on Kitchen Companion, you can safely ignore this email.</p>\n" +
                "    <p>Thank you,<br>Kitchen Companion Team</p>\n";
        // TODO make sure links are generalised

        emailService.sendEmailHtml(n.getEmail(),
                "Please verify your email",
                contents);

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
        User user = userService.findUserById(Long.valueOf(payload.get("userId")));

//        if (!optionalUser.isPresent()) {
//            // 409 - Conflict
//            return ResponseEntity.status(409).body(new ErrorResponse(409, "user is not in use"));
//        }
//        User user = optionalUser.get();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());

//        System.out.println(user.getPassword());
//        System.out.println(payload.get("oldPassword"));

        if (!bCryptPasswordEncoder.matches(payload.get("oldPassword"), user.getPassword())) {
            // 409 - Conflict
            return ResponseEntity.status(409).body(new ErrorResponse(409, "Invalid Password"));
        }

        user.setPassword(bCryptPasswordEncoder.encode(payload.get("newPassword")));

        userRepository.save(user);

        return ResponseEntity.status(200).body(new GenericResponse("Password successfully updated."));
    }

    @GetMapping(path = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> verifyUser(
            @RequestParam String uid,
            @RequestParam String token) {
        User user = userService.findUserById(Long.parseLong(uid));
        if (user != null && user.getToken().equals(token) && !user.isVerified()) {
            user.setVerified(true);
        } else if (user != null && user.isVerified()) {
            return ResponseEntity.status(400).body(new ErrorResponse(400, "Error verifying user email: User already verified"));
        } else {
            return ResponseEntity.status(400).body(new ErrorResponse(400, "Error verifying user email: No user with matching code token"));
        }
        userRepository.save(user);
//        return ResponseEntity.status(200).body(new GenericResponse("User " + u.getEmail() + " verified."));
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/login")).build();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(
            @PathVariable Long id
    ) {
        User user = userService.findUserById(id);
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
        User user = userService.findUserById(id);
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
//        try {
        return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(user.getSavedRecipes()));
//        } catch (Exception e) {
//            System.out.println(e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    new ErrorResponse(500, "Internal Server Error"));
//
//        }
    }

    @GetMapping(value = "/folders")
    public ResponseEntity<?> getCreatedFolders() {
        User user = userService.getAuthUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(401, "You must be logged in to see saved folders."));
        }
//        try {
        return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse(user.getFolders()));
//        } catch (Exception e) {
//            System.out.println(e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    new ErrorResponse(500, "Internal Server Error"));
//
//        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> payload) throws Exception {

//        System.out.println("auth1");
        try {
            authenticate(payload.get("username"), payload.get("password"));

//        System.out.println("auth2");

            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(payload.get("username"));

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("No such username"));

            if (!user.isVerified()) {
                return ResponseEntity.status(400).body(new ErrorResponse(400, "Please verify your email."));
            }

            String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(token, user.getId()));

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return ResponseEntity.status(401).body(
                    new ErrorResponse(401, "No user exists with such a username or password combination."));

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(new ErrorResponse("Unknown Error"));
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "A user with that Id could not be found"));
        }

        // Call the delete method in the repository
        userRepository.delete(user);

        // Return a successful response
        return ResponseEntity.ok(new GenericResponse("User with ID " + id + " was successfully deleted."));
    }


    @RequestMapping(value = "/{id}/photo", method = RequestMethod.GET)
    public ResponseEntity<?> getPhoto(
            @PathVariable Long id
    ) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "A user with that Id could not found"));
        }
        return ResponseEntity.ok(new GenericResponse(user.getPhoto()));
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getDetails(
            @PathVariable Long id
    ) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse(404, "A user with that Id could not found"));
        }
        return ResponseEntity.ok(new GenericResponse(user.getDetails()));
    }


    @PutMapping(value = "/{id}/update")
    public ResponseEntity<?> updateUserPhotoAndDetails(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request
    ) {

        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
        }

        User user = userOptional.get();
        user.setPhoto(request.getPhoto());
        user.setDetails(request.getDetails());

        userRepository.save(user);

        return ResponseEntity.ok(new GenericResponse("User photo and details updated successfully"));
    }
    @PostMapping(path = "/setVisibility")
    public ResponseEntity<Object> setVisibility(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("userId") || !payload.containsKey("visible")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Missing userId or visible parameter"));
        }

        long userId;
        try {
            userId = Long.parseLong(payload.get("userId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Invalid userId format"));
        }

        boolean visible;
        try {
            visible = (boolean) payload.get("visible");
        } catch (ClassCastException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Invalid visible parameter format"));
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
        }

        User user = userOptional.get();
        user.setVisible(visible);
        userRepository.save(user);

        return ResponseEntity.ok(new GenericResponse("Visibility updated successfully."));
    }



}