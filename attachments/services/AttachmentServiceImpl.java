/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.attachments.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.attachments.repository.AttachmentRepository;
import tz.go.tcra.lims.attachments.repository.AttachmentTypeRepository;
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.entity.Attachment;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.minio.service.MinioS3Service;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j

public class AttachmentServiceImpl implements AttachmentService {

	@Value("${minio.buckek.name}")
	private String bucketName;

	@Value("${minio.buckek.expire}")
	private int expire;

        @Value("${lims.licence.certificate.attachment.type}")
        private Long licenceCertificateAttachmentType;
        
	@Autowired
	private AttachmentRepository attachmentRepository;

	@Autowired
	private AttachmentTypeRepository attachmentTypeRepo;

	@Autowired
	private PagedResourcesAssembler<Attachment> pagedResourcesAssembler;

	@Autowired
	private MinioS3Service s3Service;

	@Override
	public Response<CollectionModel<EntityModel<Attachment>>> listAll(int page, int size, String sortName,
			String sortType) {
		Response response = new Response<>(ResponseCode.SUCCESS, true, "LIST ALL ATTACHMENTS ", null);
		try {

			Pageable pageable = PageRequest.of(page, size, Sort.by(sortName).descending());
			if (sortType.toUpperCase() == "ASC") {

				pageable = PageRequest.of(page, size, Sort.by(sortName).ascending());
			}

			Page<Attachment> pagedData = attachmentRepository.findAll(pageable);

			if (pagedData.hasContent()) {

				response.setData(pagedResourcesAssembler.toModel(pagedData));
				response.setMessage("LIST OF ATTACHMENTS [ PAGE : " + page + ", SIZE : " + size + ", SORT NAME : "
						+ sortName + ", SORT TYPE : " + sortType);

			}

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response = new Response<>(ResponseCode.FAILURE, false, "FAILURE TO RETRIEVE DATA ", null);
		}

		return response;
	}

	@Override
	public Response<List<Attachment>> getAttachmentsByAttachable(Attachable attachable) {
		Response response = new Response<>(ResponseCode.SUCCESS, true, "LIST ATTACHABLE ATTACHMENTS ", null);
		try {
                    
                    Optional<AttachmentType> attachmentType=attachmentTypeRepo.findById(licenceCertificateAttachmentType);
            
                    if(!attachmentType.isPresent()){

                        return response;
                    }

                    List<Attachment> data = attachmentRepository.findByAttachableIdAndAttachableTypeAndAttachmentTypeNotAndStatus(
                            attachable.getId(),
                            attachable.getAttachableType(),
                            attachmentType.get(),
                            true);

			
                    if (data.size() > 0) {

                            response.setData(data);
                    }

		} catch (Exception e) {

			e.printStackTrace();
			response = new Response<>(ResponseCode.FAILURE, false, "FAILURE TO RETRIEVE DATA ", null);
		}

		return response;
	}

	@Override
        @Transactional
	public void saveAttachments(List<AttachmentDto> attachments, Attachable attachable) throws DataNotFoundException,Exception {
            
            for (AttachmentDto attachment : attachments) {
			
                    Optional<AttachmentType> existingType = attachmentTypeRepo.findById(attachment.getAttachmentType());
                    if(!existingType.isPresent()){

                        throw new DataNotFoundException("Attachment Type Not Found");
                    }

                    AttachmentType type=existingType.get();
                    
                    Attachment attach = new Attachment();
                    
                    Optional<Attachment> existance=attachmentRepository.findFirstByAttachableIdAndAttachableTypeAndAttachmentType(attachable.getId(), attachable.getAttachableType(), type);
                    if(existance.isPresent()){
                    
                        attach=existance.get();
                    }
                    
                    attach.setAttachmentName(attachment.getName());
                    attach.setAttachable(attachable);
                    attach.setAttachmentType(type);
                    if (attachment.getUri() == null && attachment.getFiles() != null) {
                            // s3Service.uploadFile(bucketName, attachment.getFiles().getOriginalFilename(),
                            // attachment.getFiles());
                            attach.setUri(s3Service.uploadFile(bucketName, attachment.getFiles().getOriginalFilename(),
                                            attachment.getFiles()));
                    }
                    if (attachment.getUri() != null) {
                        
                        attach.setUri(attachment.getUri());
                    }
                    
                    attachmentRepository.saveAndFlush(attach);
            }
	}

