package com.workflow.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class traitement implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( updatable = false, nullable = false)
	private Integer id;
	@Enumerated(EnumType.STRING)
	private etat etats;
	private String by;
	
	
	@JsonIgnore 
	@OneToMany(mappedBy ="traitement1" ,cascade = CascadeType.ALL )
	private List<demande> Listdemande1 ;
	@JsonIgnore
	@OneToMany(mappedBy ="traitement2" ,cascade = CascadeType.ALL )
	private List<demande> Listdemande2 ;
	
	public traitement(etat etats, String by) {
		super();
		this.etats = etats;
		this.by = by;
	}

	public traitement(etat etats) {
		super();
		this.etats = etats;
	}
	
	
}
