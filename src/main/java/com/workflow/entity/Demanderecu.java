package com.workflow.entity;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demanderecu {

    private List<demande> demande_enattente;
    private List<demande> demande_nonarrive;
}