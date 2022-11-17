package com.workflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demanderecu {

    private List<Demande> demande_enattente;
    private List<Demande> demande_nonarrive;
}