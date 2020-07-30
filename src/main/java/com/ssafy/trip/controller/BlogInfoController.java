package com.ssafy.trip.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.trip.model.BlogInfo;
import com.ssafy.trip.repository.BlogInfoRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BlogInfoController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	@Autowired
	private BlogInfoRepository blogInfoRepository;
	@PutMapping("/blog/img")
	public ResponseEntity<String> uploadImgs(@RequestPart MultipartFile img) throws Exception {
		String baseDir = System.getProperty("user.dir")+ "\\frontend\\src\\assets\\blogImage\\";
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
		System.out.println(dest);
	
		return ResponseEntity.ok(newName);
	}
	
	@PutMapping("/blog/")
	public ResponseEntity<String> putBlogInfo(@RequestBody BlogInfo blogInfoData) {
		
		BlogInfo blogInfo = blogInfoRepository.findByUsernum(blogInfoData.getUsernum());
		if (blogInfoData.getTitleimg() != null) {
			blogInfo.setTitleimg(blogInfoData.getTitleimg());
		}
		if (blogInfoData.getTitle() != null) {
			blogInfo.setTitle(blogInfoData.getTitle());
		}
		
		
		blogInfoRepository.save(blogInfo);
		
		return ResponseEntity.ok(SUCCESS);
	}
	
	@GetMapping("/blog/{usernum}")
	public BlogInfo getBlogInfo(@PathVariable(value = "usernum") Long usernum) {
		

		
		if (blogInfoRepository.existsByUsernum(usernum) ==false) {
			
			BlogInfo blogInfo = new BlogInfo();
			blogInfo.setUsernum(usernum);
			blogInfo.setVisitcount(0);
			blogInfoRepository.save(blogInfo);
		}

		BlogInfo blogInfo = blogInfoRepository.findByUsernum(usernum);
		
		
		
		return blogInfo;
	}
	@GetMapping("/basedir")
	public String getBaseDir() {
		
		String baseDir = System.getProperty("user.dir")+ "\\frontend\\src\\assets\\";
		
		
		
		
		
		return baseDir;
	}
}
