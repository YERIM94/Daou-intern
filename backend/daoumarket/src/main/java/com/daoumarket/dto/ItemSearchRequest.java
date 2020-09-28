package com.daoumarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ItemSearchRequest {
	
	String[] category;
	String keyword;
	
	@Builder
	public ItemSearchRequest(String[] category, String keyword) {
		this.category = category;
		this.keyword = keyword;
	}
}
