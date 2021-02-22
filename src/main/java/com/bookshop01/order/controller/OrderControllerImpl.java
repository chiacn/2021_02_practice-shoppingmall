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
	
	// goodsDetail.jsp에서 '구매하기' 클릭시 
	@RequestMapping(value="/orderEachGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderEachGoods(@ModelAttribute("orderVO") OrderVO _orderVO,
			                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		request.setCharacterEncoding("utf-8");
		HttpSession session=request.getSession();
		session=request.getSession();
		
		Boolean isLogOn=(Boolean)session.getAttribute("isLogOn");
		String action=(String)session.getAttribute("action");
		//로그인 여부 체크
		//이전에 로그인 상태인 경우는 주문과정 진행
		//로그아웃 상태인 경우 로그인 화면으로 이동
		if(isLogOn==null || isLogOn==false){
			session.setAttribute("orderInfo", _orderVO);
			session.setAttribute("action", "/order/orderEachGoods.do"); //로그인 안 돼 있으면 action값 설정(로그인 후 해당 주소로 가게)
			return new ModelAndView("redirect:/member/loginForm.do");
		}else{
			 if(action!=null && action.equals("/order/orderEachGoods.do")){  //action 값으로 미리 로그인한지 구분 
				orderVO=(OrderVO)session.getAttribute("orderInfo");
				session.removeAttribute("action"); //action 값 제거해줌 
			 }else {
				 orderVO=_orderVO;  //미리 로그인을 했다면 바로 주문을 처리 
			 }
		 }
		
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		List myOrderList=new ArrayList<OrderVO>(); //1. 주문 정보를 저장할 ArrayList 생성
		myOrderList.add(orderVO); //2. 브라우저에서 전달한 주문정보를 ArrayList에 저장

		MemberVO memberInfo=(MemberVO)session.getAttribute("memberInfo");//3. 주문자 정보 가져오기
		
		session.setAttribute("myOrderList", myOrderList); //4. 주문 정보 + 주문자 정보를 세션에 바인딩
		session.setAttribute("orderer", memberInfo);
		return mav;
	}
	
	//장바구니에서 상품 주문 
	@RequestMapping(value="/orderAllCartGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderAllCartGoods( @RequestParam("cart_goods_qty")  String[] cart_goods_qty, //선택한 상품 수량을 배열로 받는다. 
			                 HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session=request.getSession();
		
		Map cartMap=(Map)session.getAttribute("cartMap"); //(myCartList.do에서) 미리 세션에 저장한 장바구니 상품 목록을 가져온다.
		List myOrderList=new ArrayList<OrderVO>();		
		List<GoodsVO> myGoodsList=(List<GoodsVO>)cartMap.get("myGoodsList"); // 
		MemberVO memberVO=(MemberVO)session.getAttribute("memberInfo");
		
		for(int i=0; i<cart_goods_qty.length;i++){ //체크된 각 [상품수량]
			String[] cart_goods=cart_goods_qty[i].split(":"); 
			//문자열로 결합되어 전송된 상품번호(order_goods_id)와 상품수량(order_goods_qty)를 split()메서드를 이용해 분리
			//ex. cart_goods_qty[0] = 335:1 (id:수량)이면 cart_goods[0]=335,cart_goods[1]=1 	
			
			/*   설명
			    cart_goods_qty[0]일 때 (order_goods_id : order_goods_qty를 split()한 것이므로, 
			    cart_goods[0] = order_goods_id, cart_goods[1] = order_goods_qty 이렇게 두 개의 배열이 생김
			    
			        즉, cart_goods_qty[1] 일 때도 cart_goods[0], cart_goods[1]
			       cart_goods_qty[2] 일 때, cart_goods[0], cart_goods[1] 이런식으로 
			       
			       이걸 이중 for문으로 cart_goods_qty[i]당 goods_goods[0],[1] 이렇게 나눠 표헌한 것 
			 */
			
			
			for(int j = 0; j< myGoodsList.size();j++) { //(myGoodsList의 길이만큼) 
				
    //session -> cartMap ->myGoodsList 1. 기존 장바구니에서 가져온 myGoodsList의 j번째 goodsVO와
				GoodsVO goodsVO = myGoodsList.get(j); 
				int goods_id = goodsVO.getGoods_id();
				
  //[0]이므로  id값과의 비교  | 2. cart_goods[0] (체크한 상품의 수량(cart_goods_qty)에서 goods_id 부분(cart_goods[0]) 비교			
				if(goods_id==Integer.parseInt(cart_goods[0])) {
					
					//3. 일치하면 ( myGoodsList의 goods_id == cart_goods[0], 즉 체크한 상품이면) orderVO에 담는다.
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
		return mav; //orderGoodsForm.jsp로  (주의 이런거 name-주소 매칭 tiles에서 정해짐) 
	}	
	
	//orderGoodsForm.jsp에서 최종구매하기 클릭 시
	@RequestMapping(value="/payToOrderGoods.do" ,method = RequestMethod.POST)
	public ModelAndView payToOrderGoods(@RequestParam Map<String, String> receiverMap, //주문창에서 입력한 상품 수령자 정보와 배송지정보를 Map에 바로 저장한다.
			                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		HttpSession session=request.getSession();
		MemberVO memberVO=(MemberVO)session.getAttribute("orderer"); //주문자 정보
		String member_id=memberVO.getMember_id();
		String orderer_name=memberVO.getMember_name();
		String orderer_hp = memberVO.getHp1()+"-"+memberVO.getHp2()+"-"+memberVO.getHp3();
		List<OrderVO> myOrderList=(List<OrderVO>)session.getAttribute("myOrderList"); //주문 정보 
		
		for(int i=0; i<myOrderList.size();i++){ // 주문정보(에 jsp에서 가져온 주문정보)receiver 넣기
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
			myOrderList.set(i, orderVO); //각 orderVO에 주문자 정보를 세팅한 후 다시 myOrderList에 저장한다.
		}//end for
		
	    orderService.addNewOrder(myOrderList); //주문 정보를 SQL로 전달 
		mav.addObject("myOrderInfo",receiverMap);//OrderVO로 주문결과 페이지에  주문자 정보를 표시한다.
		mav.addObject("myOrderList", myOrderList); // 이건 주문상품정보 표시 
		return mav;
	}
	

}
