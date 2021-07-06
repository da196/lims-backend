/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class TaskActionInternalDto {
    
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
    
    private String presentationDate;
    private String presentationTime;
    
    @NotNull(message="findings cannot be null")
    @Valid
    private List<FormFindingDto> findings;
    
    @NotNull(message="attachments cannot be null")
    private List<AttachmentDto> attachments;
}
