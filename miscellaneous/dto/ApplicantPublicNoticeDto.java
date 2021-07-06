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
public class ApplicantPublicNoticeDto {
    
    private Integer i;
    private String applicantEntityName;
    private String licenceCategory;
    private String licenceServices;
    private String shareholders;
    private String shares;
}
