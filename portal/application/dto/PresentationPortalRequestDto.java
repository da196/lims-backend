/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import java.util.List;
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
public class PresentationPortalRequestDto {
    
    @NotNull(message="licenseId is required")
    private Long licenseId;
    
    @NotNull(message="decision is required")
    private WorkflowDecisionEnum decision;
    
    @NotBlank(message="comment is required")
    private String comment;
    
    @NotNull(message="attachments cannot be null")
    private List<AttachmentDto> attachments;
}
