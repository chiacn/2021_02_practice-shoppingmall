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

//�߰� (HashMap ���� �׳� Map���� ������ �̰� �������)
import java.util.Map;


public abstract class BaseController  { //abstract class �ΰ� ���� 
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
				if(! file.exists()){ //��λ� ������ �������� ���� ���
					if(file.getParentFile().mkdirs()){ //��ο� �ش��ϴ� ���丮���� ����
							file.createNewFile(); //���� ���� ����
					}
				}
				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH +"\\"+"temp"+ "\\"+originalFileName)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����
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
		DecimalFormat df = new DecimalFormat("00"); //�������� ������ ����� �� 
		Calendar cal=Calendar.getInstance(); //Calendar ��ü ����
		
		//�ּ��ܰ� - ����� ���� �������� �ľ��Ѱ͵�.		
		endYear   = Integer.toString(cal.get(Calendar.YEAR)); //cal.get(Calendar.YEAR)�� ���� ���� toString���� Stringȭ . ex. 2021
		
		/*// ex. 02�� ���Դµ�, 1.cal.get(Calendar.MONTH)+1�� ���� ����ְ� 
		 * 2. DecimalFormat���� 00�������� 2->02 �̷��� ǥ��  3. +1�� ���ִ� ���� - MONTH�� 0���� ���� */
		endMonth  = df.format(cal.get(Calendar.MONTH) + 1); 
		
		endDay   = df.format(cal.get(Calendar.DATE));
		endDate = endYear +"-"+ endMonth +"-"+endDay; //������ 
		
		if(fixedSearchPeriod == null) { //fixedSearchPeriod�� null�� �� (��¥ �˻� ������ ��)
			cal.add(cal.MONTH,-4); // �ƹ� ������ �� ���� �� �˻� �ݰ��� ���� 4���� �������ΰ��� �׷��� �ؿ� beginYear, beginMonth�� ���� -4 ���༭ 2021/02 ����, 2020/10���� ����.
		}else if(fixedSearchPeriod.equals("one_week")) {
			cal.add(Calendar.DAY_OF_YEAR, -7);
		}else if(fixedSearchPeriod.equals("two_week")) {  // �˻��Ⱓ 2�� �� ��, beginDat 
			cal.add(Calendar.DAY_OF_YEAR, -14);
		}else if(fixedSearchPeriod.equals("one_month")) {
			cal.add(cal.MONTH,-1);
		}else if(fixedSearchPeriod.equals("two_month")) {
			cal.add(cal.MONTH,-2);
		}else if(fixedSearchPeriod.equals("three_month")) {
			cal.add(cal.MONTH,-3);
		}else if(fixedSearchPeriod.equals("four_month")) {
			cal.add(cal.MONTH,-4);
			
		}else if(fixedSearchPeriod.equals("today")) { //�Ϸ���� �߰�
			cal.add(Calendar.DAY_OF_YEAR, 0); 
	    }
		
		
		beginYear   = Integer.toString(cal.get(Calendar.YEAR));
		beginMonth  = df.format(cal.get(Calendar.MONTH) + 1);
		beginDay   = df.format(cal.get(Calendar.DATE));
		beginDate = beginYear +"-"+ beginMonth +"-"+beginDay;
		
		return beginDate+","+endDate; 
		/*
		 endDate�� ���� ��¥, beginDate�� endDate�� ������ ����, ���� ������ 2021-02-05�� ������ beginDate�� 2020-10-05�� �����Ǿ��ٸ�
		 �߰��� ","�� �پ 2020-10-05,2021-02-05�� ���ϵ�. �̰� �� MyPageController��  listMyOrderHistory �޼�����
		 String [] tempDate=calcSearchPeriod(fixedSearchPeriod).split(",");���� split(",")�� ������ 
		 
		 beginDate[0] = 2020-10-05
		 beginDate[1] = 2021-02-05  �̷������� �޾����°�		 
		 
		*/
		 
	}
	

	
}
