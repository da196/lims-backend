/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@Setter
@Getter
public class WorkflowMaxDto {
    
    private Long id;
    private String code;
    private String name;
    private String description;
    private Long flowableID;
    private boolean active;
    private List<WorkflowStepMinDto> steps;
    
}
