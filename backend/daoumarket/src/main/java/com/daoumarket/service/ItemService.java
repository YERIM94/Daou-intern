package com.daoumarket.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daoumarket.dao.IItemDao;
import com.daoumarket.dto.BasicResponse;
import com.daoumarket.dto.ItemInfoRequest;
import com.daoumarket.dto.ItemInsertRequest;
import com.daoumarket.dto.ItemResponse;
import com.daoumarket.dto.ItemSearchRequest;
import com.daoumarket.dto.ItemUpdateRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemService implements IItemService {

	private final IItemDao itemDao;
	private final IImageService imageService;
	
	@Override
	public ResponseEntity<BasicResponse> getItemInfoByItemId(ItemInfoRequest itemInfoRequest) {
		
		BasicResponse response = new BasicResponse();
		
		ItemResponse item = itemDao.getItemInfoByItemId(itemInfoRequest);
		
		if(item != null) {
			imageService.setItemImages(item);
			response.status = true;
			response.data = "물건 정보를 가져옴";
			response.object = item;
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		response.data = "물건을 찾을 수 없음";
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Transactional
	@Override
	public ResponseEntity<BasicResponse> insertItem(ItemInsertRequest item, MultipartFile[] images) {
		
		BasicResponse response = new BasicResponse();
		int resultCnt = (images.length == 0) ? 0 : 2;
		int id = itemDao.insertItem(item);
		
		if(id > 0) {
			resultCnt++; 
			if(images.length > 0 && imageService.insertItemImage(images, id) > 0) { // 이미지 파일이 존재하면 이미지 업로드 진행
				resultCnt++;
			}
		}
		
		switch (resultCnt) {
		case 0:
			// 게시물 등록 실패(이미지 파일 X)
			response.data = "게시물 등록 실패(이미지 파일 X)";
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		case 1:
			// 게시물 등록 성공(이미지 파일 X)
			response.status = true;
			response.data = "게시물 등록 성공!(이미지 파일 X)";
			return new ResponseEntity<>(response, HttpStatus.OK);
		case 2:
			// 게시물 등록 실패(이미지 파일 O)
			response.data = "게시물 등록 실패(이미지 파일 O)";
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		case 3:
			// 게시물 등록 성공했으나, 이미지 등록 실패(이미지 파일 O)
			response.data = "게시물 등록 성공했으나, 이미지 등록 실패(이미지 파일 O)";
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		default :
			response.status = true;
			response.data = "게시물과 등록 성공!(이미지 파일 O)";
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}
	
	@Transactional
	@Override
	public ResponseEntity<BasicResponse> updateItemInfo(ItemUpdateRequest item) {
		
		BasicResponse response = new BasicResponse();
		
		if(itemDao.updateItemInfo(item) == 1) {
			response.status = true;
			response.data = "물건 정보 수정 성공";
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		response.data = "물건 정보 수정 실패";
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@Transactional
	@Override
	public ResponseEntity<BasicResponse> updateItemStatus(ItemUpdateRequest item) {
		
		BasicResponse response = new BasicResponse();
		
		if(itemDao.updateItemStatus(item) == 1) {
			response.status = true;
			response.data = "물건 상태 수정 성공";
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		response.data = "물건 상태 수정 실패";
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Transactional
	@Override
	public ResponseEntity<BasicResponse> deleteItem(long id) {
		
		BasicResponse response = new BasicResponse();
		
		if(itemDao.deleteItem(id) == 1) {
			response.status = true;
			response.data = "물건 삭제 성공";
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		response.data = "물건 삭제 실패";
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<BasicResponse> getAllItems(long userId) {
		
		BasicResponse response = new BasicResponse();
		
		List<ItemResponse> items = itemDao.getAllItems();
		
		if(!items.isEmpty()) {
			for (ItemResponse item : items) {
				imageService.setItemImages(item);
			}
			response.status = true;
			response.data = "물건 가져오기 성공";
			response.object = items;
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		response.data = "물건이 존재하지 않음";
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<BasicResponse> getItemsByCategory(ItemSearchRequest search) {
		
		BasicResponse response = new BasicResponse();
		
		
		List<ItemResponse> items = itemDao.getItemsByCategory(search);
		
		if(!items.isEmpty()) {
			for (ItemResponse item : items) {
				imageService.setItemImages(item);
			}
			response.status = true;
			response.data = "물건 가져오기 성공";
			response.object = items;
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		response.data = "물건이 존재하지 않음";
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<BasicResponse> getItemsByKeyword(ItemSearchRequest search) {
		
		BasicResponse response = new BasicResponse();
		
		if(search.getCategory() == null) { // 카테고리가 선택되어 있지 않은 경우
			List<ItemResponse> items = itemDao.getItemsByKeyword(search);
			if(items != null) {
				for (ItemResponse item : items) {
					imageService.setItemImages(item);
				}
				response.status = true;
				response.data = "물건 가져오기 성공";
				response.object = items;
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			response.data = "물건이 존재하지 않음";
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} else { // 카테고리가 선택되어 있는 경우
			List<ItemResponse> items = itemDao.getItemsByCategoryAndKeyword(search);
			if(items != null) {
				for (ItemResponse item : items) {
					imageService.setItemImages(item);
				}
				response.status = true;
				response.data = "물건 가져오기 성공";
				response.object = items;
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			response.data = "물건이 존재하지 않음";
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<BasicResponse> getItemsByUserId(long userId) {
		BasicResponse response = new BasicResponse();
		
		List<ItemResponse> items = itemDao.getItemsByUserId(userId);
		
		if(items != null) {
			for (ItemResponse item : items) {
				imageService.setItemImages(item);
			}
			response.status = true;
			response.data = "물건 가져오기 성공";
			response.object = items;
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		response.data = "물건이 존재하지 않음";
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
