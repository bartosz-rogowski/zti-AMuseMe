package com.example.aMuseMe.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.aMuseMe.exception.ApiRequestException;
import com.example.aMuseMe.model.User;
import com.example.aMuseMe.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

import static com.example.aMuseMe.constant.Constants.EXPIRATION_TIME;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

//    @GetMapping
//    public List<User> getUsers() {
//        return userService.getUsers();
//    }

//    @GetMapping(path = "{userID}")
//    public User getUserByID(@PathVariable("userID") Long userID) {
//        return userService.getUserByID(userID);
//    }

    @GetMapping(path = "{email}")
    public Optional<User> getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    public ResponseEntity<?> signupNewUser(@RequestBody User user) {
        URI uri = URI.create(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/user").toUriString()
        );
        return ResponseEntity.created(uri).body(userService.addUser(user));
    }

    @DeleteMapping(path = "{userID}")
    public ResponseEntity<?> deleteUser(@PathVariable("userID") Long userID) {
        userService.deleteUser(userID);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "{userID}")
    public ResponseEntity<?> editUser(
            @PathVariable("userID") Long userID,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdate,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        try {
            if (authorizationHeader != null
                    && authorizationHeader.startsWith("Bearer ")
            ) {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Optional<User> userOptional = userService.getUserByEmail(username);
                if (userOptional.isPresent()) {
                    if (Objects.equals(userOptional.get().getId(), userID)) {
                        userService.updateUser(userID, email, password, nickname, birthdate);
                        return ResponseEntity.ok().build();
                    } else {
                        throw new ApiRequestException("You cannot change data of other users");
                    }
                } else {
                    throw new ApiRequestException("User with given email not found");
                }
            }
            else {
                throw new ApiRequestException("User token has not been provided");
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(FORBIDDEN.value()).build();
        }
    }


    @GetMapping("/refresh_token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ")
        ) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Optional<User> userOptional = userService.getUserByEmail(username);
                if( userOptional.isPresent() ) {
                    User user = userOptional.get();
                    String accessToken = JWT.create()
                            .withSubject(user.getEmail())
                            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME*60*1000))
                            .withIssuer(request.getRequestURL().toString())
                            .withClaim("roles",
                                    Collections.singleton("USER")
                                            .stream().toList())
                            .sign(algorithm);
                    // saving tokens to response body
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("access_token", accessToken);
                    tokens.put("refresh_token", refreshToken);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                }
                else {
                    throw new ApiRequestException("User with given email not found");
                }

            }
            catch (NumberFormatException nfe){
                String message = "Token conversion failed due to invalid token format";
                response.setHeader("error", message);
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", message);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
            catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else {
            throw new ApiRequestException("Refresh token is missing");
        }
    }
}
