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
	List<Comment> findByArticlenumAndReplyOrderByCreatedat(Long num, Comment reply);
	List<Comment> findByReplyOrderByCreatedat(Comment reply);
	List<Comment> findByUsernum(Long num);
	Comment findByNum(Long num);
	
	@Transactional
    @Modifying
	void deleteAllByReply(Comment comment);
	
	@Transactional
    @Modifying
	void deleteAllByArticlenum(Long articlenum);
	
	@Transactional
    @Modifying
	void deleteAllByArticlenumAndReplyIsNull(Long articlenum);
	
	@Transactional
    @Modifying
	void deleteAllByArticlenumAndReplyIsNotNull(Long articlenum);
	
	@Transactional
    @Modifying
	void deleteAllByUsernumAndReplyIsNotNull(Long Usernum);
	
	@Transactional
    @Modifying
	void deleteAllByUsernum(Long usernum);

}
