package com.bookshop01.cart.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bookshop01.cart.dao.CartDAO;
import com.bookshop01.cart.vo.CartVO;
import com.bookshop01.goods.vo.GoodsVO;

@Service("cartService")
@Transactional(propagation=Propagation.REQUIRED)
public class CartServiceImpl  implements CartService{
	@Autowired
	private CartDAO cartDAO;
	
    public Map<String, List> myCartList(CartVO cartVO) throws Exception {
    	Map<String, List> cartMap = new HashMap<String, List>();
    	List<CartVO> myCartList = cartDAO.selectCartList(cartVO);//vo로 받아온 id로 t_shopping_cart테이블의 컬럼을 불러온다.
    	if(myCartList.size()==0) {//카트에 저장된 상품이 없는경우
    		 return null;
    	}
    	
    	//위 코드에서 list에 담은 t_shopping_cart 테이블의 컬럼들을 인자로 보낸다.
    	List<GoodsVO> myGoodsList = cartDAO.selectGoodsList(myCartList); 
    	cartMap.put("myCartList", myCartList);
    	cartMap.put("myGoodsList", myGoodsList);
    	return cartMap;    	
    }
	
	public boolean findCartGoods(CartVO cartVO) throws Exception{
		 return cartDAO.selectCountInCart(cartVO);
		
	}	
	public void addGoodsInCart(CartVO cartVO) throws Exception{
		cartDAO.insertGoodsInCart(cartVO);
	}
	
	public boolean modifyCartQty(CartVO cartVO) throws Exception{
		boolean result=true;
		cartDAO.updateCartGoodsQty(cartVO);
		return result;
	}
	public void removeCartGoods(int cart_id) throws Exception{
		cartDAO.deleteCartGoods(cart_id);
	}
	
}
