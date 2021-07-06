/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.minio.service.MinioS3Service;
import tz.go.tcra.lims.miscellaneous.dto.DocumentDto;
import tz.go.tcra.lims.miscellaneous.dto.IndividualLicenceCertificateDto;
import tz.go.tcra.lims.miscellaneous.dto.PublicNoticeDto;
import tz.go.tcra.lims.miscellaneous.dto.MinistryConsultationLetterDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    private MinioS3Service minioService;
    
    @Value("${lims.public.notice.template}")
    private String noticeTemplate;
    
    @Value("${lims.ministry.transmittal.template}")
    private String ministryConsultationTemplate;
    
    @Value("${lims.licence.certificate.template}")
    private String licenceCertificateTemplate;
    
    @Value("${lims.staging.storage.location}")
    private String stagingLocation;
    
    @Value("${lims.public.notice.attachment.type}")
    private Long publicNoticeAttachmentType;
    
    @Value("${lims.consultation.letter.attachment.type}")
    private Long consultationLetterAttachmentType;
    
    @Value("${lims.licence.certificate.attachment.type}")
    private Long licenceCertificateAttachmentType;
    
    @Override
    public String generateDocument(DocumentDto data, Long attachmentType) throws FileNotFoundException,JRException,Exception {
        String response="";
        
        if(attachmentType == publicNoticeAttachmentType){
            
            response=this.generatePublicNotice(data.getPublicNotice());
          
        }else if(attachmentType == consultationLetterAttachmentType){
        
            response=this.generateLetterToMinistry(data.getLetter());
            
        }else if(attachmentType == licenceCertificateAttachmentType){
        
            response=this.generateLicenceCertificate(data.getCertificate());
        }
        
        return response;
    }

    @Override
    public String generatePublicNotice(PublicNoticeDto data) throws FileNotFoundException,JRException,Exception{
        String documentName="public_notice_"+System.currentTimeMillis()+".pdf";
        String path=stagingLocation+"//"+documentName;
        InputStream stream= new FileInputStream(new File(noticeTemplate));
        JasperDesign jasperDesign=JRXmlLoader.load(stream);
        JasperReport jsReport=JasperCompileManager.compileReport(jasperDesign);
        
        JRBeanCollectionDataSource dataSrc=new JRBeanCollectionDataSource(data.getApplicants());
        Map<String,Object> params=new HashMap<>();
        params.put("DataSource", dataSrc);
        JasperPrint jsPrint=JasperFillManager.fillReport(jsReport, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(jsPrint,path);
        
        File publicNotice=new File(path);
        
        minioService.uploadFileInternal(publicNotice);
        publicNotice.delete();
        
        return documentName;
    }

    @Override
    public String generateLetterToMinistry(MinistryConsultationLetterDto data) throws FileNotFoundException,JRException,Exception{
        String documentName="ministry_consultation_letter_"+System.currentTimeMillis()+".pdf";
  
        String path=stagingLocation+"//"+documentName;
        InputStream stream= new FileInputStream(new File(ministryConsultationTemplate));
        JasperDesign jasperDesign=JRXmlLoader.load(stream);
        JasperReport jsReport=JasperCompileManager.compileReport(jasperDesign);
        
        JRBeanCollectionDataSource dataSrc=new JRBeanCollectionDataSource(data.getApplicants());
        Map<String,Object> params=new HashMap<>();
        params.put("DataSource", dataSrc);
        params.put("MinistryName", data.getMinistryName());
        params.put("MawasilianoHabari", data.getMawasilianoHabari());
        params.put("DirectorGeneralName", data.getDirectorGeneralName());
        params.put("TodaysDate", data.getDate());
        params.put("DestinationAddress", data.getDestinationAddress());
        params.put("LicenceCategory", data.getApplicants().get(0).getLicenceCategory());
        params.put("ApplicantEntityName", data.getApplicants().get(0).getApplicantEntityName());
        
        JasperPrint jsPrint=JasperFillManager.fillReport(jsReport, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(jsPrint,path);
        
        File letter=new File(path);
        
        minioService.uploadFileInternal(letter);
        letter.delete();
        
        return documentName;
    }

    @Override
    public String generateLicenceCertificate(IndividualLicenceCertificateDto data) throws FileNotFoundException, JRException, Exception {
        String documentName="licence_certificate_"+System.currentTimeMillis()+".pdf";
  
        String path=stagingLocation+"//"+documentName;
        InputStream stream= new FileInputStream(new File(licenceCertificateTemplate));
        JasperDesign jasperDesign=JRXmlLoader.load(stream);
        JasperReport jsReport=JasperCompileManager.compileReport(jasperDesign);
        
        JRBeanCollectionDataSource dataSrc=new JRBeanCollectionDataSource(null);
        Map<String,Object> params=new HashMap<>();
        params.put("DataSource", dataSrc);
        params.put("LicenceNumber", data.getLicenceNumber());
        params.put("Date1", data.getIssueDate1());
        params.put("Date2", data.getIssueDate2());
        params.put("CustodianName", data.getCustodianName());
        params.put("CustodianTitle", data.getCustodianTitle());
        params.put("ProductName", data.getProductName());
        params.put("ApplicantEntityName", data.getApplicantEntityName());
        params.put("Duration", data.getDuration());
        params.put("PostalAddress", data.getPostalAddress());
        
        JasperPrint jsPrint=JasperFillManager.fillReport(jsReport, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(jsPrint,path);
        
        File certificate=new File(path);
        
        minioService.uploadFileInternal(certificate);
        certificate.delete();
        
        return documentName;
    }
    
}
