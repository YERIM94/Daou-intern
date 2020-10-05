package com.daoumarket.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.daoumarket.dto.BasicResponse;
import com.daoumarket.dto.ItemInfoRequest;
import com.daoumarket.dto.ItemInsertRequest;
import com.daoumarket.dto.ItemSearchRequest;
import com.daoumarket.dto.ItemUpdateRequest;
import com.daoumarket.service.IItemService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ItemController {
	
	private final IItemService itemService;
	
	@GetMapping("/item/{itemId}")
	@ApiOperation("물건 상세정보 조회")
	public ResponseEntity<BasicResponse> getItemInfoByItemId(@PathVariable(required = true) long itemId, 
															@RequestParam(required = true) int userId) {
		log.info("ItemController : getItemInfoByItemId => {}", itemId);
		
		ItemInfoRequest itemInfoRequest = ItemInfoRequest.builder()
				.itemId(itemId)
				.userId(userId).build();
		
		return itemService.getItemInfoByItemId(itemInfoRequest);
	}
	
	@PostMapping(path = "/item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation("물건 등록")
	public ResponseEntity<BasicResponse> insertItem(@RequestParam int userId, @RequestParam String title, @RequestParam int price,
													@RequestParam String category, @RequestParam String content,
													@RequestPart(required = false) MultipartFile[] images) {
		log.info("ItemController : insertItem");
		
		ItemInsertRequest item = ItemInsertRequest.builder()
				.userId(userId)
				.title(title)
				.price(price)
				.category(category)
				.content(content).build();
		
		return itemService.insertItem(item, images);
	}
	
	@PatchMapping("/item/info")
	@ApiOperation("물건 정보 수정하기")
	public ResponseEntity<BasicResponse> updateItemInfo(@RequestBody ItemUpdateRequest item) {
		log.info("ItemController : updateItemInfo");
		
		return itemService.updateItemInfo(item);
	}
	
	@PatchMapping("/item/status")
	@ApiOperation("물건 상태 수정하기(판매중, 거래중, 거래완료)")
	public ResponseEntity<BasicResponse> updateItemStatus(@RequestBody ItemUpdateRequest item) {
		log.info("ItemController : updateItemStatus");
		
		return itemService.updateItemStatus(item);
	}
	
	@DeleteMapping("/item/{itemId}")
	@ApiOperation("물건 삭제하기")
	public ResponseEntity<BasicResponse> deleteItem(@PathVariable long itemId) {
		log.info("ItemController : deleteItem");
		
		return itemService.deleteItem(itemId);
	}
	
	@GetMapping("/item")
	@ApiOperation("모든 물건 가져오기")
	public ResponseEntity<BasicResponse> getAllItems(@RequestParam(required = true) long userId) {
		log.info("ItemController : getAllItems");
		
		return itemService.getAllItems(userId);
	}
	
	@GetMapping("/item/category")
	@ApiOperation("선택된 카테고리의 물건 가져오기")
	public ResponseEntity<BasicResponse> getItemsByCategory(@RequestParam String[] category) {
		log.info("ItemController : getItemsByCategory");
		
		ItemSearchRequest search = ItemSearchRequest.builder()
				.category(category).build();
		
		return itemService.getItemsByCategory(search);
	}
	
	@GetMapping("/item/keyword")
	@ApiOperation("키워드 물건 가져오기")
	public ResponseEntity<BasicResponse> getItemsByKeyword(@RequestParam String[] category, @RequestParam String keyword) {
		log.info("ItemController : getItemsByKeyword");
		
		ItemSearchRequest search = ItemSearchRequest.builder()
				.category(category)
				.keyword(keyword).build();
		
		return itemService.getItemsByKeyword(search);
	}
	
	@GetMapping("/item/{userId}/list")
	@ApiOperation("id를 가진 유저의 게시물 가져오기")
	public ResponseEntity<BasicResponse> getItemsByUserId(@PathVariable long userId) {
		log.info("ItemController : getItemsByUserId => {}", userId);
		
		return itemService.getItemsByUserId(userId);
	}

}
