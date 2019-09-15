package com.rebusgenerator.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Entity
@Table(name = "syllable")
public class Syllable implements Serializable {

private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "syllable_id")
    private long syllableId;
	
	@Column(name = "syllable")
	private String syllable;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "syllable_lang", referencedColumnName = "language_id")
	private Language syllableLang;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "syllables")
	private Set<RebusImagePuzzle> connectedWords;

	public Syllable() {
		super();
	}
	
	public Syllable(String syllable) {
		super();
		this.syllable = syllable;
	}
	
	public void addWord(RebusImagePuzzle word) {
		if (this.connectedWords == null) {
			this.connectedWords = new HashSet<RebusImagePuzzle>();
		}
		this.connectedWords.add(word);
	}

	public long getSyllableId() {
		return syllableId;
	}

	public void setSyllableId(long syllableId) {
		this.syllableId = syllableId;
	}

	public String getSyllable() {
		return syllable;
	}

	public void setSyllable(String syllable) {
		this.syllable = syllable;
	}

	public Set<RebusImagePuzzle> getConnectedW_words() {
		return connectedWords;
	}

	public void setConnectedWords(Set<RebusImagePuzzle> connectedWords) {
		this.connectedWords = connectedWords;
	}

	public Language getSyllableLang() {
		return syllableLang;
	}

	public void setSyllableLang(Language syllableLang) {
		this.syllableLang = syllableLang;
	}

	public Set<RebusImagePuzzle> getConnectedWords() {
		return connectedWords;
	}
	
	
}
