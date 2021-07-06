/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import tz.go.tcra.lims.miscellaneous.dto.FormMaxDto;
import java.util.List;
import lombok.Data;
import tz.go.tcra.lims.attachments.dto.AttachmentTypeMinDto;
import tz.go.tcra.lims.licence.dto.LicenceMaxDto;
import tz.go.tcra.lims.licencee.dto.EntityApplicationMaxDto;
import tz.go.tcra.lims.miscellaneous.enums.TrackableTypeEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class TaskDetailDto {
    
    private Long id;
    private String taskName;
    private LicenceMaxDto licence;
    private EntityApplicationMaxDto entityApplication;
    private List<WorkflowDecisionEnum> decisions;
    private FormMaxDto form;
    private List<ActorMinDto> actors;
    private AttachmentTypeMinDto attachmentType;
    private TaskActivityDto activity;
    private TrackableTypeEnum trackableType;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;
}
