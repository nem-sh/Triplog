package com.ssafy.trip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import com.ssafy.trip.controller.chatbot;



@SpringBootApplication
@EntityScan(basePackageClasses = {
		TripApplication.class,
		Jsr310JpaConverters.class
})
public class TripApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
//		텔래그랩 챗봇 
//		ApiContextInitializer.init();
//		TelegramBotsApi api = new TelegramBotsApi();
//		try {
//			api.registerBot(new chatbot());
//			System.out.println("챗봇시작");
//			
//		}catch (TelegramApiRequestException e) {
//			e.printStackTrace();
//		}
//		
		SpringApplication.run(TripApplication.class, args);
	}
}