/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.service;

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
import tz.go.tcra.lims.feestructure.service.FeeStructureService;
import tz.go.tcra.lims.licencee.dto.EntityApplicationMaxDto;
import tz.go.tcra.lims.licencee.dto.EntityApplicationMinDto;
import tz.go.tcra.lims.licencee.entity.EntityApplication;
import tz.go.tcra.lims.licencee.repository.EntityApplicationRepository;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.payment.service.BillingService;
import tz.go.tcra.lims.task.service.TaskService;
import tz.go.tcra.lims.task.service.TaskStatusHistroyService;
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class EntityApplicationServiceImpl implements EntityApplicationService{

    @Autowired
    private EntityApplicationRepository entityApplicationRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private PagedResourcesAssembler<EntityApplicationMinDto> pagedResourcesAssembler;
        
    @Autowired
    private LicenceeEntityService licenceeEntityService;
    
    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private TaskStatusHistroyService taskStatusHistoryService;
    
    @Autowired
    private BillingService billingService;
    
    @Autowired
    private FeeStructureService feeStructureService;
    
    @Override
    public Response<EntityApplicationMaxDto> findEntityApplicationById(Long id) {
        Response<EntityApplicationMaxDto> response=new Response<>(ResponseCode.SUCCESS,true,"ENTITY APPLICATION RETRIEVED SUCCESSFULLY",null);
        try{
        
            Optional<EntityApplication> existance=entityApplicationRepo.findById(id);
            if(!existance.isPresent()){
            
                response.setMessage("ENTITY APPLICATION NOT FOUND");
                return response;
            }
            
            EntityApplication entityApplication=existance.get();
            EntityApplicationMaxDto data=new EntityApplicationMaxDto();
            data.setActivities(taskService.getTaskActivities(entityApplication));
            data.setTracks(taskService.getTaskTracks(entityApplication));
            data.setStatus(entityApplication.getStatus().getName());
            data.setApprovedAt(entityApplication.getApprovedAt());
            data.setCreatedAt(entityApplication.getCreatedAt());
            data.setIsDraft(entityApplication.getIsDraft());
            data.setCurrentDecision(entityApplication.getDecision());
            data.setCreator(userRepo.findApplicantMinDtoById(entityApplication.getApplicant()));
            data.setAttachments(attachmentService.getAttachmentsMax(entityApplication));
            data.setProduct(entityApplication.getEntityProduct().getDisplayName());
            data.setEntity(licenceeEntityService.composeLicenceeEntityMinDto(entityApplication.getApplicantEntity()));
            data.setStatusHistory(taskStatusHistoryService.getStatusHistoryByTrackable(entityApplication));
            data.setBills(billingService.getLicenseBillingByLicenseid(entityApplication.getId(),BillingAttachedToEnum.ENTITY_APPLICATION));
            data.setFees(feeStructureService.findFeeStructureByFeeable(entityApplication.getEntityProduct()));
            
            response.setData(data);
            
        }catch(Exception e){
        
            log.error(e.getMessage());
            e.printStackTrace();
            response.setMessage("ENTITY APPLICATION NOT FOUND");
        }
        
        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<EntityApplicationMinDto>>> getListOfAllEntityApplications(String keyword,
			Pageable pageable) {
        Response<CollectionModel<EntityModel<EntityApplicationMinDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ENTITY APPLICATIONS  RETRIEVED SUCCESSFULLY", null);
        try {
            Page<EntityApplicationMinDto> data = null;

            if (keyword.equalsIgnoreCase("All")) {
                
                data = entityApplicationRepo.findAllApplications(pageable);

            } else {

                data = entityApplicationRepo.findAllApplicationsSearchByKeyword(keyword, pageable);
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
    
}
