/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.licencee.dto.LicenceeEntityMaxDto;
import tz.go.tcra.lims.licencee.dto.LicenceeEntityMinDto;
import tz.go.tcra.lims.licencee.dto.LicenceeNotificationMaxDto;
import tz.go.tcra.lims.licencee.dto.LicenceeNotificationMinDto;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.licencee.entity.LicenceeEntityShareholder;
import tz.go.tcra.lims.licencee.entity.LicenceeNotification;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityShareholderRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeNotificationRepository;
import tz.go.tcra.lims.miscellaneous.service.ListOfValueService;
import tz.go.tcra.lims.uaa.dto.ProfileMaxDto;
import tz.go.tcra.lims.uaa.service.UserService;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class LicenceeEntityServiceImpl implements LicenceeEntityService{

    @Autowired
    private LicenceeEntityRepository licenceeEntityRepo;
    
    @Autowired
    private PagedResourcesAssembler<LicenceeEntityMinDto> pagedResourcesAssembler;
        
    @Autowired
    private PagedResourcesAssembler<LicenceeNotificationMinDto> pagedNotificationResourcesAssembler;
    
    @Autowired
    private LicenceeEntityShareholderRepository licenceeEntityShareholderRepo;
    
    @Autowired
    private LicenceeNotificationRepository licenceeNotificationRepo;
        
    @Autowired
    private ListOfValueService listOfValueService;
    
    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private UserService userService;
    
    @Override
    public Response<CollectionModel<EntityModel<LicenceeEntityMinDto>>> listAllEntities(String keyword, Pageable pageable) {
        Response<CollectionModel<EntityModel<LicenceeEntityMinDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITIES  RETRIEVED SUCCESSFULLY", null);
        try {
            Page<LicenceeEntityMinDto> data = null;

            if (keyword.equalsIgnoreCase("All")) {
                
                data = licenceeEntityRepo.findAllEntities(pageable);

            } else {

                data = licenceeEntityRepo.findAllSearchByKeyword(keyword, pageable);
            }
            
            if (data != null) {

                response.setData(pagedResourcesAssembler.toModel(data));
            }

        } catch (Exception e) {
            
            log.error(e.getMessage());
            e.printStackTrace();
            response.setMessage("FAILURE OCCURED");
        }

        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<LicenceeEntityMinDto>>> listActiveEntities(String keyword, Pageable pageable) {
        Response<CollectionModel<EntityModel<LicenceeEntityMinDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITIES  RETRIEVED SUCCESSFULLY", null);
        try {
            Page<LicenceeEntityMinDto> data = null;

            if (keyword.equalsIgnoreCase("All")) {
                
                data = licenceeEntityRepo.findAllEntitiesByStatus(true,pageable);

            } else {

                data = licenceeEntityRepo.findAllSearchByKeywordAndStatus(keyword,true, pageable);
            }
            
            if (data == null) {

                response.setMessage("ENTITIES NOT FOUND");
                return response;
            }

            response.setData(pagedResourcesAssembler.toModel(data));
        } catch (Exception e) {
            
            log.error(e.getMessage());
            e.printStackTrace();
            response.setMessage("FAILURE OCCURED");
        }

        return response;
    }

    @Override
    public Response<ProfileMaxDto> findProfileByEntityId(Long id) {
        Response<ProfileMaxDto> response=new Response<>(ResponseCode.SUCCESS,true,"ENTITY PROFILE RETRIEVED SUCCESSFULLY",null);
        try{
        
            Optional<LicenceeEntity> existance=licenceeEntityRepo.findById(id);
            
            if(!existance.isPresent()){
            
                response.setMessage("ENTITY NOT FOUND");
                return response;
            }
            
            LicenceeEntity entity=existance.get();
            ProfileMaxDto data=new ProfileMaxDto();
            
            data.setEntity(this.composeLicenceeEntityMaxDto(entity));            
            data.setPersonal(userService.composeUserDto(entity.getUser()));
            
            response.setData(data);
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public LicenceeEntityMinDto composeLicenceeEntityMinDto(LicenceeEntity data) {
        LicenceeEntityMinDto response=new LicenceeEntityMinDto();
        response.setName(data.getName());
        response.setEmail(data.getEmail());
        response.setPhone(data.getPhone());
        response.setFax(data.getFax());
        response.setWebsite(data.getWebsite());
        response.setPhysicalAddress(data.getPhysicalAddress());
        response.setPostalAddress(data.getPostalAddress());
        response.setPostalCode(data.getPostalCode());       
        response.setCategoryName(data.getCategory().getName());
        
        return response;
    }

    @Override
    public LicenceeEntityMaxDto composeLicenceeEntityMaxDto(LicenceeEntity data) {
        
        LicenceeEntityMaxDto response=new LicenceeEntityMaxDto();
        response.setName(data.getName());
        response.setEmail(data.getEmail());
        response.setPhone(data.getPhone());
        response.setFax(data.getFax());
        response.setWebsite(data.getWebsite());
        response.setPhysicalAddress(data.getPhysicalAddress());
        response.setPostalAddress(data.getPostalAddress());
        response.setPostalCode(data.getPostalCode());
        response.setCountry(data.getCountryID());
        response.setRegion(data.getRegionID());
        response.setDistrict(data.getDistrictID());
        response.setWard(data.getWardID());
        response.setBusinessLicenceNo(data.getBusinessLicenceNo());
        response.setTinNo(data.getTinNo());
        response.setRegCertNo(data.getRegCertNo());
        response.setCategory(data.getCategory().getId());
        response.setCategoryName(data.getCategory().getName());
        response.setShareholders(this.findLicenceeEntityShareholdersByEntityId(data.getId()));
        response.setAttachments(attachmentService.getAttachmentsMax(data));
        response.setOperationalStatus(data.getOperationalStatus());
        
        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<LicenceeNotificationMinDto>>> listAllNotification(String keyword, Pageable pageable) {
        Response<CollectionModel<EntityModel<LicenceeNotificationMinDto>>> response=new Response(ResponseCode.SUCCESS,true,"NOTIFICATIONS RETRIEVED SUCCESSFULLY",null);
        try{
            
            Page<LicenceeNotificationMinDto> notifications=null;
            if(keyword.trim().isEmpty()){
            
                notifications=licenceeNotificationRepo.findAllLicenceeNotifications(pageable);
            }else{
            
                notifications=licenceeNotificationRepo.findLicenceeNotificationsByKeyword(keyword,pageable);
            }
            
            
            if(!notifications.hasContent()){
            
                response.setMessage("NOTIFICATIONS NOT FOUND");
                return response;
            }
            
            response.setData(pagedNotificationResourcesAssembler.toModel(notifications));
            
        }catch(Exception e){
        
            response.setMessage("FAILURE TO RETRIEVE DATA");
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public Response<LicenceeNotificationMaxDto> findNotificationById(Long id) {
       Response<LicenceeNotificationMaxDto> response=new Response(ResponseCode.SUCCESS,true,"NOTIFICATION RETRIEVED SUCCESSFULLY",null);
        try{
            
            Optional<LicenceeNotification> notificationExistance=licenceeNotificationRepo.findById(id);
            
            if(!notificationExistance.isPresent()){
            
                response.setMessage("NOTIFICATION NOT FOUND");
                return response;
            }
            
            LicenceeNotification notification=notificationExistance.get();
            
            LicenceeNotificationMaxDto data=new LicenceeNotificationMaxDto();
            data.setEntityName(notification.getLicencee().getName());
            data.setMessage(notification.getMessage());
            data.setCreatedAt(notification.getCreatedAt());
            data.setCategory(listOfValueService.composeListOfValueMinDto(notification.getNotificationCategory()));
            data.setAttachments(attachmentService.getAttachments(notification));
            
            response.setData(data);
            
        }catch(Exception e){
        
            response.setMessage("FAILURE TO RETRIEVE DATA");
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public List<ShareholderDto> findLicenceeEntityShareholdersByEntityId(Long id) {
        List<ShareholderDto> response=new ArrayList();
        try{        
            List<LicenceeEntityShareholder> shareholders=licenceeEntityShareholderRepo.findCustomByLicenceeEntity(id);
            for(LicenceeEntityShareholder shareholder: shareholders){
            
                ShareholderDto sh=new ShareholderDto();
                sh.setFullname(shareholder.getFullname());
                sh.setNationality(shareholder.getNationality());
                sh.setShares(shareholder.getShares());
                sh.setAttachments(attachmentService.getAttachments(shareholder));
                
                response.add(sh);
            }
            
        }catch(Exception e){
        
            e.printStackTrace();
        }
        
        return response;
    }
    
}
