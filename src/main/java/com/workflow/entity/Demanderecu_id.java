package com.workflow.entity;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demanderecu_id {

    private List<Integer> demande_enattente_id;
    private List<Integer> demande_nonarrive_id;
}