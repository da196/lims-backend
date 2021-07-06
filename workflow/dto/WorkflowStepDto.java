/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.dto;

import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowStepTypeEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class WorkflowStepDto {
    
    @Min(0)
    private Long id;
    
    @NotBlank(message="code is required")
    private String code;
    
    @NotBlank(message="name is required")
    private String name;
    
    @Min(0)
    private Long currentRoleID;
    
    @NotNull(message="applicant notify cannot be null")
    private Boolean applicantNotify=false;  
        
    @NotNull(message="step type is required")
    private WorkflowStepTypeEnum stepType=WorkflowStepTypeEnum.NORMAL;
    
    @NotNull(message="applicable state is required")
    private ApplicableStateEnum applicableState=ApplicableStateEnum.UNCHARGABLE;
    
    @NotNull(message="decisions cannot be null")
    @Valid
    private Set<WorkflowStepDecisionDto> decisions;
    
    private Integer dueDays;
    
    @Min(0)
    private Long formId;
    @Min(0)
    private Long attachmentType;
}
