package com.bookshop01.common.base;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.bookshop01.goods.vo.ImageFileVO;

//추가 (HashMap 말고 그냥 Map으로 쓰려면 이거 해줘야함)
import java.util.Map;


public abstract class BaseController  { //abstract class 인거 주의 
	private static final String CURR_IMAGE_REPO_PATH = "C:\\shopping\\file_repo";
	
	protected List<ImageFileVO> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		List<ImageFileVO> fileList= new ArrayList<ImageFileVO>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while(fileNames.hasNext()){
			ImageFileVO imageFileVO =new ImageFileVO();
			String fileName = fileNames.next();
			imageFileVO.setFileType(fileName);
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName=mFile.getOriginalFilename();
			imageFileVO.setFileName(originalFileName);
			fileList.add(imageFileVO);
			
			File file = new File(CURR_IMAGE_REPO_PATH +"\\"+ fileName);
			if(mFile.getSize()!=0){ //File Null Check
				if(! file.exists()){ //경로상에 파일이 존재하지 않을 경우
					if(file.getParentFile().mkdirs()){ //경로에 해당하는 디렉토리들을 생성
							file.createNewFile(); //이후 파일 생성
					}
				}
				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH +"\\"+"temp"+ "\\"+originalFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
			}
		}
		return fileList;
	}
	
	private void deleteFile(String fileName) {
		File file =new File(CURR_IMAGE_REPO_PATH+"\\"+fileName);
		try{
			file.delete();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="/*.do" ,method={RequestMethod.POST,RequestMethod.GET})
	protected  ModelAndView viewForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	
	protected String calcSearchPeriod(String fixedSearchPeriod){
		String beginDate=null;
		String endDate=null;
		String endYear=null;
		String endMonth=null;
		String endDay=null;
		String beginYear=null;
		String beginMonth=null;
		String beginDay=null;
		DecimalFormat df = new DecimalFormat("00"); //십진법의 서식을 만드는 것 
		Calendar cal=Calendar.getInstance(); //Calendar 객체 생성
		
		//주석단거 - 디버그 모드로 돌려보고 파악한것들.		
		endYear   = Integer.toString(cal.get(Calendar.YEAR)); //cal.get(Calendar.YEAR)로 받은 값을 toString으로 String화 . ex. 2021
		
		/*// ex. 02가 나왔는데, 1.cal.get(Calendar.MONTH)+1로 월을 얻어주고 
		 * 2. DecimalFormat으로 00서식으로 2->02 이렇게 표시  3. +1을 해주는 이유 - MONTH가 0부터 시작 */
		endMonth  = df.format(cal.get(Calendar.MONTH) + 1); 
		
		endDay   = df.format(cal.get(Calendar.DATE));
		endDate = endYear +"-"+ endMonth +"-"+endDay; //합쳐줌 
		
		if(fixedSearchPeriod == null) { //fixedSearchPeriod가 null일 때 (날짜 검색 안했을 때)
			cal.add(cal.MONTH,-4); // 아무 설정도 안 했을 때 검색 반경이 이전 4개월 전부터인가봄 그래서 밑에 beginYear, beginMonth도 보면 -4 해줘서 2021/02 기준, 2020/10월로 나옴.
		}else if(fixedSearchPeriod.equals("one_week")) {
			cal.add(Calendar.DAY_OF_YEAR, -7);
		}else if(fixedSearchPeriod.equals("two_week")) {  // 검색기간 2주 일 때, beginDat 
			cal.add(Calendar.DAY_OF_YEAR, -14);
		}else if(fixedSearchPeriod.equals("one_month")) {
			cal.add(cal.MONTH,-1);
		}else if(fixedSearchPeriod.equals("two_month")) {
			cal.add(cal.MONTH,-2);
		}else if(fixedSearchPeriod.equals("three_month")) {
			cal.add(cal.MONTH,-3);
		}else if(fixedSearchPeriod.equals("four_month")) {
			cal.add(cal.MONTH,-4);
			
		}else if(fixedSearchPeriod.equals("today")) { //하루단위 추가
			cal.add(Calendar.DAY_OF_YEAR, 0); 
	    }
		
		
		beginYear   = Integer.toString(cal.get(Calendar.YEAR));
		beginMonth  = df.format(cal.get(Calendar.MONTH) + 1);
		beginDay   = df.format(cal.get(Calendar.DATE));
		beginDate = beginYear +"-"+ beginMonth +"-"+beginDay;
		
		return beginDate+","+endDate; 
		/*
		 endDate는 현재 날짜, beginDate와 endDate의 조합을 리턴, 만약 오늘이 2021-02-05고 위에서 beginDate가 2020-10-05로 설정되었다면
		 중간에 ","가 붙어서 2020-10-05,2021-02-05가 리턴됨. 이걸 또 MyPageController의  listMyOrderHistory 메서드의
		 String [] tempDate=calcSearchPeriod(fixedSearchPeriod).split(",");에서 split(",")로 나눠서 
		 
		 beginDate[0] = 2020-10-05
		 beginDate[1] = 2021-02-05  이런식으로 받아지는거		 
		 
		*/
		 
	}
	

	
}
