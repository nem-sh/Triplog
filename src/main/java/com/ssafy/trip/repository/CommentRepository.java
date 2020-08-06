package com.ssafy.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.model.Article;
import com.ssafy.trip.model.Comment;
import com.ssafy.trip.model.MemberUser;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByArticlenumAndReply(Long num, Comment reply);
	List<Comment> findByReply(Comment reply);
	
	Comment findByNum(Long num);
	
	@Transactional
    @Modifying
	void deleteAllByReply(Comment comment);
	
	@Transactional
    @Modifying
	void deleteAllByUsernum(Long usernum);

}
