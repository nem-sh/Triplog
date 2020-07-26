package com.ssafy.trip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.trip.model.LikeArticle;

@Repository
public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {
	Optional<LikeArticle> findByUserNumAndArticleNum(Long userNum, Long articleNum);
}
