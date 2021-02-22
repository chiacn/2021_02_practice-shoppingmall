package com.bookshop01.admin.order.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bookshop01.admin.goods.dao.AdminGoodsDAO;
import com.bookshop01.admin.order.dao.AdminOrderDAO;
import com.bookshop01.goods.vo.GoodsVO;
import com.bookshop01.goods.vo.ImageFileVO;
import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.order.vo.OrderVO;


@Service("adminOrderService")
@Transactional(propagation=Propagation.REQUIRED)
public class AdminOrderServiceImpl implements AdminOrderService {
	@Autowired
	private AdminOrderDAO adminOrderDAO;
	
	/* ����
	public List<OrderVO>listNewOrder(Map condMap) throws Exception{
		return adminOrderDAO.selectNewOrderList(condMap);
	} */
	
	/* ����2 
	public Map listNewOrder(Map condMap) throws Exception{
		List<OrderVO> newOrderList = adminOrderDAO.selectNewOrderList(condMap);
		
		//totArticles �߰� 
		int totArticles = adminOrderDAO.selectTotArticles(condMap);
		
		Map pagingMap = new HashMap();
				
		pagingMap.put("newOrderList", newOrderList);
		pagingMap.put("totArticles", totArticles);
				
		return pagingMap;
	}
	*/
	//����ȸ (��¥��ȸ + �˻�) 
	public Map listNewOrder(Map condMap) throws Exception{

		    
	  /* (�߰�) ������ ��ȣ�� �˻��Ѵٸ�?   (������ ��ȣ�� hp1= 000 hp2 0000 hp3 0000 <- �̷������� ������ �и��Ǿ� �ִ�. )
		String search_word = (String) condMap.get("search_word");
		if(search_word.contains("010")) {
			  if(search_word.contains("-")) {
				    String hp[] = search_word.split("-");
				    String hp1 = hp[0];
				    String hp2 = hp[1];
				    String hp3 = hp[2];
				    //condMap.remove("search_word");
				    condMap.put("hp1", hp1);
				    condMap.put("hp2", hp2);
				    condMap.put("hp3", hp3);
				    
			  }else if(search_word.length() >10) { // �ڵ��� ��ȣ '-' ���� �� �ϰ� �׳� ��ȣ�� �� �� 
				  String hp1 = search_word.substring(0,3);
				  String hp2 = search_word.substring(3,6);
				  String hp3 = search_word.substring(6);
				    condMap.put("hp1", hp1);
				    condMap.put("hp2", hp2);
				    condMap.put("hp3", hp3);				  
				    
				    System.out.println(hp1 +hp2+hp3);
			  }
		}
		*/
			String search_word = (String) condMap.get("search_word");
			if(search_word !=null) {
			System.out.println("search_word Ÿ�� : "+search_word.getClass().getName());
			}
			
			if(search_word!= null) {
				if(search_word.contains("010")==true && search_word.contains("-")==false) {//'-'�� �������� �ʰ� �˻��ߴٸ�
					  if(search_word.length() >10){
						  String hp1 = search_word.substring(0,3);
						  String hp2 = search_word.substring(3,7);
						  String hp3 = search_word.substring(7);
	                      
						  String hpNum = hp1 + "-" + hp2 +"-" +hp3;
						  //��ȣ �˻� �� �Ǵ� ���� Ÿ�Կ����ΰ� �ؼ� Integer�� �ٲ���ô�.
						  
						  
						  condMap.remove("search_word");
						  condMap.put("search_word", hpNum);

					  }
				}
			}
	    
		
		
			
		List<OrderVO> newOrderList = adminOrderDAO.selectNewOrderList(condMap);
				
		//totArticles �߰� 
		int totArticles = adminOrderDAO.selectTotArticles(condMap);
		
		Map pagingMap = new HashMap();
				
		pagingMap.put("newOrderList", newOrderList);
		pagingMap.put("totArticles", totArticles);
				
		return pagingMap;
	}
	
	@Override
	public void  modifyDeliveryState(Map deliveryMap) throws Exception{
		adminOrderDAO.updateDeliveryState(deliveryMap);
	}
	@Override
	public Map orderDetail(int order_id) throws Exception{
		Map orderMap=new HashMap();
		ArrayList<OrderVO> orderList =adminOrderDAO.selectOrderDetail(order_id);
		OrderVO deliveryInfo=(OrderVO)orderList.get(0);
		String member_id=(String)deliveryInfo.getMember_id();
		MemberVO orderer=adminOrderDAO.selectOrderer(member_id);
		orderMap.put("orderList",orderList);
		orderMap.put("deliveryInfo",deliveryInfo);
		orderMap.put("orderer", orderer);
		return orderMap;
	}
	
	
	//�ֹ� ���� ��� �߰�
	@Override
    public void removeOrder(int order_id) throws Exception {
    	adminOrderDAO.removeOrder(order_id);
    }


}