	@Override
	public List<AttachmentDto> getAttachments(Attachable attachable) {
		List<AttachmentDto> response = new ArrayList();
		try {
                    Optional<AttachmentType> attachmentType=attachmentTypeRepo.findById(licenceCertificateAttachmentType);
            
                    if(!attachmentType.isPresent()){

                        return response;
                    }

                    List<Attachment> attachments = attachmentRepository.findByAttachableIdAndAttachableTypeAndAttachmentTypeNotAndStatus(
                            attachable.getId(),
                            attachable.getAttachableType(),
                            attachmentType.get(),
                            true);
                    for (Attachment attachment : attachments) {

                            AttachmentDto data = new AttachmentDto();
                            data.setName(attachment.getAttachmentName());
                            data.setUri(attachment.getUri());
                            data.setAttachmentType(attachment.getAttachmentType().getId());

                            response.add(data);
                    }

		} catch (Exception e) {

			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}

		return response;
	}

	@Override
	public List<AttachmentMaxDto> getAttachmentsMax(Attachable attachable) {
		List<AttachmentMaxDto> response = new ArrayList();
		try {
                        
                        Optional<AttachmentType> attachmentType=attachmentTypeRepo.findById(licenceCertificateAttachmentType);
            
                        if(!attachmentType.isPresent()){

                            return response;
                        }
                        
			List<Attachment> attachments = attachmentRepository.findByAttachableIdAndAttachableTypeAndAttachmentTypeNotAndStatus(
                                attachable.getId(),
                                attachable.getAttachableType(),
                                attachmentType.get(),
                                true);
			for (Attachment attachment : attachments) {
				AttachmentMaxDto data = new AttachmentMaxDto();
				data.setName(attachment.getAttachmentName());
				data.setUri(s3Service.getFileToDownload(bucketName, attachment.getUri(), expire));
				data.setAttachmentTypeId(attachment.getAttachmentType().getId());
				data.setAttachmentTypeName(attachment.getAttachmentType().getName());

				response.add(data);
			}

		} catch (Exception e) {

			e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}

		return response;
	}

    @Override
    @Transactional
    public void saveAttachment(AttachmentDto attachment, Attachable attachable) throws DataNotFoundException, Exception {
        Optional<AttachmentType> existingType = attachmentTypeRepo.findById(attachment.getAttachmentType());
        if(!existingType.isPresent()){

            throw new DataNotFoundException("Attachment Type Not Found");
        }

        AttachmentType type=existingType.get();
        // delete the existing attachment
        attachmentRepository.deleteByAttachableAndAttachmentType(attachable, type);

        Attachment attach = new Attachment();
        attach.setAttachmentName(attachment.getName());
        attach.setAttachable(attachable);
        attach.setAttachmentType(type);
        if (attachment.getUri() == null && attachment.getFiles() != null) {
                // s3Service.uploadFile(bucketName, attachment.getFiles().getOriginalFilename(),
                // attachment.getFiles());
                attach.setUri(s3Service.uploadFile(bucketName, attachment.getFiles().getOriginalFilename(),
                                attachment.getFiles()));
        }
        if (attachment.getUri() != null) {
                attach.setUri(attachment.getUri());
        }
        attachmentRepository.saveAndFlush(attach);
    }
    
    @Override
    public AttachmentMaxDto getAttachmentMax(Attachable attachable) {
        AttachmentMaxDto response = new AttachmentMaxDto();
        try {

                Optional<AttachmentType> attachmentType=attachmentTypeRepo.findById(licenceCertificateAttachmentType);
            
                if(!attachmentType.isPresent()){

                    return null;
                }

                List<Attachment> attachments = attachmentRepository.findByAttachableIdAndAttachableTypeAndAttachmentTypeNotAndStatus(
                        attachable.getId(),
                        attachable.getAttachableType(),
                        attachmentType.get(),
                        true);
                
                if(attachments.size() == 0){
                
                    return null;
                }
                
                response.setName(attachments.get(0).getAttachmentName());
                response.setUri(s3Service.getFileToDownload(bucketName, attachments.get(0).getUri(), expire));
                response.setAttachmentTypeId(attachments.get(0).getAttachmentType().getId());
                response.setAttachmentTypeName(attachments.get(0).getAttachmentType().getName());

        } catch (Exception e) {

                e.printStackTrace();
                log.error(e.getLocalizedMessage());
        }

        return response;
    }

    @Override
    public String getLicenceCertificateUri(Attachable attachable) {
        String response = null;
        try {
            
            Optional<AttachmentType> attachmentType=attachmentTypeRepo.findById(licenceCertificateAttachmentType);
            
            if(!attachmentType.isPresent()){
            
                return null;
            }
            Optional<Attachment> attachment=attachmentRepository.findFirstByAttachableIdAndAttachableTypeAndAttachmentTypeAndStatus(attachable.getId(),attachable.getAttachableType(), attachmentType.get(), true);
            
            if(!attachment.isPresent()){

                return null;
            }

            response=s3Service.getFileToDownload(bucketName, attachment.get().getUri(), expire);

        } catch (Exception e) {

                e.printStackTrace();
                log.error(e.getLocalizedMessage());
        }

        return response;
    }
}
