/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.miscellaneous.enums.NumberQueTypeEnum;
import tz.go.tcra.lims.utils.entity.NumberQue;
import tz.go.tcra.lims.utils.repository.NumberQueRepository;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import tz.go.tcra.lims.attachments.repository.AttachmentRepository;
import tz.go.tcra.lims.attachments.repository.AttachmentTypeRepository;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.entity.Attachment;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.licence.repository.LicenceRepository;
import tz.go.tcra.lims.miscellaneous.dto.DocumentDto;
import tz.go.tcra.lims.miscellaneous.service.DocumentService;
import tz.go.tcra.lims.product.repository.LicenceProductRepository;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.repository.TaskTrackRepository;
import tz.go.tcra.lims.task.service.TaskService;

/**
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class NumberQueServiceImpl implements NumberQueService {

    @Autowired
    private NumberQueRepository numberQueRepo;

    @Autowired
    private LicenceRepository licenceRepo;

    @Autowired
    private TaskTrackRepository trackRepo;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private DocumentService documentService;
    
    @Autowired
    private LicenceProductRepository licenceProductRepo;

    @Value("${lims.licence.certificate.attachment.type}")
    private Long licenceCertificateAttachmentType;
    
    @Override
    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void runNumberQue() {
        List<NumberQue> que = numberQueRepo.findTop10ByOrderByCreatedAtDesc();
        String number = null;
        for (NumberQue q : que) {
            try {
                Licence licence = q.getLicence();
                LicenceProduct product = licence.getLicenseProduct();

                if (q.getType() == NumberQueTypeEnum.APPLICATION) {

                    number = this.generateApplicationNumber(licence);

                    licence.setApplicationNumber(number.trim());
                    product.setApplicationCount(product.getApplicationCount() + 1);
                    licenceRepo.save(licence);
                    licenceProductRepo.save(product);
                    numberQueRepo.delete(q);
                    
                } else if (q.getType() == NumberQueTypeEnum.LICENCE) {

                    number = this.generateLicenceNumber(licence);

                    licence.setLicenceNumber(number.trim());
                    product.setLicenceCount(product.getLicenceCount() + 1);
                    licence=licenceRepo.save(licence);
                    licenceProductRepo.save(product);
                    numberQueRepo.delete(q);
                    
                    List<TaskTrack> tracks=trackRepo.findByTrackableOrderByCreatedAtDesc(licence);
                    
                    if(tracks.size() > 0){
                        TaskTrack track=tracks.get(0);
                        if(track.getIsActed()){
                            
                            DocumentDto document=taskService.composeDocumentDto(tracks.get(0),licenceCertificateAttachmentType);
                            
                            if(document != null){
            
                                //generate certificate
                                String uri=documentService.generateDocument(document, licenceCertificateAttachmentType);
                                
                                AttachmentDto attachment=new AttachmentDto();
                                attachment.setAttachmentType(licenceCertificateAttachmentType);
                                attachment.setUri(uri);
                                attachment.setName(track.getTrackName());
                                attachmentService.saveAttachment(attachment, licence);
                            }
                        }
                    }
                    
                }

            } catch (Exception e) {

                log.error("Error executing number que [" + e.getLocalizedMessage() + "]");
                e.printStackTrace();
            }
        }
    }

    @Override
    public String generateApplicationNumber(Licence licence) throws Exception {
        LicenceProduct product = licence.getLicenseProduct();
        int appCount = product.getApplicationCount() + 1;
        Calendar cal = Calendar.getInstance();

        return "TCRA/APP/" + product.getCode() + "/" + appCount + "/" + cal.get(Calendar.YEAR);
    }

    @Override
    public String generateLicenceNumber(Licence licence) throws Exception {
        LicenceProduct product = licence.getLicenseProduct();
        int appCount = product.getLicenceCount() + 1;
        Calendar cal = Calendar.getInstance();

        return "TCRA/" + product.getCode() + "/" + appCount + "/" + cal.get(Calendar.YEAR);
    }

    @Override
    public void queLicence(Licence licence, NumberQueTypeEnum type) {
        try {

            NumberQue que = new NumberQue();
            que.setLicence(licence);
            que.setType(type);
            numberQueRepo.save(que);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
