package com.rebusgenerator.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Entity
@Table(name = "language")
public class Language implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private long languageId;
	
	@Column(name = "lang")
	private String lang;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "syllableLang", cascade = CascadeType.MERGE)
	private List<Syllable> syllables;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wordLang", cascade = CascadeType.MERGE)
	private List<RebusImagePuzzle> words;
	
	public Language() {
		super();
	}
	
	public Language(String lang) {
		super();
		this.lang = lang;
	}

	public long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(long languageId) {
		this.languageId = languageId;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}
