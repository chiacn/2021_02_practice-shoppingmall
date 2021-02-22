package com.bookshop01.admin.order.dao;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.order.vo.OrderVO;

public interface AdminOrderDAO {
	public ArrayList<OrderVO> selectNewOrderList(Map condMap) throws DataAccessException;
	public void  updateDeliveryState(Map deliveryMap) throws DataAccessException;
	public ArrayList<OrderVO> selectOrderDetail(int order_id) throws DataAccessException;
	public MemberVO selectOrderer(String member_id) throws DataAccessException;
		
	//추가 +  condMap 인자로 받도록 수정 
    public int selectTotArticles(Map condMap) throws DataAccessException;

  
    //추가 주문 삭제 기능
    public void removeOrder(int order_id) throws DataAccessException;
}
