<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<c:set var="myCartList"  value="${cartMap.myCartList}"  />
<c:set var="myGoodsList"  value="${cartMap.myGoodsList}"  />

<c:set  var="totalGoodsNum" value="0" />  <!--주문 개수 -->
<c:set  var="totalDeliveryPrice" value="0" /> <!-- 총 배송비 --> 
<c:set  var="totalDiscountedPrice" value="0" /> <!-- 총 할인금액 -->
<head>
<script type="text/javascript">
function calcGoodsPrice(bookPrice,obj,index){ //index 추가로 받아옴 (수정)
	
//수정한거 (${cnt.index}붙인거) - select_goods_qty  <- 그러니까 이 부분만 index붙여주면 됨 (인자로 가져온거 ) 
	var totalPrice,final_total_price,totalNum,goodsNum;
	
	//추가
	var p_totalGoodsNum = document.getElementById("p_totalGoodsNum");
	var h_totalGoodsNum = document.getElementById("h_totalGoodsNum");
	
	var goods_qty=document.getElementById("select_goods_qty"+index); //해당 상품의 개수(t_shopping_cart에서 받아오는 데이터를 가져오면 될듯)
	//alert("총 상품금액"+goods_qty.value);
	var p_totalNum=document.getElementById("p_totalNum"); //총 상품수
	var p_totalPrice=document.getElementById("p_totalGoodsPrice"); //총 상품금액 (이건 원래 있는데다가) totalPrice ->totalGoodsPrice로 수정
	var p_final_totalPrice=document.getElementById("p_final_totalPrice"); //최종 결제 금액(이건 원래 있는데다가)
	var h_totalNum=document.getElementById("h_totalNum");
	var h_totalPrice=document.getElementById("h_totalGoodsPrice");
	var h_totalDelivery=document.getElementById("h_totalDeliveryPrice");  //총 배송비 totalDelevery -> totalDeleveryPrice로 바꾸기 p
	var h_final_total_price=document.getElementById("h_final_totalPrice");  
	
	//아예 새로운 걸로 만들어줘야하나 (체크되어 있는 상태니까?)
	if(obj.checked==true){
	//	alert("체크 했음")
		//Number 문자열을 숫자로 변환하는 함수 	
		totalNum=Number(h_totalNum.value)+Number(goods_qty.value); //value 값인걸로 보아 id="select_goods_qty"는 input 값
		//alert("totalNum:"+totalNum);
		totalPrice=Number(h_totalGoodsPrice.value)+Number(goods_qty.value*bookPrice);
		//alert("totalPrice:"+totalPrice);
		final_total_price=totalPrice+Number(h_totalDelivery.value);
		//alert("final_total_price:"+final_total_price);		
		
		//추가 
		goodsNum = Number(h_totalGoodsNum.value) +1;
		
		
	}else{
	//	alert("h_totalNum.value:"+h_totalNum.value);
		totalNum=Number(h_totalNum.value)-Number(goods_qty.value);
	//	alert("totalNum:"+ totalNum);
		totalPrice=Number(h_totalGoodsPrice.value)-Number(goods_qty.value)*bookPrice;
	//	alert("totalPrice="+totalPrice);
		final_total_price=totalPrice-Number(h_totalDelivery.value);
	//	alert("final_total_price:"+final_total_price);
	
	    //추가
	    goodsNum = Number(h_totalGoodsNum.value) -1;
 
	}
	
	h_totalNum.value=totalNum;  //(input hidden 태그 업데이트)
	
	h_totalGoodsPrice.value=totalPrice;
	h_final_total_price.value=final_total_price;
	
	p_totalNum.innerHTML=totalNum;    // (표시되는 부분) <p>태그 안에 있어서 p_ 인가 
	p_totalGoodsPrice.innerHTML=totalPrice;
	p_final_totalPrice.innerHTML=final_total_price;
	
    //추가
	h_totalGoodsNum.value = goodsNum;
	p_totalGoodsNum.innerHTML = goodsNum+"개";
	
}

