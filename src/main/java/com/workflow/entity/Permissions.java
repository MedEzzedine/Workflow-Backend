package com.workflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class Permissions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( updatable = false, nullable = false)
	private Integer id;
	@Column(updatable = true, nullable = false)
	
	private String nom;
	public Permissions(String nom) {
		super();
		this.nom = nom;
	}
	
}
