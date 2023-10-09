package com.kosa.starrydonuts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosa.starrydonuts.dao.AttacheFileDAO;
import com.kosa.starrydonuts.domain.AttacheFileDTO;
import com.kosa.starrydonuts.domain.BoardDTO;

@Service
public class AttacheFileService {
	
	@Autowired
	private AttacheFileDAO attacheFileDAO;
	
	//1. ÷�� ���� ���
	public List<AttacheFileDTO> getList(BoardDTO board) throws Exception {
		System.out.println("AttacheFileService.getList() �Լ� ȣ���");
		
		return attacheFileDAO.getList(board);
	}


	//2. ÷�� ���� �󼼺���
	public AttacheFileDTO getAttacheFile(String fileNo) throws Exception {
		System.out.println("AttacheFileService.getAttacheFile() �Լ� ȣ���");
		
		return attacheFileDAO.getAttacheFile(fileNo);
	} // getBoard
	
	


} // end class
