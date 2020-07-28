package com.ssafy.trip.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.exception.ResourceNotFoundException;
import com.ssafy.trip.help.UserIdentityAvailability;
import com.ssafy.trip.help.UserProfile;
import com.ssafy.trip.model.Article;
import com.ssafy.trip.model.MemberUser;
import com.ssafy.trip.repository.UserRepository;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByNickname(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{email}")
    public UserProfile getUserProfile(@PathVariable(value = "email") String email) {
    	System.out.println(email);
        MemberUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        System.out.println(user);
        UserProfile userProfile = new UserProfile(user.getNum(), user.getEmail(), user.getNickname(), user.getName(), user.getCreatedAt(), user.getImageSrc(), user.getIntro(), user.isValid());
        return userProfile;
    }
    
    @GetMapping("/users/get/{num}")
    public UserProfile getUserProfileByNum(@PathVariable(value = "num") Long num) {
        MemberUser user = userRepository.findByNum(num)
                .orElseThrow(() -> new ResourceNotFoundException("User", "num", num));

        UserProfile userProfile = new UserProfile(user.getNum(), user.getEmail(), user.getNickname(), user.getName(), user.getCreatedAt(), user.getImageSrc(), user.getIntro(), user.isValid());
        return userProfile;
    }

    @PutMapping("/users/{email}")
    public ResponseEntity<String> modifyUserProfile(@RequestBody UserProfile userProfile, @PathVariable(value = "email") String email) {
    	MemberUser user = null;
    	String SUCCESS = "success";
        try {
        	user = userRepository.findByEmail(email)
        			.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        	user.setName(userProfile.getName());
        	user.setNickname(userProfile.getNickname());
        	user.setIntro(userProfile.getIntro());
        	user.setImageSrc(userProfile.getImagesrc());
        } catch (Exception e) {
        	return null;
        }
        System.out.println(user);
        MemberUser updateUser = userRepository.save(user); 
//        UserProfile result = new UserProfile(updateUser.getNum(), updateUser.getEmail(), updateUser.getNickname(), updateUser.getName(), updateUser.getCreatedAt(), updateUser.getImageSrc(), updateUser.getIntro(), updateUser.isValid());
        
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }
    
    @DeleteMapping("/users/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
      try {
    	  MemberUser user = null;
    	  user = userRepository.findByEmail(email)
      			.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    	 
    	  userRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
      }
    }
    @GetMapping("/{user_num}/searchArticle/{keyword}")
    public List<Article> searchArticle(@PathVariable(value="keyword") String keyword) {
    	return null;
    }
}
