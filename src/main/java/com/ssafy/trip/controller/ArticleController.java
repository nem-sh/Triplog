package com.ssafy.trip.controller;

import java.util.Collections;
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

	@GetMapping("/getList/{hostNum}")
	public List<Article> findArticlesByHostNum(@PathVariable(value = "hostNum") Long hostNum) {
		List<Article> list = articleRepository.findByUsernum(hostNum);
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
	
	//좋아요 기능 -남시성
	
	@GetMapping("/likelist/{email}")
	public List<Article> findArticleLikeList(@PathVariable(value = "email") String email){
		
		MemberUser user =  userRepository.findByEmail(email)
    			.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
		
	
		
		List<Article> articles = articleRepository.findByLikearticle(user);
		return articles;
	}
	
	@DeleteMapping("/likelist/{email}/{num}")
	public ResponseEntity<String> DeleteArticleLikeList(@PathVariable(value = "email") String email,@PathVariable(value = "num") Long num){
		MemberUser user =  userRepository.findByEmail(email)
    			.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
		Article article =  articleRepository.findByNum(num)
    			.orElseThrow(() -> new ResourceNotFoundException("Article", "num", num));
		
		List<MemberUser> users = article.getLikearticle();
		
		users.remove(user);
		
		article.setLikearticle(users);
		articleRepository.save(article);
		
		return ResponseEntity.ok(SUCCESS);
	}
	
	@PutMapping("/likelist/{email}/{num}")
	public ResponseEntity<String> UpdateArticleLikeList(@PathVariable(value = "email") String email,@PathVariable(value = "num") Long num){
		MemberUser user =  userRepository.findByEmail(email)
    			.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
		Article article =  articleRepository.findByNum(num)
    			.orElseThrow(() -> new ResourceNotFoundException("Article", "num", num));
		
		List<MemberUser> users = article.getLikearticle();
		
		users.add(user);
		
		article.setLikearticle(users);
		articleRepository.save(article);
		
		
		return ResponseEntity.ok(SUCCESS);
	}
	//좋아요 기능 - 남시성
	
	@GetMapping("/like/{articleNum}/{userNum}")
	public ResponseEntity<Boolean> getIsLike(@PathVariable(value = "userNum") Long userNum,
			@PathVariable(value = "articleNum") Long articleNum) {
		MemberUser user = userRepository.findByNum(userNum)
				.orElseThrow(() -> new ResourceNotFoundException("User", "num", userNum));

		Boolean isLike = false;
		List<Article> articles = articleRepository.findByLikearticle(user);
		for (Article article : articles) {
			if (article.getNum() == articleNum)
				isLike = true;
		}

		return ResponseEntity.ok(isLike);
	}

	@PutMapping("/{num}/{userNum}/{flag}")
	public ResponseEntity<String> modifyLikeInfoInArticle(@PathVariable(value = "userNum") Long userNum,
			@PathVariable(value = "num") Long num, @PathVariable(value = "flag") boolean flag) {
		
		Article article =  articleRepository.findByNum(num)
    			.orElseThrow(() -> new ResourceNotFoundException("Article", "num", num));
		
		MemberUser user = userRepository.findByNum(userNum)
				.orElseThrow(() -> new ResourceNotFoundException("User", "num", userNum));

		List<MemberUser> users = article.getLikearticle();

		if(flag) users.add(user);
		else users.remove(user);
		if (flag) {
			article.setLikeCount(article.getLikeCount()+1);
		} else {
			article.setLikeCount(article.getLikeCount()-1);
		}
		article.setLikearticle(users);
		articleRepository.save(article);

		return ResponseEntity.ok(SUCCESS);
	}
}
