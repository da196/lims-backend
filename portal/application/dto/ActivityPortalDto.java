/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ActivityPortalDto {
    
    private String activityName;
    private String comments;
    private WorkflowDecisionEnum decision;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")    
    private LocalDateTime createdAt;
}
