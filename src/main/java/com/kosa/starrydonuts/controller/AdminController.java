package com.kosa.starrydonuts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kosa.starrydonuts.domain.AttacheFileDTO;
import com.kosa.starrydonuts.domain.BoardDTO;
import com.kosa.starrydonuts.domain.CommentDTO;
import com.kosa.starrydonuts.domain.NoticeDTO;
import com.kosa.starrydonuts.service.AttacheFileService;
import com.kosa.starrydonuts.service.BoardService;
import com.kosa.starrydonuts.service.CommentService;
import com.kosa.starrydonuts.service.NoticeService;

@Controller
public class AdminController {
	
	@Autowired
	NoticeService noticeService;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	private AttacheFileService attachService;
	
	// ����������
	@RequestMapping("/admin/main.do")
	public String main(HttpServletRequest req, HttpServletResponse res) throws Exception {
    	System.out.println("admin.main() invoked. starrynodnuts-admin");
		
		try { 
			
			List<NoticeDTO> noticeTop5 = noticeService.noticeTop5();
        	req.setAttribute("noticeTop5", noticeTop5);
        	
        	List<BoardDTO> boardTop5 = boardService.boardTop5();
        	req.setAttribute("boardTop5", boardTop5);
        	
        } catch (Exception e) { 
        	e.printStackTrace();
        }

		return "admin/main";
		
	} // main
	
	
	
	
	
	
	
	// 2. [����¡]�������� ��ü ��� ������
	@RequestMapping("/admin//notice/list2.do")
	public String list2(NoticeDTO notice, HttpServletRequest req, HttpServletResponse res, String searchTitle) throws Exception {
    	System.out.println("notice.controller.list2() invoked.");
    	
    	Map<String, Object> plist = noticeService.noticePageList(notice);
		req.setAttribute("plist", plist);
		System.out.println("plist : " +plist);
		
		
		return "admin/notice/list2";
	}
	
	
	
	
	
	
	
	
	

	// 1. [����¡]�Խ��� ��ü ��� ������
	@RequestMapping("/admin/board/list2.do")
	public String list2(BoardDTO board, HttpServletRequest req, HttpServletResponse res, String searchTitle) throws Exception {
    	System.out.println("notice.controller.list2() invoked.");
    	
    	Map<String, Object> plist = new HashMap<>();
    	
    	plist = boardService.noticePageList(board);
		req.setAttribute("plist", plist);
		System.out.println("plist : " +plist);
		
		
		
		return "admin/board/list2";
	}
	
	
	// 2. �Խ��� �� ������
	@ResponseBody
	@RequestMapping(value="/admin/board/detail.do", method = RequestMethod.POST)
	public Map<String, Object> detail(@RequestBody BoardDTO board, Model model) throws Exception {
		System.out.println("board.controller.detail() invoked.");

		
		// �� �� ��ȸ
		Map<String, Object> map = new HashMap<>();
		BoardDTO detail = boardService.boardGet(board.getBoardid());
    	
		map.put("bdetail", detail);
    	System.out.println("bdetail : " + detail);
    
		
    	// ��� ��ȸ
    	// ��� ��� ���
    	List<CommentDTO> comment = commentService.commentList(board.getBoardid());
    	map.put("comment", comment);
    	System.out.println("��� ������ ��� �������� : " + comment);
    
    	
    	//÷������ ����Ʈ ���
    	List<AttacheFileDTO> file = attachService.getList(board);
    	map.put("file", file);

    	
		return map;
		
	} // detail
	
	
	
	
	
	
	
	// ȸ������ �Խ���
	/*
	 * @RequestMapping("/admin/member/main.do") public String
	 * memberList(HttpServletRequest req, HttpServletResponse res) throws Exception
	 * { System.out.println("admin.member.list() invoked. starrynodnuts-admin");
	 * 
	 * 
	 * return "admin/main";
	 * 
	 * } // main
	 */	
	
	
} // end class
