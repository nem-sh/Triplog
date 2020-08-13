package com.ssafy.trip.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trip.model.PreArticle;
import com.ssafy.trip.repository.PreArticleRepository;

@CrossOrigin(origins = "*")
@RequestMapping("/api/chatbot")
@RestController
public class PreArticleController {
	
	@Autowired
	private PreArticleRepository preArticleRepository;

	@RequestMapping(value = "/kakao" , method= {RequestMethod.POST , RequestMethod.GET },headers = {"Accept=application/json"})
    public HashMap<String,Object> callAPI(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, PreArticle preArticle) {

        HashMap<String, Object> resultJson = new HashMap<>();

        try{

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(params);
            System.out.println(jsonInString);
            
            /* 발화 처리 부분 * */
            HashMap<String,Object> userRequest =  (HashMap<String,Object>)params.get("userRequest");
            String utter = userRequest.get("utterance").toString().replace("\n","");
            
            
           preArticle.setComment(utter);
           preArticleRepository.save(preArticle);
            
            
            /* 발화 처리 끝*/

            List<HashMap<String,Object>> outputs = new ArrayList<>();
            HashMap<String,Object> template = new HashMap<>();
            HashMap<String, Object> simpleText = new HashMap<>();
            HashMap<String, Object> text = new HashMap<>();
            String CompleteMsg = "저장돼었습니다";
            
            
//            text.put("text",CompleteMsg);
            text.put("text",utter);
            simpleText.put("simpleText",text);
            outputs.add(simpleText);
            template.put("outputs",outputs);
            resultJson.put("version","2.0");
            resultJson.put("template",template);

        }catch (Exception e){

        }

        return resultJson;
	}
}
