package com.kosa.starrydonuts.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor // �۾���
public class AttacheFileDTO {

	// �ʵ�
	private int fileNo; 			// ÷������ ���̵�
	private int boardid; 			// �Խñ۹�ȣ
	private String fileNameOrg;		// ����ڰ� �ø� ���� ���ϸ� 
	private String fileNameReal;	// ������ ����� ���ϸ� 
	private int    length;			// ������ ����
	private String contentType;		// ������ Ÿ��
	private Date   reg_date;		// ����Ͻ�
	
} // AttacheFileDTO

