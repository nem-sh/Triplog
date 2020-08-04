package com.ssafy.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.model.Neighbor;

@Repository
public interface NeighborRepository extends JpaRepository<Neighbor, Long>{
	List<Neighbor> findByUserNumOrderByNeighborNum(Long user_num);
	
	@Transactional
    @Modifying
	void deleteByUserNumAndNeighborNum(Long user_num, Long neighbor_num);
}
