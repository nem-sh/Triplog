package com.ssafy.trip.controller;

import java.net.URI;
import java.util.Collections;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssafy.trip.exception.AppException;
import com.ssafy.trip.help.ApiResult;
import com.ssafy.trip.help.JwtAuthenticationResult;
import com.ssafy.trip.help.LoginRequest;
import com.ssafy.trip.help.SignUpRequest;
import com.ssafy.trip.model.MemberUser;
import com.ssafy.trip.model.Role;
import com.ssafy.trip.model.RoleName;
import com.ssafy.trip.repository.RoleRepository;
import com.ssafy.trip.repository.UserRepository;
import com.ssafy.trip.security.JwtTokenProvider;
import com.ssafy.trip.service.EmailValidationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    EmailValidationService emailValidationService;
    
    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    	logger.info("1-------------authenticateUser-----------------------------"+loginRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResult(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser( @RequestBody SignUpRequest signUpRequest) {
    	logger.info("1-------------registerUser-----------------------------"+signUpRequest);
    	if(userRepository.existsByNickname(signUpRequest.getNickname())) {
            return new ResponseEntity(new ApiResult(false, "사용자 별명이 이미 존재합니다!"),
                    HttpStatus.ACCEPTED);
        }
    	logger.info("2-------------registerUser-----------------------------"+signUpRequest);
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResult(false, "이메일이 이미 존재합니다!"),
                    HttpStatus.ACCEPTED);
        }
        logger.info("3-------------registerUser-----------------------------"+signUpRequest);
        // Creating user's account
        MemberUser user = new MemberUser(null, signUpRequest.getName(), signUpRequest.getNickname(),
                signUpRequest.getEmail(), signUpRequest.getPassword(), null, null, null, 0, false);
        logger.info("4-------------registerUser-----------------------------"+user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        //logger.info("4-------------registerUser-----------------------------"+userRole);
        user.setRoles(Collections.singleton(userRole));
       
        MemberUser result = userRepository.save(user);
        //logger.info("5-------------registerUser-----------------------------"+result);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getNickname()).toUri();

        emailValidationService.sendEmail(signUpRequest.getEmail());
        
        return ResponseEntity.created(location).body(new ApiResult(true, "성공적으로 등록되었습니다."));
    }
}
