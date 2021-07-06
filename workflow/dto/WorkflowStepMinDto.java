/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.uaa.dto.RoleMinDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@Setter
@Getter
public class WorkflowStepMinDto {
    
    private Long id;
    private String code;
    private String name;
    private RoleMinDto previousRole;
    private RoleMinDto currentRole;
    private RoleMinDto nextRole;
    private Long stepNumber;
    private boolean active=true;
}
