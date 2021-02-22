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
		 //1. newGoodsMap�� ���� �޾ƿ� ���� t_shopping_goods ���̺� insert���ְ�  goods_id�� ��ȯ�޴´�. 
		int goods_id = adminGoodsDAO.insertNewGoods(newGoodsMap);
		
		//2.newGoodsMap���� imageFileList�� ���� �� ��, ������ imageVO�� goods_id�� �����Ѵ�. 
		ArrayList<ImageFileVO> imageFileList = (ArrayList)newGoodsMap.get("imageFileList");
		for(ImageFileVO imageFileVO : imageFileList) {
			imageFileVO.setGoods_id(goods_id); //���⼭ �߰��Ǵ� imageFileVO -> �����Ǵ°Ŷ� ���� ���� �� �ص� �� .
		}
		//3. ���ŵ� �����ͷ�  t_goods_detail_image ���̺� ������Ʈ 
		adminGoodsDAO.insertGoodsImageFile(imageFileList);
		return goods_id;
	}
	
	@Override
	/*  ����
	 public List<GoodsVO> listNewGoods(Map condMap) throws Exception{
		return adminGoodsDAO.selectNewGoodsList(condMap);
	}*/
	
	 
	public Map listNewGoods(Map condMap) throws Exception{
		List<GoodsVO> newGoodsList = adminGoodsDAO.selectNewGoodsList(condMap);
		
		//totArticles �߰�
		int totArticles = adminGoodsDAO.selectTotArticles(condMap);  // totArticles�� �Ⱓ ���ǰ˻� �ϱ� ���ؼ� condMap ���ڷ� �־���.
		
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
	
	//��ǰ ���� ���� �߰�
	@Override
	public void deleteGoods(int goods_id) throws Exception {
		adminGoodsDAO.deleteGoods(goods_id);
	}
	

	
}
