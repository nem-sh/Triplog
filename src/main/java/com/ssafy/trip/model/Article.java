package com.ssafy.trip.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "article")
//@Data
public class Article {

	@Id
	private Long num;
	private Long user_num;
	private Long trippackage_num;
	private String title;
	private String place;
	private String content;
	private String thumbnail;
	private Boolean temp;
	private Date created_at;
	private Date date_start;
	private Date date_end;

	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Article(Long num, Long user_num, Long trippackage_num, String title, String place, String content,
			String thumbnail, Boolean temp, Date created_at, Date date_start, Date date_end) {
		super();
		this.num = num;
		this.user_num = user_num;
		this.trippackage_num = trippackage_num;
		this.title = title;
		this.place = place;
		this.content = content;
		this.thumbnail = thumbnail;
		this.temp = temp;
		this.created_at = created_at;
		this.date_start = date_start;
		this.date_end = date_end;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public Long getUser_num() {
		return user_num;
	}

	public void setUser_num(Long user_num) {
		this.user_num = user_num;
	}

	public Long getTrippackage_num() {
		return trippackage_num;
	}

	public void setTrippackage_num(Long trippackage_num) {
		this.trippackage_num = trippackage_num;
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
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getDate_start() {
		return date_start;
	}

	public void setDate_start(Date date_start) {
		this.date_start = date_start;
	}

	public Date getDate_end() {
		return date_end;
	}

	public void setDate_end(Date date_end) {
		this.date_end = date_end;
	}

	@Override
	public String toString() {
		return "Article [num=" + num + ", user_num=" + user_num + ", trippackage_num=" + trippackage_num + ", title="
				+ title + ", place=" + place + ", content=" + content + ", thumbnail=" + thumbnail + ", temp=" + temp
				+ ", created_at=" + created_at + ", date_start=" + date_start + ", date_end=" + date_end + "]";
	}

}
