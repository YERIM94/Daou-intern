package com.daoumarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ItemInsertRequest {
	
	long id;
	long userId;
	String title;
	int price;
	String category;
	String content;
	
	@Builder
	public ItemInsertRequest(long id, long userId, String title, int price, String category, String content) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.price = price;
		this.category = category;
		this.content = content;
	}
}
