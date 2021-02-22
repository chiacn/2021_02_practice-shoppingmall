<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html >
<html>
<head>
<meta charset="utf-8">
<script>
function search_goods_list(fixeSearchPeriod){
	var formObj=document.createElement("form");
	var i_fixedSearch_period = document.createElement("input");
	i_fixedSearch_period.name="fixedSearchPeriod";
	i_fixedSearch_period.value=searchPeriod;
    formObj.appendChild(i_fixedSearch_period);
    document.body.appendChild(formObj); 
    formObj.method="get";
    formObj.action="${contextPath}/admin/goods/adminGoodsMain.do";
    formObj.submit();
}


function  calcPeriod(search_period){
	var dt = new Date();
	var beginYear,endYear;
	var beginMonth,endMonth;
	var beginDay,endDay;
	var beginDate,endDate;
	
	endYear = dt.getFullYear();
	endMonth = dt.getMonth()+1;
	endDay = dt.getDate();
	if(search_period=='today'){
		beginYear=endYear;
		beginMonth=endMonth;
		beginDay=endDay;
	}else if(search_period=='one_week'){
		beginYear=dt.getFullYear();
		beginMonth=dt.getMonth()+1;
		dt.setDate(endDay-7);
		beginDay=dt.getDate();
		
	}else if(search_period=='two_week'){
		beginYear = dt.getFullYear();
		beginMonth = dt.getMonth()+1;
		dt.setDate(endDay-14);
		beginDay=dt.getDate();
	}else if(search_period=='one_month'){
		beginYear = dt.getFullYear();
		dt.setMonth(endMonth-1);
		beginMonth = dt.getMonth();
		beginDay = dt.getDate();
	}else if(search_period=='two_month'){
		beginYear = dt.getFullYear();
		dt.setMonth(endMonth-2);
		beginMonth = dt.getMonth();
		beginDay = dt.getDate();
	}else if(search_period=='three_month'){
		beginYear = dt.getFullYear();
		dt.setMonth(endMonth-3);
		beginMonth = dt.getMonth();
		beginDay = dt.getDate();
	}else if(search_period=='four_month'){
		beginYear = dt.getFullYear();
		dt.setMonth(endMonth-4);
		beginMonth = dt.getMonth();
		beginDay = dt.getDate();
	}
	
	if(beginMonth <10){
		beginMonth='0'+beginMonth;
		if(beginDay<10){
			beginDay='0'+beginDay;
		}
	}
	if(endMonth <10){
		endMonth='0'+endMonth;
		if(endDay<10){
			endDay='0'+endDay;
		}
	}
	endDate=endYear+'-'+endMonth +'-'+endDay;
	beginDate=beginYear+'-'+beginMonth +'-'+beginDay;
	//alert(beginDate+","+endDate);
	return beginDate+","+endDate;
}

//상품 삭제 기능 추가 구현
function fn_deleteGoods(goods_id, url) {
	var form = document.createElement("form");
	form.setAttribute("method","post");
	form.setAttribute("action",url);
	
	var goodsIdInput = document.createElement("input");
	goodsIdInput.type = "hidden";
	goodsIdInput.setAttribute("name", "goods_id");
	goodsIdInput.setAttribute("value", goods_id);
	
	form.appendChild(goodsIdInput);
	document.body.appendChild(form);
	form.submit();
	
}

