package com.ssafy.trip.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "article")
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long num;
	private Long userNum;
	private Long trippackageNum;
	private String title;
	private String place;
	private String content;
	private String thumbnail;
	private Boolean temp;
	private Date createdAt;
	private Date dateStart;
	private Date dateEnd;
	private int likeCount;
	private String userNickname;
	private Long views;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "like_Article", joinColumns = @JoinColumn(name = "article_num"), inverseJoinColumns = @JoinColumn(name = "user_num"))
	List<MemberUser> likearticle;

	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Article(Long num, Long usernum, Long trippackage_num, String title, String place, String content,
			String thumbnail, Boolean temp, Date created_at, Date date_start, Date date_end, int likeCount,
			String userNickname, List<MemberUser> likearticle) {
		super();
		this.num = num;
		this.userNum = usernum;
		this.trippackageNum = trippackage_num;
		this.title = title;
		this.place = place;
		this.content = content;
		this.thumbnail = thumbnail;
		this.temp = temp;
		this.createdAt = created_at;
		this.dateStart = date_start;
		this.dateEnd = date_end;
		this.likeCount = likeCount;
		this.userNickname = userNickname;
		this.likearticle = likearticle;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public Long getUser_num() {
		return userNum;
	}

	public void setUser_num(Long user_num) {
		this.userNum = user_num;
	}

	public Long getTrippackage_num() {
		return trippackageNum;
	}

	public void setTrippackage_num(Long tripPackage_num) {
		this.trippackageNum = tripPackage_num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Boolean getTemp() {
		return temp;
	}

	public void setTemp(Boolean temp) {
		this.temp = temp;
	}

	public Date getCreated_at() {
		return createdAt;
	}

	public void setCreated_at(Date create_at) {
		this.createdAt = create_at;
	}

	public Date getDate_start() {
		return dateStart;
	}

	public void setDate_start(Date date_start) {
		this.dateStart = date_start;
	}

	public Date getDate_end() {
		return dateEnd;
	}

	public void setDate_end(Date date_end) {
		this.dateEnd = date_end;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public List<MemberUser> getLikearticle() {
		return likearticle;
	}

	public void setLikearticle(List<MemberUser> likearticle) {
		this.likearticle = likearticle;
	}


	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}
	
	@Override
	public String toString() {
		return "Article [num=" + num + ", usernum=" + userNum + ", trippackage_num=" + trippackageNum + ", title="
				+ title + ", place=" + place + ", content=" + content + ", thumbnail=" + thumbnail + ", temp=" + temp
				+ ", created_at=" + createdAt + ", date_start=" + dateStart + ", date_end=" + dateEnd
				+ ", likeCount=" + likeCount + ", userNickname=" + userNickname + ", likearticle=" + likearticle + ",views="+views +"]";
	}


}
