package com.ssafy.trip.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.exception.ResourceNotFoundException;
import com.ssafy.trip.model.Article;
import com.ssafy.trip.model.MemberUser;
import com.ssafy.trip.repository.ArticleRepository;
import com.ssafy.trip.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/article")
public class ArticleController {

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/{num}")
	public ResponseEntity<Article> getArticleByNum(@PathVariable(value = "num") Long num) {
		Article article = articleRepository.findByNum(num)
				.orElseThrow(() -> new ResourceNotFoundException("Article", "num", num));

		return ResponseEntity.ok(article);
	}

	@DeleteMapping("/{num}")
	public ResponseEntity<String> deleteArticleByNum(@PathVariable(value = "num") Long num) {
		articleRepository.deleteByNum(num);

		return ResponseEntity.ok(SUCCESS);
	}

	@PutMapping("/{num}")
	public ResponseEntity<String> modifyArticleByNum(@PathVariable(value = "num") Long num,
			@RequestBody Article article) {
		articleRepository.save(article);

		return ResponseEntity.ok(SUCCESS);
	}

	@PostMapping("/post")
	public ResponseEntity<String> registArticleByNum(@RequestBody Article article) {
		articleRepository.save(article);

		return ResponseEntity.ok(SUCCESS);
	}

	@GetMapping("/getList")
	public List<Article> findAllArticles() {
		List<Article> list = articleRepository.findAll();
		System.out.println(list.get(0));
		return list;
	}

	@GetMapping("/searchArticle/{keyword}")
	public List<Article> searchArticle(@PathVariable(value = "keyword") String keyword) {
		System.out.println("11");
		List<Article> searchArticle = articleRepository.findByTitleContaining(keyword);
		System.out.println(keyword);

		return searchArticle;
	}

	@GetMapping("/like/{articleNum}/{email}")
	public ResponseEntity<Boolean> getIsLike(@PathVariable(value = "email") String email,
			@PathVariable(value = "articleNum") Long articleNum) {
		MemberUser user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

		Boolean isLike = false;
		List<Article> articles = articleRepository.findByLikearticle(user);
		for (Article article : articles) {
			if (article.getNum() == articleNum)
				isLike = true;
		}

		return ResponseEntity.ok(isLike);
	}

	@PutMapping("/article/{num}/{email}/{flag}")
	public ResponseEntity<String> modifyLikeInfoInArticle(@PathVariable(value = "email") String email,
			@PathVariable(value = "num") Long num, @PathVariable(value = "flag") boolean flag,
			@RequestBody Article article) {
		MemberUser user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

		List<MemberUser> users = article.getLikearticle();

		if(flag) users.add(user);
		else users.remove(user);

		article.setLikearticle(users);
		articleRepository.save(article);

		return ResponseEntity.ok(SUCCESS);
	}
}
