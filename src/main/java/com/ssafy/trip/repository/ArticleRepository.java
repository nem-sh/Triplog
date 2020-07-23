package com.ssafy.trip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.trip.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	Optional<Article> findByNum(Long num);
	
	void deleteByNum(Long num);
}
