/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.attachments.services;

import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.entity.Attachment;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface AttachmentService {
    
    Response<CollectionModel<EntityModel<Attachment>>> listAll(int page,int size,String sortName,String sortType);
    Response<List<Attachment>> getAttachmentsByAttachable(Attachable attachable);
    void saveAttachments(List<AttachmentDto> attachments,Attachable attachable) throws DataNotFoundException,Exception;
    List<AttachmentDto> getAttachments(Attachable attachable);
    List<AttachmentMaxDto> getAttachmentsMax(Attachable attachable);
    AttachmentMaxDto getAttachmentMax(Attachable attachable);
    void saveAttachment(AttachmentDto attachment,Attachable attachable) throws DataNotFoundException,Exception;
    String getLicenceCertificateUri(Attachable attachable);
}
