/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.uaa.dto.PermissionMinDto;
import tz.go.tcra.lims.uaa.entity.Permission;
import tz.go.tcra.lims.uaa.repository.PermissionRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.GeneralException;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    
    @Autowired
    private PagedResourcesAssembler<Permission> pagedResourcesAssembler;
    
    @Autowired
    private PermissionRepository permissionRepo;
        
    @Autowired
    private PermissionServiceModelAssembler assembler;
        
    @Override
    public Response<CollectionModel<EntityModel<Permission>>> listAll(int page,int size,String sortName,String sortType) {
        Response response=new Response<>(ResponseCode.SUCCESS,true,"LIST ALL PERMISSIONS ",null);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortName).descending());
            if(sortType.toUpperCase() == "ASC"){
                
                pageable = PageRequest.of(page, size, Sort.by(sortName).ascending());
            }
            
            Page<Permission> pagedData=permissionRepo.findAll(pageable);
            
            if(pagedData.hasContent()){
                
                response.setData(pagedResourcesAssembler.toModel(pagedData));
                response.setMessage("LIST OF PERMISSIONS [ PAGE : "+page+", SIZE : "+size+", SORT NAME : "+sortName+", SORT TYPE : "+sortType);
            }

        } catch (Exception e) {

            e.printStackTrace();
            response=new Response<>(ResponseCode.FAILURE,false,"FAILURE TO RETRIEVE DATA ",null);
        }
        
        return response;
    }

    @Override
    public Response<EntityModel<Permission>> details(Long id) {
        try {

            return new Response<>(ResponseCode.SUCCESS,true,"PERMISSION DETAILS",assembler.toModel(permissionRepo.getOne(id)));
        
        } catch (EntityNotFoundException e) {

            e.printStackTrace();
            
        }catch (Exception e) {

            e.printStackTrace();
        }
        
        return new Response<>(ResponseCode.SUCCESS,true,"PERMISSION DETAILS",null);
    }

    @Override
    public Response<CollectionModel<EntityModel<Permission>>> getPermissionByGroupName(String groupName) {
        try {
            List<Permission> permissions=permissionRepo.findPermissionByGroupName(groupName);
            
            if(permissions.size() > 0){
                
                return new Response<>(ResponseCode.SUCCESS,true,"PERMISSION DETAILS",assembler.toCollectionModel(permissions));
            }
            
        } catch (Exception e) {

            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return new Response<>(ResponseCode.SUCCESS,true,"PERMISSION DETAILS",null);
    }

    @Override
    public PermissionMinDto getPermissionMinDto(Long id) {
        PermissionMinDto response=null;
        try{
        
            Permission data=permissionRepo.getOne(id);
            response=new PermissionMinDto();
            response.setId(data.getId());
            response.setName(data.getName());
            response.setDisplayName(data.getDisplayName());
            response.setGroupName(data.getGroupName());
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }
}
