package com.ssafy.trip.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.help.CommentResponse;
import com.ssafy.trip.model.Comment;
import com.ssafy.trip.repository.CommentRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/comment")
public class CommentController {
	@Autowired
	private CommentRepository commentRepository;
	
	@GetMapping("/{articlenum}")
	public List<CommentResponse> getComment(@PathVariable(value = "articlenum") Long num) {
		
		List<Comment> comments = commentRepository.findByArticlenumAndReply(num, null);
		System.out.println("dd");
		System.out.println(comments);
		List<CommentResponse> commentResponses = new ArrayList<CommentResponse>();
		for(Comment comment : comments) {
			List<Comment> cocomments = commentRepository.findByReply(comment);
			
			commentResponses.add(new CommentResponse(comment, cocomments));
		}
		System.out.println(commentResponses);
		return commentResponses;	
	}
	
	@DeleteMapping("/{num}")
	public ResponseEntity<?> deleteComment(@PathVariable(value = "num") Long num) {
		System.out.println(num);
		Comment comment = commentRepository.findByNum(num);
		

  	  	commentRepository.deleteAllByReply(comment);
		
  	  	commentRepository.delete(comment);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);	
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createComment(@RequestBody Comment comment) {
		
	
		commentRepository.save(comment);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);	
	}
	@PostMapping("/{replyNum}")
	public ResponseEntity<?> createCocomment(@PathVariable(value = "replyNum") Long replyNum, @RequestBody Comment comment) {
		
		
		Comment reply = commentRepository.findByNum(replyNum);
		comment.setReply(reply);
		commentRepository.save(comment);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);	
	}

}
