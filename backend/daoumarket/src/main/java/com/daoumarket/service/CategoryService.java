package com.daoumarket.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.daoumarket.dto.BasicResponse;

@Service
public class CategoryService implements ICategoryService {

	@Override
	public ResponseEntity<BasicResponse> getCategory() {
		
		BasicResponse response = new BasicResponse();
		
		String[] category = {"디지털/가전", "가구/인테리어", "유아동/유아도서", "생활/가공식품", 
				"스포츠/레저", "여성잡화", "여성의류", "남성패션/잡화", 
				"게임/취미", "뷰티/미용", "반려동물용품", "도서/티켓/음반", "기타", "무료나눔"};
		
		response.status = true;
		response.data = "카테고리 호출 성공";
		response.object = category;
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
