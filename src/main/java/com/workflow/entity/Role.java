package com.workflow.entity;

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
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( updatable = false, nullable = false)
	private Integer id;
	@Column(updatable = true, nullable = false)
	private String nom;
	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<Permissions> permissions = new HashSet<>();
	
	//@JsonManagedReference
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Groupe groupe ;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	private Role rolesup;
	
	private int niveau;
	
	public Role(String nom) {
		super();
		this.nom = nom;
	}

	public Role(String nom, int niveau) {
		super();
		this.nom = nom;
		this.niveau = niveau;
	}

	
	
	
	
	

}