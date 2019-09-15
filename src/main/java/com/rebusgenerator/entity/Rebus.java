package com.rebusgenerator.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Entity
@Table(name = "rebus")
public class Rebus implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rebus_id")
    private long rebusId;

    @Column(name = "rebus_word")
    private String rebusWord;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> rebusSequence;
    
    public Rebus() {
		super();
	}

	public Rebus(Long rebusId, String rebusWord) {
		super();
		this.rebusId = rebusId;
		this.rebusWord = rebusWord;
	}

	public Long getRebusId() {
		return rebusId;
	}

	public String getRebusWord() {
		return rebusWord;
	}

	public void setRebusWord(String rebusWord) {
		this.rebusWord = rebusWord;
	}

	public List<String> getRebusSequence() {
		return rebusSequence;
	}

	public void setRebusSequence(List<String> rebusSequence) {
		this.rebusSequence = rebusSequence;
	}

	public void setRebusId(long rebusId) {
		this.rebusId = rebusId;
	}
	
}
