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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Entity
@Table(name = "rebus_image_puzzle")
public class RebusImagePuzzle implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rebus_image_puzzle_id")
    private long rebusImagePuzzleId;

	@Column(name = "image_word")
	private String imageWord;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_lang", referencedColumnName = "language_id")
	private Language wordLang;
	
	@Column(name = "image_name")
	private String imageName;
	
	@Column(name = "image_word_type")
	private ImageWordType ImageWordType;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "word_syllable", 
        joinColumns = { @JoinColumn(name = "rebus_image_puzzle_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "syllable_id") }
    )
	private Set<Syllable> syllables;
	
	public RebusImagePuzzle() {
		super();
	}

	public RebusImagePuzzle(String imageWord, String imageName) {
		super();
		this.imageWord = imageWord;
		this.imageName = imageName;
	}

	public void addSyllable(Syllable syllable) {
		if (this.syllables == null) {
			this.syllables = new HashSet<Syllable>();
		}
		this.syllables.add(syllable);
	}
	
	public long getRebusImagePuzzleId() {
		return rebusImagePuzzleId;
	}

	public void setRebusImagePuzzleId(long rebusImagePuzzleId) {
		this.rebusImagePuzzleId = rebusImagePuzzleId;
	}

	public String getImageWord() {
		return imageWord;
	}

	public void setImageWord(String imageWord) {
		this.imageWord = imageWord;
	}

	public Language getWordLang() {
		return wordLang;
	}

	public void setWordLang(Language wordLang) {
		this.wordLang = wordLang;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ImageWordType getImageWordType() {
		return ImageWordType;
	}

	public void setImageWordType(ImageWordType imageWordType) {
		ImageWordType = imageWordType;
	}

	public Set<Syllable> getSyllables() {
		return syllables;
	}

	public void setSyllables(Set<Syllable> syllables) {
		this.syllables = syllables;
	}

    
}
