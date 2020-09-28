package com.daoumarket.service;

import org.springframework.http.ResponseEntity;

import com.daoumarket.dto.BasicResponse;
import com.daoumarket.dto.SearchInsertResponse;

public interface ISearchService {
	public ResponseEntity<BasicResponse> insertSearch(SearchInsertResponse searchInsertResponse);
}