</script>
</head>
<body>
	<H3>상품 조회</H3>
	<form  method="post">	
		<TABLE cellpadding="10" cellspacing="10"  >
			<TBODY>
				<TR >
					<TD>
						<input type="radio" name="r_search"  checked/> 등록일로조회 &nbsp;&nbsp;&nbsp;
						<input type="radio" name="r_search" />상세조회 &nbsp;&nbsp;&nbsp;
					</TD>
				</TR>
				<TR >
					<TD>
					  <select name="curYear">
					    <c:forEach   var="i" begin="0" end="5">
					      <c:choose>
					        <c:when test="${endYear==endYear-i}">
					          <option value="${endYear}" selected>${endYear}</option>
					        </c:when>
					        <c:otherwise>
					          <option value="${endYear-i }">${endYear-i }</option>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>
					</select>년 <select name="curMonth" >
						 <c:forEach   var="i" begin="1" end="12">
					      <c:choose>
					        <c:when test="${endMonth==i }">
					          <option value="${i }"  selected>${i }</option>
					        </c:when>
					        <c:otherwise>
					          <option value="${i }">${i }</option>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>					
					</select>월
					
					 <select name="curDay">
					  <c:forEach   var="i" begin="1" end="31">
					      <c:choose>
					        <c:when test="${endDay==i}">
					          <option value="${i }"  selected>${i }</option>
					        </c:when>
					        <c:otherwise>
					          <option value="${i }">${i }</option>
					        </c:otherwise>
					      </c:choose>
					    </c:forEach>	
					</select>일  &nbsp;이전&nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="javascript:search_goods_list('today')">
					   <img   src="${contextPath}/resources/image/btn_search_one_day.jpg">
					</a>
					<a href="javascript:search_goods_list('one_week')">
					   <img   src="${contextPath}/resources/image/btn_search_1_week.jpg">
					</a>
					<a href="javascript:search_goods_list('two_week')">
					   <img   src="${contextPath}/resources/image/btn_search_2_week.jpg">
					</a>
					<a href="javascript:search_goods_list('one_month')">
					   <img   src="${pageContext.request.contextPath}/resources/image/btn_search_1_month.jpg">
					</a>
					<a href="javascript:search_goods_list('two_month')">
					   <img   src="${contextPath}/resources/image/btn_search_2_month.jpg">
					</a>
					<a href="javascript:search_goods_list('three_month')">
					   <img   src="${contextPath}/resources/image/btn_search_3_month.jpg">
					</a>
					<a href="javascript:search_goods_list('four_month')">
					   <img   src="${contextPath}/resources/image/btn_search_4_month.jpg">
					</a>
					&nbsp;까지 조회
					</TD>
				</TR>
				<tr>
				  <td>
				    <select name="search_condition" disabled >
						<option value="전체" checked>전체</option>
						<option value="제품번호">상품번호</option>
						<option value="제품이름">상품이름</option>
						<option value="제조사">제조사</option>
					</select>
					<input  type="text"  size="30"  disabled/>  
					<input   type="button"  value="조회" disabled/>
				  </td>
				</tr>
				<tr>
				  <td>
					조회한 기간:<input  type="text"  size="4" value="${beginYear}" />년
							<input  type="text"  size="4" value="${beginMonth}"/>월	
							 <input  type="text"  size="4" value="${beginDay}"/>일	
							 &nbsp; ~
							<input  type="text"  size="4" value="${endYear }" />년 
							<input  type="text"  size="4" value="${endMonth }"/>월	
							 <input  type="text"  size="4" value="${endDay }"/>일							 
				  </td>
				</tr>
			</TBODY>
		</TABLE>
		<DIV class="clear">
	</DIV>