function modify_cart_qty(goods_id,bookPrice,index){ //수정 버튼을 누르는 그 상품의 index 값(forEach 상 )
	//alert(index);
   var length=document.frm_order_all_cart.cart_goods_qty.length; //myCartList 길이
   var _cart_goods_qty=0;
	if(length>1){ //카트에 제품이 한개인 경우와 여러개인 경우 나누어서 처리한다.
		
		// cart_goods_qty input 값 value 가져옴 (숫자 고치고 변경버튼 누른거 ) / foreach로 반복되는 input id 값
		_cart_goods_qty=document.frm_order_all_cart.cart_goods_qty[index].value; //해당되는 상품(수량을 변경한)의 forEach상 index 로 해당 상품의 기존 개수 파악
	}else{
		_cart_goods_qty=document.frm_order_all_cart.cart_goods_qty.value;
	}
		
	var cart_goods_qty=Number(_cart_goods_qty);
	//alert("cart_goods_qty:"+cart_goods_qty);
	//console.log(cart_goods_qty); 
	$.ajax({
		type : "post",
		async : false, //false인 경우 동기식으로 처리한다.
		url : "${contextPath}/cart/modifyCartQty.do",
		data : {
			goods_id:goods_id,
			cart_goods_qty:cart_goods_qty
		},
		
		success : function(data, textStatus) {
			//alert(data);
			if(data.trim()=='modify_success'){
				alert("수량을 변경했습니다!!");	
			}else{
				alert("다시 시도해 주세요!!");	
			}
			
		},
		error : function(data, textStatus) {
			alert("에러가 발생했습니다."+data);
		},
		complete : function(data, textStatus) {
			//alert("작업을완료 했습니다");
			
		}
	}); //end ajax	
	
	location.reload();

}

function delete_cart_goods(cart_id){
	var cart_id=Number(cart_id);
	var formObj=document.createElement("form");
	var i_cart = document.createElement("input");
	i_cart.name="cart_id";
	i_cart.value=cart_id;
	
	formObj.appendChild(i_cart);
    document.body.appendChild(formObj); 
    formObj.method="post";
    formObj.action="${contextPath}/cart/removeCartGoods.do";
    formObj.submit();
}

function fn_order_each_goods(goods_id,goods_title,goods_sales_price,fileName){
	var total_price,final_total_price,_goods_qty;
	var cart_goods_qty=document.getElementById("cart_goods_qty");
	
	_order_goods_qty=cart_goods_qty.value; //장바구니에 담긴 개수 만큼 주문한다.
	var formObj=document.createElement("form");
	var i_goods_id = document.createElement("input"); 
    var i_goods_title = document.createElement("input");
    var i_goods_sales_price=document.createElement("input");
    var i_fileName=document.createElement("input");
    var i_order_goods_qty=document.createElement("input");
    
    i_goods_id.name="goods_id";
    i_goods_title.name="goods_title";
    i_goods_sales_price.name="goods_sales_price";
    i_fileName.name="goods_fileName";
    i_order_goods_qty.name="order_goods_qty";
    
    i_goods_id.value=goods_id;
    i_order_goods_qty.value=_order_goods_qty;
    i_goods_title.value=goods_title;
    i_goods_sales_price.value=goods_sales_price;
    i_fileName.value=fileName;
    
    formObj.appendChild(i_goods_id);
    formObj.appendChild(i_goods_title);
    formObj.appendChild(i_goods_sales_price);
    formObj.appendChild(i_fileName);
    formObj.appendChild(i_order_goods_qty);

    document.body.appendChild(formObj); 
    formObj.method="post";
    formObj.action="${contextPath}/order/orderEachGoods.do";
    formObj.submit();
}

