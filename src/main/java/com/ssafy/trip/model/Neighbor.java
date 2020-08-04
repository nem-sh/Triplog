package com.ssafy.trip.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "neighbor")
public class Neighbor {
	@Id
	@Column(name = "user_num")
	private Long userNum;
	@Column(name = "neighbor_num")
	private Long neighborNum;

	public Neighbor() {
		super();
	}

	public Neighbor(Long userNum, Long neighborNum) {
		super();
		this.userNum = userNum;
		this.neighborNum = neighborNum;
	}

	public Long getUserNum() {
		return userNum;
	}

	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}

	public Long getNeighborNum() {
		return neighborNum;
	}

	public void setNeighborNum(Long neighborNum) {
		this.neighborNum = neighborNum;
	}

	@Override
	public String toString() {
		return "neighbor [userNum=" + userNum + ", neighborNum=" + neighborNum + "]";
	}

}
