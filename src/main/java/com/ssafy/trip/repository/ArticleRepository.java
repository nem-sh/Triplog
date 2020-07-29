package com.ssafy.trip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.trip.model.Article;
import com.ssafy.trip.model.MemberUser;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	Optional<Article> findByNum(Long num);
//	블로그에서 검색기능
	List<Article> findByTitleContaining(String keyword);
	List<Article> findByTitleContainingAndNum(String keyword, int user_num);
	List<Article> findByUsernum(Long user_num);
	
	@Query(nativeQuery = true, value = "select * from article where user_num = :user_num order by num desc limit :limit, 10")
	List<Article> findByUsernumPaging(@Param("user_num") Long user_num, @Param("limit") int limit);
	
	void deleteByNum(Long num);
	List<Article> findByLikearticle(MemberUser user);
}
