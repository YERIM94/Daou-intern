package com.daoumarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
public class Image {

	long id;
	String image;
	String[] picture;
	
	@Builder
	public Image(long id, String image, String[] picture) {
		this.id = id;
		this.image = image;
		this.picture = picture;
	}
	
}
