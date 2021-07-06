/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import java.util.List;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@Setter
@Getter
public class ApplicantEntityDto {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String fax;
	private String website;
	private String physicalAddress;
	private String postalAddress;
	private String postalCode;
	private String country;
	private String region;
	private String district;
	private String ward;
	private String businessLicenceNo;
	private String regCertNo;
	private String tinNo;
	private long category;
	private String categoryName;
	private List<ShareholderDto> shareholders;
}