</form>	
<DIV class="clear"></DIV>
<TABLE class="list_view">
		<TBODY align=center >
			<tr style="background:#33ff00" >
				<td>상품번호</td>
				<td>상품이름</td>
				<td>저자</td>
				<td>출판사</td>
				<td>상품가격</td>
				<td>입고일자</td>
				<td>출판일</td>
				<td></td>
			</tr>
   <c:choose>
     <c:when test="${empty newGoodsList }">			
			<TR>
		       <TD colspan=8 class="fixed">
				  <strong>조회된 상품이 없습니다.</strong>
			   </TD>
		     </TR>
	 </c:when>
	 <c:otherwise>
     <c:forEach var="item" items="${newGoodsList }">
			 <TR>       
				<TD>
				  <strong>${item.goods_id }</strong>
				</TD>
				<TD >
				 <a href="${pageContext.request.contextPath}/admin/goods/modifyGoodsForm.do?goods_id=${item.goods_id}">
				    <strong>${item.goods_title } </strong>
				 </a> 
				</TD>
				<TD>
				<strong>${item.goods_writer }</strong> 
				</TD>
				<TD >
				   <strong>${item.goods_publisher }</strong> 
				</TD>
				<td>
				  <strong>${item.goods_sales_price }</strong>
				</td>
				<td>
				 <strong>${item.goods_credate }</strong> 
				</td>
				<td>
				    <c:set var="pub_date" value="${item.goods_published_date}" />
					   <c:set var="arr" value="${fn:split(pub_date,' ')}" />
					<strong>
					   <c:out value="${arr[0]}" />
					</strong>
				</td>
				<td>
				<!-- 삭제버튼 추가 -->
				   <input type="button" value="삭제" onClick="fn_deleteGoods('${item.goods_id}', '${contextPath}/admin/goods/deleteGoods.do')" />
				</td>
				
			</TR>
	</c:forEach>
	</c:otherwise>
  </c:choose>
           <tr>
           <!--  기존코드
             <td colspan=8 class="fixed">
                 <c:forEach   var="page" begin="1" end="10" step="1" >
		         <c:if test="${section >1 && page==1 }">
		          <a href="${contextPath}/admin/goods/adminGoodsMain.do?chapter=${section-1}&pageNum=${(section-1)*10 +1 }">&nbsp; &nbsp;</a>
		         </c:if>
		          <a href="${contextPath}/admin/goods/adminGoodsMain.do?chapter=${section}&pageNum=${page}">${(section-1)*10 +page } </a>
		         <c:if test="${page ==10 }">
		          <a href="${contextPath}/admin/goods/adminGooodsMain.do?chapter=${section+1}&pageNum=${section*10+1}">&nbsp; next</a>
		         </c:if> 
	      		</c:forEach> 
             -->
             
             
             <!-- 페이징 추가  -->             
             <td colspan=8 class="fixed">
             
                 <c:if test="${totArticles != null }">
        <c:choose>
             <c:when test="${totArticles > 100}">                                 
               <!-- / 로 나눴을 때 정수 값만 나오게 하기 위하여 fmt 태그 이용  -->
               <!-- 아래는 next가 전체 글 수에 상응되는 만큼만 표시되도록하고, 마지막 부분(section)의 페이지도 전체 글 수만큼만 표기되도록 하는 코드-->
               <c:choose>           
                    
                   <c:when test="${(totArticles%100)>0 }">
                        <fmt:parseNumber var="test" value="${totArticles/100 }" integerOnly="true"/>  
                        <c:set var="lastSec" value="${test +1}"/>
                   </c:when>                                    
                   <c:otherwise>
                       <fmt:parseNumber var="test" value="${totArticles/100 }" integerOnly="true"/>  
                      <c:set var="lastSec" value="${test }"/>
                   </c:otherwise>
               </c:choose> 
                             
               <!-- 추가 - 마지막 section값일 경우 forEach의 end 변수 구하기 -->
               <!-- / 기호를 사용해서 나눌 때 소수점 이하를 제거시켜주어야 하므로 fmt 사용  -->
                <fmt:parseNumber var="test1" value="${(totArticles%100)/10 }" integerOnly="true"/>
               <c:choose>             
                   <c:when test="${(totArticles%100)%10>0}">                   
                       <c:set var="end" value="${test1+1 }"/>
                   </c:when>
                   <c:otherwise>
                       <c:set var="end" value="${test1}"/>
                   </c:otherwise>
               </c:choose>
             
               <!-- 마지막 section인 경우와 아닌 경우로 나눠서 -->
             <c:choose>  
                  <c:when test="${section==lastSec }"> 
		                <c:forEach var="page" begin="1" end="${end }" step="1">
		                    <c:if test="${section > 1 && page == 1 }">  
		                       <a class="no-uline" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section-1}&pageNum=1">pre</a>              
		                    </c:if>		                   
		                    <c:choose>
		                       <c:when test="${page==pageNum }">
		                          <a class="sel-page" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
		                       </c:when>
		                       <c:otherwise>
		                          <a class="no-uline" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
		                       </c:otherwise>
		                    </c:choose>
		                    
		                    <c:if test="${page==10 }">
		                       <a class="no-uline" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section+1}&pageNum=1">next</a>
		                    </c:if>
		                </c:forEach>
		             </c:when>
		             
		             
		             <c:otherwise>
		                 <c:forEach var="page" begin="1" end="10" step="1">
		                    <c:if test="${section > 1 && page == 1 }">   
		                       <a class="no-uline" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section-1}&pageNum=1">pre</a>              
		                    </c:if>	                  
		                    <c:choose>
		                       <c:when test="${page==pageNum }">
		                          <a class="sel-page" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
		                       </c:when>
		                       <c:otherwise>
		                          <a class="no-uline" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
		                       </c:otherwise>
		                    </c:choose>
		                    
		                    <c:if test="${page==10 }">
		                       <a class="no-uline" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section+1}&pageNum=1">next</a>
		                    </c:if>
		                </c:forEach>
		             </c:otherwise>
		       </c:choose>
             </c:when>
             
             <c:when test="${totArticles == 100 }">
                <c:forEach var="page" begin="1" end="10" step="1">              
                  <c:choose>
                     <c:when test="${page==pageNum }">
                         <a class="sel-page" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section}&pageNum=${page}">${page }</a>
                     </c:when>
                     <c:otherwise>
                         <a class="no-uline" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section}&pageNum=${page}">${page }</a>
                     </c:otherwise>
                  </c:choose>                 
                </c:forEach>                
             </c:when>
             
             <c:when test="${totArticles<100 }">
             
	              <!-- 마지막 section에서  x0(예 - 50)개인지 xx(예- 53) 개인지에 따라 유동적으로 페이지 조절해주기 위해 변수 추가-->
	              <c:choose>
	                  <c:when test="${totArticles%10==0 }"> 
	                      <c:set var="end" value="${totArticles/10 }"/>
	                  </c:when>
	                  <c:otherwise> 
	                      
	                      <c:set var="end" value="${totArticles/10  +1 }"/>
	                  </c:otherwise>
	              </c:choose>  
                  
                  <c:forEach var="page" begin="1" end="${end }" step="1">
                      <c:choose>
                         <c:when test="${page==pageNum }">  
                             <a class="sel-page" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section}&pageNum=${page}">${page }</a>
                         </c:when>
                         <c:otherwise>
                             <a class="no-uline" href="${contextPath }/admin/goods/adminGoodsMain.do?section=${section }&pageNum=${page }">${page }</a>
                         </c:otherwise>
                      </c:choose>
                  </c:forEach>
            
             </c:when>
        </c:choose>
    </c:if>
          
      <!-- 여기까지 페이징 추가 부분  -->   
             
		</TBODY>
		
	</TABLE>
	<DIV class="clear"></DIV>
	<br><br><br>
<H3>상품등록하기</H3>
<DIV id="search">
	<form action="${contextPath}/admin/goods/addNewGoodsForm.do">
		<input   type="submit" value="상품 등록하기">
	</form>
</DIV>
</body>
</html>