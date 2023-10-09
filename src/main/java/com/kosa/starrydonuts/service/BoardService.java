package com.kosa.starrydonuts.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosa.starrydonuts.dao.AttacheFileDAO;
import com.kosa.starrydonuts.dao.BoardDAO;
import com.kosa.starrydonuts.dao.CommentDAO;
import com.kosa.starrydonuts.domain.AttacheFileDTO;
import com.kosa.starrydonuts.domain.BoardDTO;

@Service
public class BoardService {
	
	@Autowired
	private BoardDAO boardDAO;

	@Autowired
	private CommentDAO commentDAO;
	
	@Autowired
	private AttacheFileDAO attacheFileDAO;
	
	
	
	//1. �Խ��� ���
	public List<BoardDTO> boardList() throws Exception {
		System.out.println("service.boardList() invoked.");
		
		return boardDAO.boardList();
	}
	
	//2. �Խ��� �󼼺���
	public BoardDTO boardGet(int boardid) throws Exception {
		System.out.println("service.boardGet() invoked.");
		
		// ��ȸ�� ����
		boardDAO.viewCount(boardid);
		
		// �Խ��� �󼼺���
		BoardDTO boardDTO = boardDAO.boardGet(boardid);
		
		// ��� ��� �����ϱ�
		// boardDTO.setCommentList(commentDAO.commentList(boardid));
		
		
		return boardDAO.boardGet(boardid);
	}
	
	
	//3. �Խ��� �۾���
	public boolean boardInsert(BoardDTO board) throws Exception {
		System.out.println("service.boardInsert() invoked.");
		
		boolean result = boardDAO.boardInsert(board);
		
		
		
		List<BoardDTO> list = attacheFileDAO.boardId(board);
		int firstFileBoardId = -1; // �⺻������ -1 �Ǵ� �ٸ� ���� �����Ͻʽÿ�.

		if (!list.isEmpty()) {
		    BoardDTO firstFile = list.get(0);
		    firstFileBoardId = firstFile.getBoardid();
		}
		

		System.out.println("ù ��° ������ boardid: " + firstFileBoardId);
		
		if (board.getAttacheFileList() != null) {
			for (AttacheFileDTO attacheFile : board.getAttacheFileList()) {
				attacheFile.setBoardid(firstFileBoardId);
				attacheFileDAO.insert(attacheFile);
			}
		}
		
		return result;
	} 
	
	
	//4. �Խ��� �����ϱ�
	public boolean boardUpdate(BoardDTO board) throws Exception {
		System.out.println("service.boardUpdate() invoked.");
		
		return boardDAO.boardUpdate(board);
	}

	
	//5. �Խ��� �����ϱ�
	public boolean boardDelete(BoardDTO board) throws Exception {
		System.out.println("service.boardDelete() invoked.");
		
		return boardDAO.boardDelete(board);
	}
	
	
	//6. ���ο� TOP5 ����ϱ�
	public List<BoardDTO> boardTop5() throws Exception {
		System.out.println("service.boardTop5() invoked.");

		return boardDAO.boardTop5();
	}
	
	
	//7. ��ȸ�� ����
	public int viewCount(int boardid) throws Exception {
		System.out.println("service.viewCount() invoked.");
		
		return boardDAO.viewCount(boardid);
	}
	

	//8. üũ�ڽ� �Խñ� �����ϱ�
	public boolean boardsDelete(String[] boardids) throws Exception {
		System.out.println("service.boardsDelete() invoked.");
		System.out.println("boardIds.toString() : " + Arrays.toString(boardids));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("list", boardids);
		return 0 != boardDAO.boardsDelete(params);
	} // deleteBoards
	
	
	//9. ������ ����ϱ�
//	public List<BoardDTO> boardList2(BoardDTO board) throws Exception {
//		System.out.println("board.service.getBoardList2() �Լ� ȣ��� -> " + board);
//		
//		return boardDAO.boardList2(board);
//	} // getBoardList2
	
	
	
	//10. �Խ��� ����¡ ���(�Ķ���Ͱ� notice��!)
	public Map<String, Object> noticePageList(BoardDTO board) throws Exception {
		System.out.println("notice.service.getNoticeList() ����¡ ��� �Լ� ȣ���");

		//1. ��ü �Ǽ��� ��´�
		board.setTotalCount(boardDAO.getTotalCount(board));
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("board", board);
		result.put("list", boardDAO.boardPagingList(board));
		
		return result;
		
	} // getNoticeList
	
	
	//10. �Խñ� 10���� ���ƿ����ϴ� �޼���
	public List<BoardDTO> boardListBoforeN(BoardDTO board) throws Exception {
		System.out.println("service.boardListBoforeN() invoked.");
		
		return boardDAO.boardListBoforeN(board);
	}

	
	//11. ��� �߰��ϱ�
	public int reply(BoardDTO board) {
		System.out.println("service.reply() invoked.");
		
		return boardDAO.reply(board);
	}


}
