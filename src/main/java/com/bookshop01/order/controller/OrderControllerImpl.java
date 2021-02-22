package com.bookshop01.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.common.base.BaseController;
import com.bookshop01.goods.vo.GoodsVO;
import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.order.service.OrderService;
import com.bookshop01.order.vo.OrderVO;

@Controller("orderController")
@RequestMapping(value="/order")
public class OrderControllerImpl extends BaseController implements OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderVO orderVO;
	
	// goodsDetail.jsp���� '�����ϱ�' Ŭ���� 
	@RequestMapping(value="/orderEachGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderEachGoods(@ModelAttribute("orderVO") OrderVO _orderVO,
			                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		request.setCharacterEncoding("utf-8");
		HttpSession session=request.getSession();
		session=request.getSession();
		
		Boolean isLogOn=(Boolean)session.getAttribute("isLogOn");
		String action=(String)session.getAttribute("action");
		//�α��� ���� üũ
		//������ �α��� ������ ���� �ֹ����� ����
		//�α׾ƿ� ������ ��� �α��� ȭ������ �̵�
		if(isLogOn==null || isLogOn==false){
			session.setAttribute("orderInfo", _orderVO);
			session.setAttribute("action", "/order/orderEachGoods.do"); //�α��� �� �� ������ action�� ����(�α��� �� �ش� �ּҷ� ����)
			return new ModelAndView("redirect:/member/loginForm.do");
		}else{
			 if(action!=null && action.equals("/order/orderEachGoods.do")){  //action ������ �̸� �α������� ���� 
				orderVO=(OrderVO)session.getAttribute("orderInfo");
				session.removeAttribute("action"); //action �� �������� 
			 }else {
				 orderVO=_orderVO;  //�̸� �α����� �ߴٸ� �ٷ� �ֹ��� ó�� 
			 }
		 }
		
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		List myOrderList=new ArrayList<OrderVO>(); //1. �ֹ� ������ ������ ArrayList ����
		myOrderList.add(orderVO); //2. ���������� ������ �ֹ������� ArrayList�� ����

		MemberVO memberInfo=(MemberVO)session.getAttribute("memberInfo");//3. �ֹ��� ���� ��������
		
		session.setAttribute("myOrderList", myOrderList); //4. �ֹ� ���� + �ֹ��� ������ ���ǿ� ���ε�
		session.setAttribute("orderer", memberInfo);
		return mav;
	}
	
	//��ٱ��Ͽ��� ��ǰ �ֹ� 
	@RequestMapping(value="/orderAllCartGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderAllCartGoods( @RequestParam("cart_goods_qty")  String[] cart_goods_qty, //������ ��ǰ ������ �迭�� �޴´�. 
			                 HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session=request.getSession();
		
		Map cartMap=(Map)session.getAttribute("cartMap"); //(myCartList.do����) �̸� ���ǿ� ������ ��ٱ��� ��ǰ ����� �����´�.
		List myOrderList=new ArrayList<OrderVO>();		
		List<GoodsVO> myGoodsList=(List<GoodsVO>)cartMap.get("myGoodsList"); // 
		MemberVO memberVO=(MemberVO)session.getAttribute("memberInfo");
		
		for(int i=0; i<cart_goods_qty.length;i++){ //üũ�� �� [��ǰ����]
			String[] cart_goods=cart_goods_qty[i].split(":"); 
			//���ڿ��� ���յǾ� ���۵� ��ǰ��ȣ(order_goods_id)�� ��ǰ����(order_goods_qty)�� split()�޼��带 �̿��� �и�
			//ex. cart_goods_qty[0] = 335:1 (id:����)�̸� cart_goods[0]=335,cart_goods[1]=1 	
			
			/*   ����
			    cart_goods_qty[0]�� �� (order_goods_id : order_goods_qty�� split()�� ���̹Ƿ�, 
			    cart_goods[0] = order_goods_id, cart_goods[1] = order_goods_qty �̷��� �� ���� �迭�� ����
			    
			        ��, cart_goods_qty[1] �� ���� cart_goods[0], cart_goods[1]
			       cart_goods_qty[2] �� ��, cart_goods[0], cart_goods[1] �̷������� 
			       
			       �̰� ���� for������ cart_goods_qty[i]�� goods_goods[0],[1] �̷��� ���� ǥ���� �� 
			 */
			
			
			for(int j = 0; j< myGoodsList.size();j++) { //(myGoodsList�� ���̸�ŭ) 
				
    //session -> cartMap ->myGoodsList 1. ���� ��ٱ��Ͽ��� ������ myGoodsList�� j��° goodsVO��
				GoodsVO goodsVO = myGoodsList.get(j); 
				int goods_id = goodsVO.getGoods_id();
				
  //[0]�̹Ƿ�  id������ ��  | 2. cart_goods[0] (üũ�� ��ǰ�� ����(cart_goods_qty)���� goods_id �κ�(cart_goods[0]) ��			
				if(goods_id==Integer.parseInt(cart_goods[0])) {
					
					//3. ��ġ�ϸ� ( myGoodsList�� goods_id == cart_goods[0], �� üũ�� ��ǰ�̸�) orderVO�� ��´�.
					OrderVO _orderVO=new OrderVO();         
					String goods_title=goodsVO.getGoods_title();
					int goods_sales_price=goodsVO.getGoods_sales_price();
					String goods_fileName=goodsVO.getGoods_fileName();
					_orderVO.setGoods_id(goods_id);
					_orderVO.setGoods_title(goods_title);
					_orderVO.setGoods_sales_price(goods_sales_price);
					_orderVO.setGoods_fileName(goods_fileName);
					_orderVO.setOrder_goods_qty(Integer.parseInt(cart_goods[1]));
					myOrderList.add(_orderVO);
					break;
				}
			}
		}
		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberVO);
		return mav; //orderGoodsForm.jsp��  (���� �̷��� name-�ּ� ��Ī tiles���� ������) 
	}	
	
	//orderGoodsForm.jsp���� ���������ϱ� Ŭ�� ��
	@RequestMapping(value="/payToOrderGoods.do" ,method = RequestMethod.POST)
	public ModelAndView payToOrderGoods(@RequestParam Map<String, String> receiverMap, //�ֹ�â���� �Է��� ��ǰ ������ ������ ����������� Map�� �ٷ� �����Ѵ�.
			                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		HttpSession session=request.getSession();
		MemberVO memberVO=(MemberVO)session.getAttribute("orderer"); //�ֹ��� ����
		String member_id=memberVO.getMember_id();
		String orderer_name=memberVO.getMember_name();
		String orderer_hp = memberVO.getHp1()+"-"+memberVO.getHp2()+"-"+memberVO.getHp3();
		List<OrderVO> myOrderList=(List<OrderVO>)session.getAttribute("myOrderList"); //�ֹ� ���� 
		
		for(int i=0; i<myOrderList.size();i++){ // �ֹ�����(�� jsp���� ������ �ֹ�����)receiver �ֱ�
			OrderVO orderVO=(OrderVO)myOrderList.get(i);
			orderVO.setMember_id(member_id);
			orderVO.setOrderer_name(orderer_name);
			orderVO.setReceiver_name(receiverMap.get("receiver_name"));
			
			orderVO.setReceiver_hp1(receiverMap.get("receiver_hp1"));
			orderVO.setReceiver_hp2(receiverMap.get("receiver_hp2"));
			orderVO.setReceiver_hp3(receiverMap.get("receiver_hp3"));
			orderVO.setReceiver_tel1(receiverMap.get("receiver_tel1"));
			orderVO.setReceiver_tel2(receiverMap.get("receiver_tel2"));
			orderVO.setReceiver_tel3(receiverMap.get("receiver_tel3"));
			
			orderVO.setDelivery_address(receiverMap.get("delivery_address"));
			orderVO.setDelivery_message(receiverMap.get("delivery_message"));
			orderVO.setDelivery_method(receiverMap.get("delivery_method"));
			orderVO.setGift_wrapping(receiverMap.get("gift_wrapping"));
			orderVO.setPay_method(receiverMap.get("pay_method"));
			orderVO.setCard_com_name(receiverMap.get("card_com_name"));
			orderVO.setCard_pay_month(receiverMap.get("card_pay_month"));
			orderVO.setPay_orderer_hp_num(receiverMap.get("pay_orderer_hp_num"));	
			orderVO.setOrderer_hp(orderer_hp);	
			myOrderList.set(i, orderVO); //�� orderVO�� �ֹ��� ������ ������ �� �ٽ� myOrderList�� �����Ѵ�.
		}//end for
		
	    orderService.addNewOrder(myOrderList); //�ֹ� ������ SQL�� ���� 
		mav.addObject("myOrderInfo",receiverMap);//OrderVO�� �ֹ���� ��������  �ֹ��� ������ ǥ���Ѵ�.
		mav.addObject("myOrderList", myOrderList); // �̰� �ֹ���ǰ���� ǥ�� 
		return mav;
	}
	

}
