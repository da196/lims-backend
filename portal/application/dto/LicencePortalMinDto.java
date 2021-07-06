/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto;
import tz.go.tcra.lims.payment.dto.BillingChargesDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Getter
@Setter
@NoArgsConstructor
public class LicencePortalMinDto {

	private Long id;
	private Boolean isDraft;
	private String product;
	private String licenseState;
	private String applicationState;
        private String comments;
        private String operationalStatus;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
	private Date issueDate;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
	private Date expireDate;
        
	private LicenseCategoryMinDto category;
	private LicenseCategoryMinDto subCategory;
	private LicenseCategoryMinDto coverage;
	private String status;
	private String billId;
	private int gepgFlag;
	private String controlNumber;
	private Double amount;

	private String currencyName;
	private Long currencyId;
	private String billStatus;
	private List<BillingChargesDto> billingCharges;
	private ApplicantEntityDto entity;
	private String invoicedate;
	private String controlNoExpireDate;
	private String amountInWords;
	private String licenseNumber;
	private String applicationRefNumber;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime submittedAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
}
