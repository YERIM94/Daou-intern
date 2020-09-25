package com.daoumarket.jwt;

import com.daoumarket.dto.UserDto;

public interface IJWTService {
	// Jwt�� �������ִ� �޼ҵ�
	public String makeJwt(UserDto res) throws Exception;
	
	// jwt�� ������ ��ū���� �˻��ϴ� �޼ҵ�
	public UserDto checkJwt(String jwt) throws Exception;

}
