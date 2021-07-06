/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Time;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PresentationPortalDto {
    
    private Long licenceId;
    private String licenceApplicationNumber;
    private LicenseCategoryMinDto category;
    private LicenseCategoryMinDto subCategory;
    private LicenseCategoryMinDto coverage;
    private WorkflowDecisionEnum status;
    private String remark;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime presentationDate;
    
    private String uri;
}
