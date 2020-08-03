package com.ssafy.trip.controller;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.help.SearchArticleResponse;
import com.ssafy.trip.model.Article;
import com.ssafy.trip.model.MemberUser;
import com.ssafy.trip.repository.ArticleRepository;
import com.ssafy.trip.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/search")
public class ArticleSearchController {
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private UserRepository userRepository;

	@GetMapping(value = { "/{user_num}/article/{keyword}" })
	public List<Article> blogSearch(@PathVariable(value = "user_num") int user_num,
			@PathVariable(value = "keyword") String keyword) {
		
		List<Article> blogSearch = articleRepository.findByTitleContainingAndNum(keyword, user_num);
		return blogSearch;
	}
	
//	제목만 검색 리스트
	@GetMapping("/searchArticle/{keyword}")
	public List<SearchArticleResponse> responseList(@PathVariable(value = "keyword") String keyword) {
		
		List<Article> searchArticle = articleRepository.findByTitleContaining(keyword);
		List<SearchArticleResponse> responseList = new LinkedList<SearchArticleResponse>();
		for (Article article : searchArticle) {
			
			String nickname = userRepository.findByNum(article.getUser_num()).get().getNickname();
			System.out.println(nickname);
			responseList.add(new SearchArticleResponse(article.getNum(), article.getUser_num(), article.getTrippackage_num(), article.getTitle(), article.getPlace(), article.getContent(), article.getThumbnail(), article.getTemp(), article.getCreated_at(), article.getDate_start(), article.getDate_end(), article.getLikeCount(), nickname));
		}
 
		return responseList;
	}
// 닉네임 검색 리스트
	@GetMapping("/searchUserArticle/{selected}")
	public List<SearchArticleResponse> searchArticleAuth(@PathVariable(value = "selected") String selected) {
		List<MemberUser> searchUsers = userRepository.findByNicknameContaining(selected);
		List<Article> searchArticleAuth = new ArrayList<Article>();
		List<SearchArticleResponse> responseAuthList = new LinkedList<SearchArticleResponse>();
		for (MemberUser searchUser : searchUsers) {
			searchArticleAuth.addAll(articleRepository.findByUsernum(searchUser.getNum()));
		}
		for(Article article: searchArticleAuth) {
			String nickname = userRepository.findByNum(article.getUser_num()).get().getNickname();
			responseAuthList.add(new SearchArticleResponse(article.getNum(), article.getUser_num(), article.getTrippackage_num(), article.getTitle(), article.getPlace(), article.getContent(), article.getThumbnail(), article.getTemp(), article.getCreated_at(), article.getDate_start(), article.getDate_end(), article.getLikeCount(), nickname));
		}
		return responseAuthList;
	}
	
	@GetMapping("/searchArticleUser/{keyword}")
	public List<MemberUser> userSearch(@PathVariable(value="keyword") String keyword) {
	List<MemberUser> Users = userRepository.findByNicknameContaining(keyword);
	return Users;
	}
//	유저num으로 유저의 닉네임 반
	@GetMapping("/articleUser/{user_num}")
	public Optional<MemberUser> user(@PathVariable(value="user_num") Long user_num) {
		Optional<MemberUser> user = userRepository.findByNum(user_num);
		System.out.println(user.get().getNickname());
		System.out.println(user.getClass().getName());
		return user;
	}
}
