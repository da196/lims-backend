/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
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
@Data
public class WorkflowDto {
    
    @NotBlank(message="code is required")
    private String code;
    
    @NotBlank(message="name is required")
    private String name;
    
    private String description;
    
    @Min(1)
    private Long workflowType;
    
    @NotNull
    @Valid
    private List<WorkflowStepDto> steps;
}
