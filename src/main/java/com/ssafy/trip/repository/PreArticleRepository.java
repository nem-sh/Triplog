package com.ssafy.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.model.PreArticle;



public interface PreArticleRepository extends JpaRepository<PreArticle, Long> {
	List<PreArticle> findByUsernum(Long usernum);
	List<PreArticle> findByUsernumAndCommentNotNull(Long usernum);
	List<PreArticle> findByUsernumAndMediaNotNull(Long usernum);
	PreArticle findByNum(Long num);
	
	@Transactional
    @Modifying
	void deleteByUsernum(Long num);
}
