package com.workflow.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
@ToString
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    private String password;
    private String email;

	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
	
    private Set<Role> roles = new HashSet<>();
	@JsonIgnore
	@OneToMany(mappedBy="owner",cascade = CascadeType.ALL)
	private List<demande> demande;
}