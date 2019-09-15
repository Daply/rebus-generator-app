package com.rebusgenerator.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
public class KeyWordImage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String word;
	
	private String lang;
	
	private MultipartFile file;
	
	public KeyWordImage() {
		this.word = new String();
	}
	
	public KeyWordImage(String word, String lang, MultipartFile file) {
		this.word = word;
		this.lang = lang;
		this.file = file;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}

