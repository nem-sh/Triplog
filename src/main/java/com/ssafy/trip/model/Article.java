package com.ssafy.trip.model;

import java.util.Date;

import lombok.Data;

@Data
public class Article {

	private Long num;
	private String title;
	private String place;
	private String content;
	private String thumbnail;
	private Boolean temp;
	private Date create_at;
	private Date date_start;
	private Date date_end;
}
