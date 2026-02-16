package com.example.Blogging_platform2.controller;

import com.example.Blogging_platform2.dao.UserDao;
import com.example.Blogging_platform2.dto.LoginRequestDto7;
import com.example.Blogging_platform2.dto.UserDto;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.securityconfig7.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto7 loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Extract roles as claims
            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            // Generate JWT with username + roles
            return jwtUtil.generateToken(loginRequest.getUsername(), roles);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }


    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto) {
        if (userDao.findByUsername(userDto.getUsername()).isPresent()) {
            return "Username already exists!";
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDao.save(user);
        return "User registered successfully!";
    }

}
