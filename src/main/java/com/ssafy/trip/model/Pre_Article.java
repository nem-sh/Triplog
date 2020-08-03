package com.ssafy.trip.model;

import java.util.Date;

import lombok.Data;

@Data
public class Pre_Article {
	private Long num;
	private Date date;
	private String comment;
	private String place;
	private String media;
}
