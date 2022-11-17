package com.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @Column(updatable = true, nullable = false)
    private String nom;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Permissions> permissions = new HashSet<>();

    //@JsonManagedReference
    @ManyToOne(cascade = CascadeType.MERGE)
    private Groupe groupe;

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