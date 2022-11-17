package com.workflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Traitement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Etat etats;
    private String by;


    @JsonIgnore
    @OneToMany(mappedBy = "traitement1", cascade = CascadeType.ALL)
    private List<Demande> listDemande1;
    @JsonIgnore
    @OneToMany(mappedBy = "traitement2", cascade = CascadeType.ALL)
    private List<Demande> listDemande2;

    public Traitement(Etat etats, String by) {
        super();
        this.etats = etats;
        this.by = by;
    }

    public Traitement(Etat etats) {
        super();
        this.etats = etats;
    }


}
