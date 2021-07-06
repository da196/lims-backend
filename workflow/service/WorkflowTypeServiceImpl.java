/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.workflow.entity.WorkflowType;
import tz.go.tcra.lims.workflow.repository.WorkflowTypeRepository;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class WorkflowTypeServiceImpl implements WorkflowTypeService{

    @Autowired
    private WorkflowTypeRepository workflowTypeRepo;
    
    @Override
    public Response<List<WorkflowType>> getAllWorkflowTypes() {
        Response<List<WorkflowType>> response=new Response<>(ResponseCode.SUCCESS,true,"WORKFLOW TYPES RETRIEVED SUCCESSFULLY",null);
        try{
        
            List<WorkflowType> data=workflowTypeRepo.findAll();
            
            if(data.isEmpty()){
            
                response.setMessage("WORKFLOW TYPES NOT FOUND");
                return response;
            }
            
            response.setData(data);
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    @Override
    public Response<List<WorkflowType>> getWorkflowTypesByActive(boolean active) {
        Response<List<WorkflowType>> response=new Response<>(ResponseCode.SUCCESS,true,"WORKFLOW TYPES RETRIEVED SUCCESSFULLY",null);
        try{
        
            List<WorkflowType> data=workflowTypeRepo.findByActive(active);
            
            if(data.isEmpty()){
            
                response.setMessage("WORKFLOW TYPES NOT FOUND");
                return response;
            }
            
            response.setData(data);
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

   
}
