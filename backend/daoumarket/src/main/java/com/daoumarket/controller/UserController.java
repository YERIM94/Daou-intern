package com.daoumarket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.daoumarket.dto.BasicResponse;
import com.daoumarket.dto.UserDto;
import com.daoumarket.jwt.IJWTService;
import com.daoumarket.service.IUserService;
import com.daoumarket.util.EncodePassword;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class UserController {
	
	private final IUserService userService;
	private final IJWTService jwtService;
	
	// sign up
	@PostMapping("/api/user")
	@ApiOperation("ȸ������")
	public ResponseEntity<BasicResponse> insertUser(@RequestBody UserDto dto) {
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		
		UserDto encodePasswordDto = EncodePassword.Encode(dto);
		
		int res = userService.insertUser(encodePasswordDto);

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
	
	
	@GetMapping("/api/{num}")
	@ApiOperation("���̵� �ߺ� üũ")
	public ResponseEntity<BasicResponse> getNum(@PathVariable long num) {
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		
		UserDto res = userService.getNum(num);
		
		if (res == null) {
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
	@PostMapping("/api/login")
	@ApiOperation("�α���")
	public ResponseEntity<BasicResponse> getUserLogin(@RequestBody UserDto dto){
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		
		UserDto encodePasswordDto = EncodePassword.Encode(dto);
		UserDto res = userService.getUserLogin(encodePasswordDto);
		
		if (res == null) {
			basicResponse.status = false;
			basicResponse.data = "Discorrect";
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
		} else {
			basicResponse.status = true;
			basicResponse.data = "Correct";
			
			try {
				String token = jwtService.makeJwt(res);
				
				basicResponse.object = token;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);		
		}
		
		return responseEntity;
	}
	
	@PostMapping("/api/token")
	@ApiOperation("��ū ����")
	public ResponseEntity<BasicResponse> token(@RequestBody String accessToken){
		ResponseEntity<BasicResponse> responseEntity = null;
		BasicResponse basicResponse = new BasicResponse();
		UserDto jwt = null;

		try {
			jwt = jwtService.checkJwt(accessToken);
			
			if (jwt == null) {
				basicResponse.status = false;
				basicResponse.data = "Token Mismatch";
				responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
			
			} else {
				basicResponse.status = true;
				basicResponse.data = "Token Match";
				basicResponse.object = jwt;
				responseEntity = new ResponseEntity<BasicResponse>(basicResponse, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseEntity;
		
	}
	
}
