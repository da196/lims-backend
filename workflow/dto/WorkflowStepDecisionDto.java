/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class WorkflowStepDecisionDto {
    
    @NotNull(message="decision is required")
    private WorkflowDecisionEnum decision;
    
    @Min(0)
    private Long notificationTemplateId;
    
    @Min(1)
    private Long statusId=0L;
}
