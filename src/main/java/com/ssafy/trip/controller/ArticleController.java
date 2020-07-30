package com.ssafy.trip.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.ssafy.trip.exception.ResourceNotFoundException;
import com.ssafy.trip.help.ArticleLikeListResponseObject;
import com.ssafy.trip.help.ArticleRequestWithFile;
import com.ssafy.trip.model.Article;
import com.ssafy.trip.model.MemberUser;
import com.ssafy.trip.model.Paging;
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
	
	@PostMapping
	public ResponseEntity<String> postArticle(@RequestBody Article article) {
		articleRepository.save(article);
		
		return ResponseEntity.ok(SUCCESS);
	}

	@PostMapping("/img")
	public ResponseEntity<String> uploadImgs(@RequestPart MultipartFile img) throws Exception {
		String baseDir = System.getProperty("user.dir")+ "\\frontend\\src\\assets\\articleImage\\";
		String originalFileName = img.getOriginalFilename();
		System.out.println(originalFileName);
		File dest = new File(baseDir + originalFileName);
		
		String newName = originalFileName;
		String realName = originalFileName.split("\\.")[0];
		String extension = originalFileName.split("\\.")[1];
		int index = 0;
		while(dest.exists()) {
			index++;
			newName = realName + "(" + index + ")." + extension;
			dest = new File(baseDir + newName);
		}
		
		img.transferTo(dest);
	
		return ResponseEntity.ok(newName);
	}

	@GetMapping("/getList/{hostNum}")
	public List<Article> findArticlesByHostNum(@PathVariable(value = "hostNum") Long hostNum) {
		List<Article> list = articleRepository.findByUsernum(hostNum);
		return list;
	}
	
	@PostMapping("/getList")
	public List<Article> findArticlesByPaging(@RequestBody Paging paging) {
		List<Article> list = articleRepository.findByUsernumPaging(paging.getUsernum(), paging.getLimit());
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
	
	@PostMapping("/likelist")
	public List<ArticleLikeListResponseObject> findArticleLikeList(@RequestBody Paging paging){
		MemberUser user =  userRepository.findByNum(paging.getUsernum())
    			.orElseThrow(() -> new ResourceNotFoundException("User", "usernum", paging.getUsernum()));
		
		
		
		List<Article> articles = articleRepository.findByLikearticle(user);
		List<ArticleLikeListResponseObject> objs = new ArrayList<ArticleLikeListResponseObject>();
		
		MemberUser writer = null;
		for(Article article : articles) {
			writer =  userRepository.findByNum(article.getUser_num())
	    			.orElseThrow(() -> new ResourceNotFoundException("User", "num", article.getUser_num()));
			
			objs.add(new ArticleLikeListResponseObject(article, writer));
		}
		
		if(paging.getLimit() > objs.size())
			return null;
		int max = paging.getLimit()+9;
		if(max > objs.size())
			max = objs.size();
		List<ArticleLikeListResponseObject> list = objs.subList(paging.getLimit(), max);
		
		return list;
	}
	
	@DeleteMapping("/likelist/{usernum}/{num}")
	public ResponseEntity<String> DeleteArticleLikeList(@PathVariable(value = "usernum") Long usernum,@PathVariable(value = "num") Long num){
		MemberUser user =  userRepository.findByNum(usernum)
    			.orElseThrow(() -> new ResourceNotFoundException("User", "usernum", usernum));
		Article article =  articleRepository.findByNum(num)
    			.orElseThrow(() -> new ResourceNotFoundException("Article", "num", num));
		
		List<MemberUser> users = article.getLikearticle();
		article.setLikeCount(article.getLikeCount()-1);
		users.remove(user);
		
		article.setLikearticle(users);
		articleRepository.save(article);
		
		return ResponseEntity.ok(SUCCESS);
	}
	
	@PutMapping("/likelist/{usernum}/{num}")
	public ResponseEntity<String> UpdateArticleLikeList(@PathVariable(value = "usernum") Long usernum,@PathVariable(value = "num") Long num){
		MemberUser user =  userRepository.findByNum(usernum)
    			.orElseThrow(() -> new ResourceNotFoundException("User", "usernum", usernum));
		Article article =  articleRepository.findByNum(num)
    			.orElseThrow(() -> new ResourceNotFoundException("Article", "num", num));
		
		List<MemberUser> users = article.getLikearticle();
		article.setLikeCount(article.getLikeCount()+1);
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
	
	@PostMapping("/uploadWithFile")
	public String registArticleByFile(@RequestBody MultipartFile img) throws IOException {
		String imgName = img.getOriginalFilename();
		Article article = new Article();
		article.setThumbnail(imgName);
		
		File url = new File("images/"+imgName);
		url.createNewFile();
		FileOutputStream fout = new FileOutputStream(url);
		fout.write(img.getBytes());
		fout.close();
		return "ok";
	}
}
