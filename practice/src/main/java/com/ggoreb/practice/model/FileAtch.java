package com.ggoreb.practice.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class FileAtch {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	// 원본 파일명 (사용자가 업로드한 파일명)
	String originalFileName;
	
	// 저장 파일명 (중복으로 인해 변경된 파일명)
	String saveFileName;
	
	@ManyToOne
	Question question;
	
}
