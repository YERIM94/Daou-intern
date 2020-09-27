package com.daoumarket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daoumarket.dto.BasicResponse;
import com.daoumarket.dto.User;
import com.daoumarket.jwt.IJWTService;
import com.daoumarket.service.IUserService;
import com.daoumarket.util.EncodePassword;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class UserController {
	
	private final IUserService userService;
	private final IJWTService jwtService;
	
	// sign up
	@PostMapping("/user")
	@ApiOperation("회원가입")
	public ResponseEntity<BasicResponse> insertUser(@RequestBody User user) {
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		
		User encodePassword = EncodePassword.Encode(user);
		
		int res = userService.insertUser(encodePassword);

		if (res > 0) {
			basicResponse.status = true;
			basicResponse.data = "Success in signup";
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
			
		} else {
			basicResponse.status = false;
			basicResponse.data = "Fail in signup";
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
		}
		
		return responseEntity;
	}
	
	
	@GetMapping("/{num}")
	@ApiOperation("사번 중복 체크")
	public ResponseEntity<BasicResponse> getNum(@PathVariable long num) {
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		
		User userRes = userService.getNum(num);
		
		if (userRes == null) {
			basicResponse.status = false;
			basicResponse.data = "No Duplication of Employee Number";
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
			
		} else {
			basicResponse.status = true;
			basicResponse.data = "Duplication of Employee Number";
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
		}
		
		return responseEntity;
	}
	
	// login
	@PostMapping("/login")
	@ApiOperation("로그인")
	public ResponseEntity<BasicResponse> getUserLogin(@RequestBody User user){
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		
		User encodePassword = EncodePassword.Encode(user);
		User userRes = userService.getUserLogin(encodePassword);
		
		if (userRes == null) {
			basicResponse.status = false;
			basicResponse.data = "Discorrect";
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
		} else {
			basicResponse.status = true;
			basicResponse.data = "Correct";
			
			try {
				String token = jwtService.makeJwt(userRes);
				
				basicResponse.object = token;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);		
		}
		
		return responseEntity;
	}
	
	@PostMapping("/token")
	@ApiOperation("토큰 검증")
	public ResponseEntity<BasicResponse> token(@RequestBody String accessToken){
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		User userJwt = null;

		try {
			userJwt = jwtService.checkJwt(accessToken);
			
			if (userJwt == null) {
				basicResponse.status = false;
				basicResponse.data = "Token Mismatch";
				responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
			
			} else {
				basicResponse.status = true;
				basicResponse.data = "Token Match";
				basicResponse.object = userJwt;
				responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseEntity;
	}
	
	// edit user
	@PostMapping("/edit")
	@ApiOperation("정보수정")
	public ResponseEntity<BasicResponse> updateUser(@RequestBody User user){
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		
		User encodePassword = EncodePassword.Encode(user);
		int res = userService.updateUser(encodePassword);
		
		if (res > 0) {
			
			try {
				String token = jwtService.makeJwt(encodePassword);
				
				basicResponse.object = token;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			basicResponse.status = true;
			basicResponse.data = "Modify";
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
			
		} else {
			basicResponse.status = false;
			basicResponse.data = "Unable to Modify";
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
		}
		
		return responseEntity;
	}
	
}
