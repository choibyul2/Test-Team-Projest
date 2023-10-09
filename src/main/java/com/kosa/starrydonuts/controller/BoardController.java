package com.kosa.starrydonuts.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kosa.starrydonuts.domain.AttacheFileDTO;
import com.kosa.starrydonuts.domain.BoardDTO;
import com.kosa.starrydonuts.domain.CommentDTO;
import com.kosa.starrydonuts.service.AttacheFileService;
import com.kosa.starrydonuts.service.BoardService;
import com.kosa.starrydonuts.service.CommentService;


@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	@Autowired
	private CommentService cservice;
	
	@Autowired
	private AttacheFileService aservice;
	
	private static final String CURR_IMAGE_REPO_PATH = "C:\\file_repo";

	
//	�Խ��� ��� =====================================================================================
	
	
	// 1. [����¡]�Խ��� ��ü ��� ������
	@RequestMapping("/board/list2.do")
	public String list2(BoardDTO board, HttpServletRequest req, HttpServletResponse res, String searchTitle) throws Exception {
    	System.out.println("notice.controller.list2() invoked.");
    	
    	Map<String, Object> plist = new HashMap<>();
    	
    	plist = service.noticePageList(board);
		req.setAttribute("plist", plist);
		System.out.println("plist : " +plist);
		
		
		
		return "board/list2";
	}

	
//	�Խ��� �� =====================================================================================
	
	// 2. �Խ��� �� ������
	@ResponseBody
	@RequestMapping(value="/board/detail.do", method = RequestMethod.POST)
	public Map<String, Object> detail(@RequestBody BoardDTO board, Model model) throws Exception {
		System.out.println("board.controller.detail() invoked.");

		
		// �� �� ��ȸ
		Map<String, Object> map = new HashMap<>();
		BoardDTO detail = service.boardGet(board.getBoardid());
    	
		map.put("bdetail", detail);
    	System.out.println("bdetail : " + detail);
    
		
    	// ��� ��ȸ
    	// ��� ��� ���
    	List<CommentDTO> comment = cservice.commentList(board.getBoardid());
    	map.put("comment", comment);
    	System.out.println("��� ������ ��� �������� : " + comment);
    
    	
    	//÷������ ����Ʈ ���
    	List<AttacheFileDTO> file = aservice.getList(board);
    	map.put("file", file);

    	
		return map;
		
	} // detail
	
	
	
	// 3. ȸ���Խ��� ��� ���
	@ResponseBody
	@RequestMapping(value="/board/reply.do", method = RequestMethod.POST)
	public String reply(@RequestBody BoardDTO board, HttpServletRequest req, HttpServletResponse res) throws Exception {
    	System.out.println("board.controller.reply() invoked.");
    	
    	JSONObject result = new JSONObject();
    	int status = service.reply(board);

    	System.out.println("ȸ���Խ��� ��� �������� : " + status);
    	
    	if(status == 1) {
    		result.put("status", true);
    		result.put("message", "ȸ���Խ��� ����� ��ϵǾ����ϴ�.");
    	} else {
    		result.put("status", false);
    		result.put("message", "ȸ���Խ��� ����� ��ϵ��� �ʾҽ��ϴ�.");
    	}

    	System.out.println(result.toString());
		return result.toString();
	}
	
	
	
//	�Խ��� �۾��� =====================================================================================

	
	// 3-2. �Խ��� �۾���
	@ResponseBody
	@RequestMapping(value="/board/insert.do", method = RequestMethod.POST)
	public String insert(BoardDTO board, HttpServletRequest req, HttpServletResponse res, MultipartHttpServletRequest multipartRequest) throws Exception {
		System.out.println("board.controller.insert() invoked.");
		JSONObject jsonResult = new JSONObject();
		
		multipartRequest.setCharacterEncoding("utf-8");
		Map map = new HashMap();
		
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			//System.out.println(name+", "+value);
			map.put(name,value);
		}
		
		board.setAttacheFileList(fileProcess(multipartRequest));

		boolean status = service.boardInsert(board);
		
		jsonResult.put("status", status);
		
		return jsonResult.toString();
	
	} // insert

	
//	�Խ��� �� ���� =====================================================================================

	// 4-1. �Խ��� �� ����
	@ResponseBody
	@RequestMapping(value="/board/delete.do", method = RequestMethod.POST)
	public String delete(@RequestBody BoardDTO board, HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("board.controller.delete() invoked.");
		JSONObject jsonResult = new JSONObject();
		boolean status = service.boardDelete(board);
		
    	System.out.println("ȸ���Խ��� ���� �������� : " + status);
    	
		jsonResult.put("status", status);
		jsonResult.put("message", status ? "���� �����Ǿ����ϴ�" : "������ �߻��Ͽ����ϴ�. �ٽ� �õ����ּ���.");
		
		System.out.println();
		return jsonResult.toString();
		
	} // delete
	
	
	// 4-2. üũ�ڽ� �� ����
	public String deleteBoards(String[] boardids, HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("board.controller.deleteBoards() invoked.");
		JSONObject jsonResult = new JSONObject();
		boolean status = service.boardsDelete(boardids);
		
		jsonResult.put("status", status);
		jsonResult.put("message", status ? "���� �����Ǿ����ϴ�" : "������ �߻��Ͽ����ϴ�. �ٽ� �õ����ּ���.");
		
		return jsonResult.toString();
	
	}

	
//	�Խ��� �� ���� =====================================================================================
	

	
	// 5-1. �Խ��� �� ����
	@ResponseBody
	@RequestMapping(value="/board/update.do", method = RequestMethod.POST)
	public String update(@RequestBody BoardDTO board, HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("board.controller.update() invoked.");
		
		JSONObject jsonResult = new JSONObject();
		boolean status = service.boardUpdate(board);
		
		jsonResult.put("status", status);
		jsonResult.put("message", status ? "���� �����Ǿ����ϴ�." : "������ �߻��Ͽ����ϴ�. �ٽ� �õ����ּ���.");
		
		return jsonResult.toString();
		
	} // update
	
	
	
//	�Խ��� ÷������ =====================================================================================
	
	
	private List<AttacheFileDTO> fileProcess(MultipartHttpServletRequest multipartRequest) throws Exception{
		List<AttacheFileDTO> fileList = new ArrayList<>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("\\yyyy\\MM\\dd");
		
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String fileNameOrg = mFile.getOriginalFilename();
			String realFolder = sdf.format(now.getTime());
			
			File file = new File(CURR_IMAGE_REPO_PATH + realFolder);
			if (file.exists() == false) {
				file.mkdirs();
			}

			String fileNameReal = UUID.randomUUID().toString() + fileNameOrg.substring(fileNameOrg.lastIndexOf("."));
			
			//���� ���� 
			mFile.transferTo(new File(file, fileNameReal)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����

			fileList.add(
					AttacheFileDTO.builder()
					.fileNameOrg(fileNameOrg)
					.fileNameReal(realFolder + "\\" + fileNameReal)
					.length((int) mFile.getSize())
					.contentType(mFile.getContentType())
					.build()
					);
		}
		
		return fileList;
	
	}	

	
}
