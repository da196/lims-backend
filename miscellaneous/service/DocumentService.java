/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import java.io.FileNotFoundException;
import net.sf.jasperreports.engine.JRException;
import tz.go.tcra.lims.miscellaneous.dto.DocumentDto;
import tz.go.tcra.lims.miscellaneous.dto.IndividualLicenceCertificateDto;
import tz.go.tcra.lims.miscellaneous.dto.PublicNoticeDto;
import tz.go.tcra.lims.miscellaneous.dto.MinistryConsultationLetterDto;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface DocumentService {
    
    String generateDocument(DocumentDto data,Long attachmentType) throws FileNotFoundException,JRException,Exception;
    String generatePublicNotice(PublicNoticeDto data) throws FileNotFoundException,JRException,Exception;
    String generateLetterToMinistry(MinistryConsultationLetterDto data) throws FileNotFoundException,JRException,Exception;
    String generateLicenceCertificate(IndividualLicenceCertificateDto data) throws FileNotFoundException,JRException,Exception;
}
