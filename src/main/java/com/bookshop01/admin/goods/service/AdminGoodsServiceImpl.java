package com.bookshop01.admin.goods.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bookshop01.admin.goods.dao.AdminGoodsDAO;
import com.bookshop01.goods.vo.GoodsVO;
import com.bookshop01.goods.vo.ImageFileVO;
import com.bookshop01.order.vo.OrderVO;


@Service("adminGoodsService")
@Transactional(propagation=Propagation.REQUIRED)
public class AdminGoodsServiceImpl implements AdminGoodsService {
	@Autowired
	private AdminGoodsDAO adminGoodsDAO;
	
	@Override
	public int addNewGoods(Map newGoodsMap) throws Exception{
		 //1. newGoodsMap을 통해 받아온 정보 t_shopping_goods 테이블에 insert해주고  goods_id를 반환받는다. 
		int goods_id = adminGoodsDAO.insertNewGoods(newGoodsMap);
		
		//2.newGoodsMap에서 imageFileList를 가져 온 후, 각각의 imageVO에 goods_id를 세팅한다. 
		ArrayList<ImageFileVO> imageFileList = (ArrayList)newGoodsMap.get("imageFileList");
		for(ImageFileVO imageFileVO : imageFileList) {
			imageFileVO.setGoods_id(goods_id); //여기서 추가되는 imageFileVO -> 참조되는거라 따로 저장 안 해도 됨 .
		}
		//3. 갱신된 데이터로  t_goods_detail_image 테이블 업데이트 
		adminGoodsDAO.insertGoodsImageFile(imageFileList);
		return goods_id;
	}
	
	@Override
	/*  원본
	 public List<GoodsVO> listNewGoods(Map condMap) throws Exception{
		return adminGoodsDAO.selectNewGoodsList(condMap);
	}*/
	
	 
	public Map listNewGoods(Map condMap) throws Exception{
		List<GoodsVO> newGoodsList = adminGoodsDAO.selectNewGoodsList(condMap);
		
		//totArticles 추가
		int totArticles = adminGoodsDAO.selectTotArticles(condMap);  // totArticles도 기간 조건검색 하기 위해서 condMap 인자로 넣어줌.
		
		Map pagingMap = new HashMap();
				
		pagingMap.put("newGoodsList", newGoodsList);
		pagingMap.put("totArticles", totArticles);
				
		return pagingMap;
	}
	
	
	@Override
	public Map goodsDetail(int goods_id) throws Exception {
		Map goodsMap = new HashMap();
		GoodsVO goodsVO=adminGoodsDAO.selectGoodsDetail(goods_id);
		List imageFileList =adminGoodsDAO.selectGoodsImageFileList(goods_id);
		goodsMap.put("goods", goodsVO);
		goodsMap.put("imageFileList", imageFileList);
		return goodsMap;
	}
	@Override
	public List goodsImageFile(int goods_id) throws Exception{
		List imageList =adminGoodsDAO.selectGoodsImageFileList(goods_id);
		return imageList;
	}
	
	@Override
	public void modifyGoodsInfo(Map goodsMap) throws Exception{
		adminGoodsDAO.updateGoodsInfo(goodsMap);
		
	}	
	@Override
	public void modifyGoodsImage(List<ImageFileVO> imageFileList) throws Exception{
		adminGoodsDAO.updateGoodsImage(imageFileList); 
	}
	
	@Override
	public List<OrderVO> listOrderGoods(Map condMap) throws Exception{
		return adminGoodsDAO.selectOrderGoodsList(condMap);
	}
	@Override
	public void modifyOrderGoods(Map orderMap) throws Exception{
		adminGoodsDAO.updateOrderGoods(orderMap);
	}
	
	@Override
	public void removeGoodsImage(int image_id) throws Exception{
		adminGoodsDAO.deleteGoodsImage(image_id);
	}
	
	@Override
	public void addNewGoodsImage(List imageFileList) throws Exception{
		adminGoodsDAO.insertGoodsImageFile(imageFileList);
	}
	
	//상품 삭제 구현 추가
	@Override
	public void deleteGoods(int goods_id) throws Exception {
		adminGoodsDAO.deleteGoods(goods_id);
	}
	

	
}
