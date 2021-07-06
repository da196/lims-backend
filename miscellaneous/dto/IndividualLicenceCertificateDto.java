/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.dto;

import lombok.Data;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class IndividualLicenceCertificateDto {
 
    private String applicantEntityName;
    private String productName;
    private String licenceNumber;
    private String postalAddress;
    private String duration;
    private String issueDate1;
    private String issueDate2; 
    private String custodianName;
    private String custodianTitle;
}