function fn_order_all_cart_goods(){
//	alert("모두 주문하기");
	var order_goods_qty;
	var order_goods_id;
	var objForm=document.frm_order_all_cart;
	var cart_goods_qty=objForm.cart_goods_qty; //objForm.cart_goods_qty이므로 이 부분을 바꾸면 해당 값의 value 값이 바뀜 
	var h_order_each_goods_qty=objForm.h_order_each_goods_qty;
	var checked_goods=objForm.checked_goods;
	var length=checked_goods.length; //checkbox name="checked_goods" (각 상품 구분 체크하는 부분)
	
	
	//alert(length);
	if(length>1){ //하나 이상 체크 
		for(var i=0; i<length;i++){
			if(checked_goods[i].checked==true){ //1. 해당 배열의 상품이 체크되어 있다면
				order_goods_id=checked_goods[i].value;  //2. 그 배열 순서의 checked_goods의 value(good_id)를 order_goods_id로 가져옴 
				order_goods_qty=cart_goods_qty[i].value; //3. 그 배열 순서의 cart_goods_qty의 value를 order_goods_qty로 가져옴
				
				cart_goods_qty[i].value="";
				cart_goods_qty[i].value=order_goods_id+":"+order_goods_qty; //4. order_goods_id와 order_goods_qty로 cart_goods_qty를 구성
				//alert(select_goods_qty[i].value);
				console.log(cart_goods_qty[i].value);
			}
		}	
	}else{  //하나만 체크 
		order_goods_id=checked_goods.value;
		order_goods_qty=cart_goods_qty.value;
		cart_goods_qty.value=order_goods_id+":"+order_goods_qty;
		//alert(select_goods_qty.value);
	}
		
 	objForm.method="post";
 	objForm.action="${contextPath}/order/orderAllCartGoods.do"; 
	objForm.submit();  //위에서 objForm의 요소들 바꿔서 전송 
}

