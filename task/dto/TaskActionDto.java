/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class TaskActionDto {
    
    @NotNull(message="id is required")
    private Long id;
    
    @NotNull(message="decision is required")
    private WorkflowDecisionEnum decision;
    
    @NotNull(message="actors cannot be null")
    private List<Long> actors;
    
    @NotBlank(message="comment is required")
    private String comment;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime presentationDate;
    
    @NotNull(message="findings cannot be null")
    @Valid
    private List<FormFindingDto> findings;
}
