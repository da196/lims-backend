/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenceeEntityMaxDto {
    
    private String name;
    private String phone;
    private String email;
    private String fax;
    private String website;
    private String physicalAddress;
    private String postalAddress;
    private String postalCode;
    private long country;
    private long region;
    private long district;
    private long ward;
    private String businessLicenceNo;
    private String regCertNo;
    private String tinNo;
    private long category;
    private String categoryName;
    private String operationalStatus;
    private List<AttachmentMaxDto> attachments;
    private List<ShareholderDto> shareholders;
}