</script>
</head>
<body>
	<table class="list_view">
		<tbody align=center >
			<tr style="background:#33ff00" >
				<td class="fixed" >구분</td>
				<td colspan=2 class="fixed">상품명</td>
				<td>정가</td>
				<td>판매가</td>
				<td>수량</td>
				<td>합계</td>
				<td>주문</td>
			</tr>
			
			 <c:choose>
				    <c:when test="${ empty myCartList }">
				    <tr>
				       <td colspan=8 class="fixed">
				         <strong>장바구니에 상품이 없습니다.</strong>
				       </td>
				     </tr>
				    </c:when>
			        <c:otherwise>
							 <tr>       
							 
							 <!-- 수정사항 : calcGoodsPrice()함수에서 사용할 value 값들에 ${cnt.index} 붙여준다. -->
							 
				               <form name="frm_order_all_cart">
								      <c:forEach var="item" items="${myGoodsList }" varStatus="cnt">
								       <c:set var="cart_goods_qty" value="${myCartList[cnt.index].cart_goods_qty}" /> 
								       <c:set var="cart_id" value="${myCartList[cnt.index].cart_id}" />                                                                      <!-- cnt.index 인자로 추가 -->
									<td><input type="checkbox" name="checked_goods"  checked  value="${item.goods_id }"  onClick="calcGoodsPrice(${item.goods_sales_price },this,${cnt.index })"></td>
									<td class="goods_image">
									<a href="${contextPath}/goods/goodsDetail.do?goods_id=${item.goods_id }">
										<img width="75" alt="" src="${contextPath}/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}"  />
									</a>
									</td>
									<td>
										<h2>
											<a href="${contextPath}/goods/goodsDetail.do?goods_id=${item.goods_id }">${item.goods_title }</a>
										</h2>
									</td>
									<td class="price"><span>${item.goods_price }원</span></td>
									<td>
									   <strong>
									      <fmt:formatNumber  value="${item.goods_sales_price*0.9}" type="number" var="discounted_price" />
								            ${discounted_price}원(10%할인)
								         </strong>
									</td>
									<td>
									   <input type="text" id="cart_goods_qty" name="cart_goods_qty" size=3 value="${cart_goods_qty}"><br>
										<a href="javascript:modify_cart_qty(${item.goods_id },${item.goods_sales_price*0.9 },${cnt.index });" >
										    <img width=25 alt=""  src="${contextPath}/resources/image/btn_modify_qty.jpg">
										</a>
										<!-- 추가 -->
										<input type="hidden" id="select_goods_qty${cnt.index }" value="${cart_goods_qty }">
									</td>
									<td>
									   <strong>
									    <fmt:formatNumber  value="${item.goods_sales_price*0.9*cart_goods_qty}" type="number" var="total_sales_price" />
								         ${total_sales_price}원
									</strong> </td>
									<td>
									      <a href="javascript:fn_order_each_goods('${item.goods_id }','${item.goods_title }','${item.goods_sales_price}','${item.goods_fileName}');">
									       	<img width="75" alt=""  src="${contextPath}/resources/image/btn_order.jpg">
											</a><br>
									 	<a href="#"> 
									 	   <img width="75" alt=""
											src="${contextPath}/resources/image/btn_order_later.jpg">
										</a><br> 
										<a href="#"> 
										   <img width="75" alt=""
											src="${contextPath}/resources/image/btn_add_list.jpg">
										</A><br> 
										<a href="javascript:delete_cart_goods('${cart_id}');""> 
										   <img width="75" alt=""
											   src="${contextPath}/resources/image/btn_delete.jpg">
									   </a>
									</td>
							</tr>
				
								<c:set  var="totalGoodsPrice" value="${totalGoodsPrice+item.goods_sales_price*0.9*cart_goods_qty }" />
								<c:set  var="totalGoodsNum" value="${totalGoodsNum+1 }" />
								
								<!-- 추가 총주문개수를 위한 -->
								<c:set var="totalNum" value="${totalNum + cart_goods_qty}" />
							   </c:forEach>
						    
						</tbody>
					</table>
				     	
					<div class="clear"></div>
	         </c:otherwise>
	</c:choose> 
	<br>
	<br>
	
	
	<table  width=80%   class="list_view" style="background:#cacaff">
	<tbody>
	     <tr  align=center  class="fixed" >
	       <td class="fixed">총 상품수 </td>
	       <td>총 상품금액</td>
	       <td>  </td>
	       <td>총 배송비</td>
	       <td>  </td>
	       <td>총 할인 금액 </td>
	       <td>  </td>
	       <td>최종 결제금액</td>
	     </tr>
		<tr cellpadding=40  align=center >
			<td id="">
			  <p id="p_totalGoodsNum">${totalGoodsNum}개 </p>
			  <input id="h_totalGoodsNum"type="hidden" value="${totalGoodsNum}"  />
			</td>
	       <td>
	          <p id="p_totalGoodsPrice">
	          <fmt:formatNumber  value="${totalGoodsPrice}" type="number" var="total_goods_price" />
				         ${total_goods_price}원
	          </p>
	          <input id="h_totalGoodsPrice"type="hidden" value="${totalGoodsPrice}" />
	       </td>
	       <td> 
	          <img width="25" alt="" src="${contextPath}/resources/image/plus.jpg">  
	       </td>
	       <td>
	         <p id="p_totalDeliveryPrice">${totalDeliveryPrice }원  </p>
	         <input id="h_totalDeliveryPrice"type="hidden" value="${totalDeliveryPrice}" />
	       </td>
	       <td> 
	         <img width="25" alt="" src="${contextPath}/resources/image/minus.jpg"> 
	       </td>
	       <td>  
	         <p id="p_totalSalesPrice"> 
				         ${totalDiscountedPrice}원
	         </p>
	         <input id="h_totalSalesPrice"type="hidden" value="${totalSalesPrice}" />
	       </td>
	       <td>  
	         <img width="25" alt="" src="${contextPath}/resources/image/equal.jpg">
	       </td>
	       <td>
	          <p id="p_final_totalPrice">
	          <fmt:formatNumber  value="${totalGoodsPrice+totalDeliveryPrice-totalDiscountedPrice}" type="number" var="total_price" />
	            ${total_price}원
	          </p>
	          <input id="h_final_totalPrice" type="hidden" value="${totalGoodsPrice+totalDeliveryPrice-totalDiscountedPrice}" />
	       </td>
		</tr>
		</tbody>
	</table>
	
	<!-- 추가 -->
	<table  width=80%   class="list_view" style="background:#cacaff">
	    <tbody>
	      <tr  align=center  class="fixed" >
	       <td class="fixed">총 주문상품 개수 </td>
	       <td></td>
	       <td>  </td>
	       <td></td>
	       <td>  </td>
	       <td> </td>
	       <td>  </td>
	       <td></td>
	     </tr>
		<tr cellpadding=40  align=center >
		   <td>
		      <p id="p_totalNum">${totalNum } 개</p>
		      <input type="hidden" value="${totalNum }" id="h_totalNum"/>
		   </td>
		</tr>
	    </tbody>
	</table>
	
	<center>
    <br><br>	
		 <a href="javascript:fn_order_all_cart_goods()">
		 	<img width="75" alt="" src="${contextPath}/resources/image/btn_order_final.jpg">
		 </a>
		 <a href="#">
		 	<img width="75" alt="" src="${contextPath}/resources/image/btn_shoping_continue.jpg">
		 </a>
	<center>
</form>	
