/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.controller;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.miscellaneous.dto.ApplicantPublicNoticeDto;
import tz.go.tcra.lims.miscellaneous.dto.DocumentDto;
import tz.go.tcra.lims.miscellaneous.dto.MinistryConsultationLetterDto;
import tz.go.tcra.lims.miscellaneous.service.DocumentService;

/**
 *
 * @author emmanuel.mfikwa
 */
@RestController
//@RequestMapping("v1/jasper")
public class JasperController {
    
    @Autowired
    private DocumentService service;
    
//    @GetMapping(value = "/generate")
	// @PreAuthorize("hasRole('ROLE_LICENSE_CATEGORY_VIEW_ALL')")
    public String generatePublicNotice() throws FileNotFoundException, JRException,Exception {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
        LocalDateTime now = LocalDateTime.now();
        DocumentDto doc=new DocumentDto();
        MinistryConsultationLetterDto pb=new MinistryConsultationLetterDto();
        List<ApplicantPublicNoticeDto> applicants=new ArrayList();
        ApplicantPublicNoticeDto applicant=new ApplicantPublicNoticeDto();
        applicant.setApplicantEntityName("Kemix Technologies");
        applicant.setI(1);
        applicant.setLicenceServices("Radio");
        applicant.setLicenceCategory("Content Services");
        applicant.setShareholders("Mfikwa/Tanzanian");
        applicant.setShares("Sharesss");
        
        applicants.add(applicant);
        
        pb.setApplicants(applicants);
        pb.setDestinationAddress("Katibu Mkuu,\n" +
"Wizara ya Mawasiliano na Teknolojia ya Habari,\n" +
"Mji wa Serikali – Mtumba,\n" +
"Ujenzi Street,\n" +
"S.L.P 677\n" +
"404470 DODOMA.");
        pb.setDirectorGeneralName("Dkt. Jabiri K. Bakari");
        pb.setMawasilianoHabari("Mawasiliano");
        pb.setMinistryName("Mawasiliano na Teknolojia ya Habari");
        pb.setDate(dtf.format(now));        
        doc.setLetter(pb);
            return service.generateDocument(doc, 2L);

    }
}
