/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.notification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.licencee.entity.LicenceeNotification;
import tz.go.tcra.lims.licencee.repository.LicenceeNotificationRepository;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.miscellaneous.service.ListOfValueService;
import tz.go.tcra.lims.portal.notification.dto.NotificationDto;
import tz.go.tcra.lims.portal.notification.dto.NotificationMaxPortalDto;
import tz.go.tcra.lims.portal.notification.dto.NotificationMinPortalDto;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private LicenceeNotificationRepository licenceeNotificationRepo;
    
    @Autowired
    private ListOfValueRepository listOfValueRepo;
    
    @Autowired
    private ListOfValueService listOfValueService;
    
    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private AppUtility utility;
    
    @Override
    public Response<List<NotificationMinPortalDto>> listAll() {
        Response<List<NotificationMinPortalDto>> response=new Response(ResponseCode.SUCCESS,true,"NOTIFICATIONS RETRIEVED SUCCESSFULLY",null);
        try{
            LimsUser user=utility.getUser();
            
            List<LicenceeNotification> notifications=licenceeNotificationRepo.findByLicencee(user.getUserEntity());
            
            if(notifications.size() == 0){
            
                response.setMessage("NOTIFICATIONS NOT FOUND");
                return response;
            }
            
            List<NotificationMinPortalDto> data=new ArrayList();
            for(LicenceeNotification notification : notifications){
            
                NotificationMinPortalDto dt=new NotificationMinPortalDto();
                dt.setId(notification.getId());
                dt.setMessage(notification.getMessage());
                dt.setCreatedAt(notification.getCreatedAt());
                dt.setCategory(listOfValueService.composeListOfValueMinDto(notification.getNotificationCategory()));
                dt.setAttachments(attachmentService.getAttachmentsMax(notification));
                
                data.add(dt);
            }
            
            response.setData(data);
            
        }catch(Exception e){
        
            response.setMessage("FAILURE TO RETRIEVE DATA");
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public Response<SaveResponseDto> saveNotification(NotificationDto data) {
        Response<SaveResponseDto> response=new Response(ResponseCode.SUCCESS,true,"NOTIFICATION SAVED SUCCESSFULLY",new SaveResponseDto(HttpStatus.CREATED.value(),
						HttpStatus.CREATED.toString(), System.currentTimeMillis()));
        try{
            LimsUser user=utility.getUser();
            LicenceeNotification notification=new LicenceeNotification();
                        
            Optional<ListOfValue> category=listOfValueRepo.findById(data.getCategoryId());
            
            if(!category.isPresent()){
            
                throw new DataNotFoundException("CATEGORY NOT FOUND");
            }
            
            notification.setMessage(data.getMessage());
            notification.setNotificationCategory(category.get());
            notification.setLicencee(user.getUserEntity());
            notification=licenceeNotificationRepo.save(notification);
            
            attachmentService.saveAttachments(data.getAttachments(), notification);
            
        }catch(Exception e){
        
            response.setMessage("FAILURE TO RETRIEVE DATA");
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public Response<NotificationMaxPortalDto> findById(Long id) {
        Response<NotificationMaxPortalDto> response=new Response(ResponseCode.SUCCESS,true,"NOTIFICATION RETRIEVED SUCCESSFULLY",null);
        try{
            LimsUser user=utility.getUser();
            Optional<LicenceeNotification> notificationExistance=licenceeNotificationRepo.findByIdAndLicencee(id,user.getUserEntity());
            
            if(!notificationExistance.isPresent()){
            
                response.setMessage("NOTIFICATION NOT FOUND");
                return response;
            }
            
            LicenceeNotification notification=notificationExistance.get();
            
            NotificationMaxPortalDto data=new NotificationMaxPortalDto();
            data.setId(notification.getId());
            data.setMessage(notification.getMessage());
            data.setCreatedAt(notification.getCreatedAt());
            data.setCategory(listOfValueService.composeListOfValueMinDto(notification.getNotificationCategory()));
            data.setAttachments(attachmentService.getAttachmentsMax(notification));
            
            response.setData(data);
            
        }catch(Exception e){
        
            response.setMessage("FAILURE TO RETRIEVE DATA");
            e.printStackTrace();
        }
        
        return response;
    }
    
}
