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
public class DocumentDto {
    
    private PublicNoticeDto publicNotice;
    private MinistryConsultationLetterDto letter;
    private IndividualLicenceCertificateDto certificate;
}
