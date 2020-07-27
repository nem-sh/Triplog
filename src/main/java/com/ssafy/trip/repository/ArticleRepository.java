package com.ssafy.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.trip.model.Article;
import com.ssafy.trip.model.MemberUser;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	Optional<Article> findByNum(Long num);
//	블로그에서 검색기능
	List<Article> findByTitleContaining(String keyword);
	void deleteByNum(Long num);
	List<Article> findByLikearticle(MemberUser user);
}
