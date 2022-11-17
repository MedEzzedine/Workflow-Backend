package com.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @Column(updatable = true, nullable = false)

    private String nom;

    public Permissions(String nom) {
        super();
        this.nom = nom;
    }

}
