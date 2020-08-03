package com.ssafy.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.trip.model.BlogInfo;

@Repository
public interface BlogInfoRepository extends JpaRepository<BlogInfo, Long> {
	Boolean existsByUsernum(Long usernum);
	BlogInfo findByUsernum(Long usernum);

	BlogInfo findByNum(Long num);

}
