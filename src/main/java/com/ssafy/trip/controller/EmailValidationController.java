package com.ssafy.trip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.trip.exception.ResourceNotFoundException;
import com.ssafy.trip.model.MemberUser;
import com.ssafy.trip.repository.UserRepository;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/emailValid")
public class EmailValidationController {

	@Autowired
    UserRepository userRepository;
	
	@RequestMapping("/auth/{email}")
	public String validationPage(@PathVariable(value = "email") String email) {
        MemberUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        user.setValid(true);
        userRepository.save(user);
        return "emailValid";
    }
}
