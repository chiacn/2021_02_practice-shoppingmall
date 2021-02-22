package com.bookshop01.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.common.base.BaseController;
import com.bookshop01.goods.service.GoodsService;
import com.bookshop01.goods.vo.GoodsVO;

import net.sf.json.JSONObject;

//추가
import java.util.Collections;



@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsControllerImpl extends BaseController   implements GoodsController {
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value="/goodsDetail.do" ,method = RequestMethod.GET)
	public ModelAndView goodsDetail(@RequestParam("goods_id") String goods_id,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		HttpSession session=request.getSession();
		Map goodsMap=goodsService.goodsDetail(goods_id);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsMap", goodsMap);
		GoodsVO goodsVO=(GoodsVO)goodsMap.get("goodsVO");
		addGoodsInQuick(goods_id,goodsVO,session);
		return mav;
	}
	
	@RequestMapping(value="/keywordSearch.do",method = RequestMethod.GET,produces = "application/text; charset=utf8")
	public @ResponseBody String  keywordSearch(@RequestParam("keyword") String keyword,
			                                  HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//System.out.println(keyword);
		if(keyword == null || keyword.equals(""))
		   return null ;
	
		keyword = keyword.toUpperCase();
	    List<String> keywordList =goodsService.keywordSearch(keyword);
	    
	 // 최종 완성될 JSONObject 선언(전체)
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("keyword", keywordList);
		 		
	    String jsonInfo = jsonObject.toString();
	   // System.out.println(jsonInfo);
	    return jsonInfo ;
	}
	
	@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		List<GoodsVO> goodsList=goodsService.searchGoods(searchWord);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsList", goodsList);
		return mav;
		
	}
	
	private void addGoodsInQuick(String goods_id,GoodsVO goodsVO,HttpSession session){
		boolean already_existed=false;
		List<GoodsVO> quickGoodsList; //최근 본 상품 저장 ArrayList
		quickGoodsList=(ArrayList<GoodsVO>)session.getAttribute("quickGoodsList");
				
		if(quickGoodsList!=null){
				if(quickGoodsList.size() < 4){ //미리본 상품 리스트에 상품개수가 세개 이하인 경우 
					for(int i=0; i<quickGoodsList.size();i++){
						GoodsVO _goodsBean=(GoodsVO)quickGoodsList.get(i);
						
						/*형변환 추가 - addGoodsInQuick메서드에 인자로 들어오는 goods_id는 String값인데,
						            quickGoodsList의 goodsVO에서 getter로 얻는 goods_id 값은 int형이므로
						            equals 비교를 위해 String.valueOf로 감싸 형변환 해주었다.
						            
						     _goodsBean.getGoods_id()   ->   String.valueOf(_goodsBean.getGoods_id())
						*/
						if(goods_id.equals(String.valueOf(_goodsBean.getGoods_id()))){
							already_existed=true;
							break;
						}
					}
					if(already_existed==false){ //중복 x 
						
						
						
						//추가 앞뒤로 reverse 해준거 - 
						if(quickGoodsList.size() > 1) {
						      Collections.reverse(quickGoodsList);
						}
						
						quickGoodsList.add(goodsVO);
						//추가
						if(quickGoodsList.size() > 1) {
							 Collections.reverse(quickGoodsList);
						}
						
					}
			// 추가한 부분 qucikGoodsList==4일 때, List에 가장 최근 본거 추가, 가장 마지막에 본거 제거-----------------
				}else {	// else if 안 써도 됨 (?) quickGoodsList == 4일 때 					
				     for(int i=0; i<quickGoodsList.size();i++) {
				    	 GoodsVO _goodsBean= (GoodsVO) quickGoodsList.get(i);
				    	 
				    	 //goods_id 는 String 값이고 getGoods_id()로 얻어지는 값은 int 값임
				    	
				    	 if(goods_id.equals(String.valueOf(_goodsBean.getGoods_id()))) {//id값이 숫자이므로 ==로 해야하나?
				    		 already_existed =true;
				    		 break;
				    	 }
				     }
				     if(already_existed==false) {
				    	 //추가
				    	 Collections.reverse(quickGoodsList);
	
				    	 quickGoodsList.remove(0);
				    	 quickGoodsList.add(goodsVO);
				    	 
				    	 //추가
				    	 Collections.reverse(quickGoodsList);
				     }
				}
				// -----------------------------------------------------------			
					
		}else{
			quickGoodsList =new ArrayList<GoodsVO>();
			quickGoodsList.add(goodsVO);
			
		}
		session.setAttribute("quickGoodsList",quickGoodsList);
		session.setAttribute("quickGoodsListNum", quickGoodsList.size());
	}
}
