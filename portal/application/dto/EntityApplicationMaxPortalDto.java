/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import tz.go.tcra.lims.licencee.dto.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.licence.dto.LicenceBillingDto;
import tz.go.tcra.lims.task.dto.TaskStatusHistoryDto;
import tz.go.tcra.lims.uaa.dto.ApplicantMinDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@Setter
@Getter
public class EntityApplicationMaxPortalDto {
    
    private Long id;
    private Boolean isDraft;
    private String product;
    private List<AttachmentMaxDto> attachments;
    private String currentDecision;
    private String status;
    private List<PayableFeesDto> fees;
    private List<LicenceBillingDto> bills;
    private List<TaskStatusHistoryDto> statusHistory;
    private ApplicantMinDto creator;
    private LicenceeEntityMinDto entity;
    private ShareholderChangeDto shareholderChange;
    private NameChangeDto changeOfName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
}
