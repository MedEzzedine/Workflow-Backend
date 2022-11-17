package com.workflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demanderecu_id {

    private List<Integer> demande_enattente_id;
    private List<Integer> demande_nonarrive_id;
}