/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.geolocation.dto.GeoLocationMinDto;
import tz.go.tcra.lims.licence.dto.LicenceBillingDto;
import tz.go.tcra.lims.task.dto.TaskStatusHistoryDto;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto;
import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;
import tz.go.tcra.lims.licence.dto.SpectrumValueDto;
import tz.go.tcra.lims.uaa.dto.UserMaxDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LicencePortalMaxDto {
    
    private Long id;
    private Boolean isDraft;
    private String applicationNumber;
    private String licenceNumber;
    private String product;
    private String licenseState;
    private String applicationState;
    private String comments;
    private String operationalStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;
    
    private String licenceCertificateUri;
    private LicenseCategoryMinDto category;
    private LicenseCategoryMinDto subCategory;
    private LicenseCategoryMinDto subCategoryLeaf;
    private LicenseCategoryMinDto coverage;
    private List<GeoLocationMinDto> coverageAreas;
    private List<LicenseDetailMaxDto> services;
    private List<PayableFeesDto> fees;
    private List<LicenceBillingDto> bills;
    private String status;
    private List<TaskStatusHistoryDto> statusHistory;
    private List<AttachmentMaxDto> attachments;
    private UserMaxDto creator;
    private ApplicantEntityDto entity;
    private IndividualLicenceDto individual; 
    private List<SpectrumValueDto> spectrumValues;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
