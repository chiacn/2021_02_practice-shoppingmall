package com.bookshop01.admin.goods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookshop01.goods.vo.GoodsVO;
import com.bookshop01.goods.vo.ImageFileVO;
import com.bookshop01.order.vo.OrderVO;

public interface AdminGoodsService {
	public int  addNewGoods(Map newGoodsMap) throws Exception;
	//public List<GoodsVO> listNewGoods(Map condMap) throws Exception;
	// totArticles 추가버전 (페이징)
	public Map listNewGoods(Map condMap) throws Exception;
	
	public Map goodsDetail(int goods_id) throws Exception;
	public List goodsImageFile(int goods_id) throws Exception;
	public void modifyGoodsInfo(Map goodsMap) throws Exception;
	public void modifyGoodsImage(List<ImageFileVO> imageFileList) throws Exception;
	public List<OrderVO> listOrderGoods(Map condMap) throws Exception;
	public void modifyOrderGoods(Map orderMap) throws Exception;
	public void removeGoodsImage(int image_id) throws Exception;
	public void addNewGoodsImage(List imageFileList) throws Exception;
	
	//상품 삭제 추가
	public void deleteGoods(int goods_id) throws Exception;
}
