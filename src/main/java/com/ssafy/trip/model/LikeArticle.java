package com.ssafy.trip.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "like_article")
public class LikeArticle {

	@ManyToOne
	private Long userNum;
	@ManyToOne
	private Long articleNum;
	
	public LikeArticle() {
		// TODO Auto-generated constructor stub
	}
	
	public LikeArticle(Long userNum, Long articleNum) {
		super();
		this.userNum = userNum;
		this.articleNum = articleNum;
	}

	public Long getUserNum() {
		return userNum;
	}

	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}

	public Long getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(Long articleNum) {
		this.articleNum = articleNum;
	}

	@Override
	public String toString() {
		return "LikeArticle [userNum=" + userNum + ", articleNum=" + articleNum + "]";
	}
	
}
