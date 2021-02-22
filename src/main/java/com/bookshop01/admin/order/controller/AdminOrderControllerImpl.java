package com.bookshop01.admin.order.controller;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.admin.goods.service.AdminGoodsService;
import com.bookshop01.admin.order.service.AdminOrderService;
import com.bookshop01.common.base.BaseController;
import com.bookshop01.goods.vo.GoodsVO;
import com.bookshop01.goods.vo.ImageFileVO;
import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.mypage.controller.MyPageController;
import com.bookshop01.mypage.service.MyPageService;
import com.bookshop01.order.vo.OrderVO;

@Controller("adminOrderController")
@RequestMapping(value="/admin/order")
public class AdminOrderControllerImpl extends BaseController  implements AdminOrderController{
	@Autowired
	private AdminOrderService adminOrderService;

	/* 수정 전
	@Override
	@RequestMapping(value="/adminOrderMain.do" ,method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView adminOrderMain(@RequestParam Map<String, String> dateMap,
			                          HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
	
		
		String fixedSearchPeriod = dateMap.get("fixedSearchPeriod");  //adminOrderMain.jsp의  search_order_history함수로부터 받음.
		String section = dateMap.get("section");
		String pageNum = dateMap.get("pageNum");
		String beginDate=null,endDate=null;
		
		
		String [] tempDate=calcSearchPeriod(fixedSearchPeriod).split(",");
		beginDate=tempDate[0];
		endDate=tempDate[1];
		
			//이거 없어도 되지 않나..? 오타인듯  없애도 실행됨. 
			//dateMap.put("beginDate", beginDate);
			//dateMap.put("endDate", endDate);
		
		
		
		HashMap<String,Object> condMap=new HashMap<String,Object>();
		if(section== null) {
			section = "1";
		}
		condMap.put("section",section);
		if(pageNum== null) {
			pageNum = "1";
		}
		condMap.put("pageNum",pageNum);
		condMap.put("beginDate",beginDate);
		condMap.put("endDate", endDate);
		//List<OrderVO> newOrderList=adminOrderService.listNewOrder(condMap);
		//totArticles 가져오는 기능도 추가한 버전
		Map pagingMap = adminOrderService.listNewOrder(condMap);
		List<OrderVO> newOrderList = (List) pagingMap.get("newOrderList");
		int totArticles = (Integer) pagingMap.get("totArticles");		
		
		mav.addObject("newOrderList",newOrderList);
		
		String beginDate1[]=beginDate.split("-");
		String endDate2[]=endDate.split("-");
		mav.addObject("beginYear",beginDate1[0]);
		mav.addObject("beginMonth",beginDate1[1]);
		mav.addObject("beginDay",beginDate1[2]);
		mav.addObject("endYear",endDate2[0]);
		mav.addObject("endMonth",endDate2[1]);
		mav.addObject("endDay",endDate2[2]);
		
		mav.addObject("section", section);
		mav.addObject("pageNum", pageNum);
		//totArticles 부분 추가
		mav.addObject("totArticles", totArticles);
		return mav;
		
	}
	*/
	
	
	//주문관리 (adminOrderMain.jsp)에서 날짜설정 + 검색 기능 추가한 adminOrderMain 메서드 
	@Override
	@RequestMapping(value="/adminOrderMain.do" ,method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView adminOrderMain(@RequestParam Map<String, String> dateMap,
			                          HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
	
		
		String fixedSearchPeriod = dateMap.get("fixedSearchPeriod");  //adminOrderMain.jsp의  search_order_history함수로부터 받음.
		String section = dateMap.get("section");
		String pageNum = dateMap.get("pageNum");
		String search_type = dateMap.get("search_type");
		String search_word = dateMap.get("search_word"); //검색어
		
		String beginDate = dateMap.get("beginDate");
		String endDate = dateMap.get("endDate");
				
		
		// 이건 beginDate나 endDate가 null일 때만 하는걸로
		if(beginDate ==null) {
			String [] tempDate=calcSearchPeriod(fixedSearchPeriod).split(",");
			beginDate=tempDate[0];
			endDate=tempDate[1];
		}
		
		HashMap<String,Object> condMap=new HashMap<String,Object>();
		if(section== null) {
			section = "1";
		}
		condMap.put("section",section);
		if(pageNum== null) {
			pageNum = "1";
		}
		condMap.put("pageNum",pageNum);
		condMap.put("beginDate",beginDate);
		condMap.put("endDate", endDate);
		
		//추가 (검색어 등)
		condMap.put("search_type", search_type);
		condMap.put("search_word", search_word);
		
		
		Map pagingMap = adminOrderService.listNewOrder(condMap);
		List<OrderVO> newOrderList = (List) pagingMap.get("newOrderList");
		int totArticles = (Integer) pagingMap.get("totArticles");		
		
		mav.addObject("newOrderList",newOrderList);
		
		String beginDate1[]=beginDate.split("-");
		String endDate2[]=endDate.split("-");
		
		// jsp에서 상세검색 년도를 보정하기 위한 추가 
		fixedSearchPeriod= "today";
		String [] tempDate=calcSearchPeriod(fixedSearchPeriod).split(",");
		beginDate=tempDate[0];
		endDate=tempDate[1];
		String beginDate3[] = beginDate.split("-");
		String endDate3[] = endDate.split("-");
		mav.addObject("currentBeginYear", beginDate3[0]);
		mav.addObject("currentEndYear", endDate3[0]);
		// -----------------------------------------
		
		
		mav.addObject("beginYear",beginDate1[0]);
		mav.addObject("beginMonth",beginDate1[1]);
		mav.addObject("beginDay",beginDate1[2]);
		mav.addObject("endYear",endDate2[0]);
		mav.addObject("endMonth",endDate2[1]);
		mav.addObject("endDay",endDate2[2]);
		
		mav.addObject("section", section);
		mav.addObject("pageNum", pageNum);
		//totArticles 부분 추가
		mav.addObject("totArticles", totArticles);
		return mav;
		
	}
	
	
	
	//배송상태 변경하고 배송수정 클릭했을 때
	@Override
	@RequestMapping(value="/modifyDeliveryState.do" ,method={RequestMethod.POST})
	public ResponseEntity modifyDeliveryState(@RequestParam Map<String, String> deliveryMap, 
			                        HttpServletRequest request, HttpServletResponse response)  throws Exception {
		adminOrderService.modifyDeliveryState(deliveryMap); //Ajax를 통해 Map 형태로 반환받은 (deliveryMap) 데이터를 인자로
		
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		message  = "mod_success";
		resEntity =new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
		
	}
	
	// 주문관리 페이지(adminOrderMain.jsp)에서 주문번호(order_id)를 클릭했을 때 주문 상세정보창(orderDetail.jsp)으로 요청되는 메서드 
	@Override
	@RequestMapping(value="/orderDetail.do" ,method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView orderDetail(@RequestParam("order_id") int order_id, 
			                      HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		Map orderMap =adminOrderService.orderDetail(order_id);
		mav.addObject("orderMap", orderMap);
		return mav;
	}
	
	// 주문관리 삭제기능 추가
	@Override
	@RequestMapping(value="/removeOrder.do", method= RequestMethod.POST)
    public ResponseEntity removeOrder(@RequestParam("order_id") int order_id,
    		                    HttpServletRequest request, HttpServletResponse response) throws Exception {
		 response.setContentType("text/html; charset=UTF-8");
		 String message;
		 ResponseEntity resEnt = null;
		 HttpHeaders responseHeaders = new HttpHeaders();
		 responseHeaders.add("Content-Type", "text/html;charset=utf-8");
		 try {
			 adminOrderService.removeOrder(order_id);
			 
			 message="<script>";
			 message +=" alert('삭제 완료');";
			 message +=" location.href='" + request.getContextPath() + "/admin/order/adminOrderMain.do';";
			 message += " </script>";
			 resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		 }catch(Exception e) {
             message ="<script>";
             message += " alert('작업중 오류가 발생했습니다. 다시 시도해 주세요.');";
             message += " location.href='" + request.getContextPath() + "/admin/order/adminOrderMain.do';";
             message += " </script>";
             
             resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
             e.printStackTrace();			 
		 }
		 return resEnt;
	}

}
