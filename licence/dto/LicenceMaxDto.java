/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.dto;

import tz.go.tcra.lims.task.dto.TaskStatusHistoryDto;
import tz.go.tcra.lims.task.dto.TrackDto;
import tz.go.tcra.lims.task.dto.TaskActivityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.geolocation.dto.GeoLocationMinDto;
import tz.go.tcra.lims.portal.application.dto.ApplicantEntityDto;
import tz.go.tcra.lims.portal.application.dto.IndividualLicenceDto;
import tz.go.tcra.lims.portal.application.dto.PayableFeesDto;
import tz.go.tcra.lims.uaa.dto.UserMaxDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Getter
@Setter
@NoArgsConstructor
public class LicenceMaxDto {
    
    private Long id;
    private Boolean isDraft;
    private String product;
    private String applicationNumber;
    private String licenceNumber;
    private String applicationState;
    private String licenseState;
    
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
    private List<LicenceBillingDto> bills;
    private List<PayableFeesDto> fees;
    private String status;
    private List<AttachmentMaxDto> attachments;
    private List<TaskActivityDto> activities;
    private List<TrackDto> track;
    private List<TaskStatusHistoryDto> statusHistory;
    private UserMaxDto creator;
    private ApplicantEntityDto entity;
    private IndividualLicenceDto individual; 
    private List<SpectrumValueDto> spectrumValues;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
