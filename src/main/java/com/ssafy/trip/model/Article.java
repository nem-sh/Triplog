package com.ssafy.trip.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "article")
@Data
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
}
