package com.ssafy.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.model.Article;
import com.ssafy.trip.model.MemberUser;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	Optional<Article> findByNum(Long num);
//	블로그에서 검색기능
	List<Article> findByTitleContaining(String keyword);
	List<Article> findByTitleContainingAndNum(String keyword, int user_num);
	List<Article> findByUsernum(Long user_num);
	
	void deleteByNum(Long num);
	
	@Transactional
    @Modifying
	void deleteAllByUsernum(Long usernum);
	List<Article> findByLikearticle(MemberUser user);
}
