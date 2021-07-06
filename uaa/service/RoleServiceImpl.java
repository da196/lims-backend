/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.uaa.dto.*;
import tz.go.tcra.lims.uaa.entity.Role;
import tz.go.tcra.lims.uaa.entity.RolePermission;
import tz.go.tcra.lims.uaa.repository.PermissionRepository;
import tz.go.tcra.lims.uaa.repository.RolePermissionRepository;
import tz.go.tcra.lims.uaa.repository.RoleRepository;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleService.class.getName());

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RolePermissionRepository rolePermissionRepo;

	@Autowired
	private PermissionRepository permissionRepo;

	@Autowired
	private RoleServiceModelAssembler assembler;

        @Autowired
	private RoleServiceModelAssembler2 assembler2;
        
        @Autowired
	private PermissionService permissionService;
        
	@Override
	public Response<CollectionModel<EntityModel<Role>>> listAll() {
            try {

                return new Response<>(ResponseCode.SUCCESS,true,"ROLE DETAILS",assembler.toCollectionModel(roleRepository.findAll()));
            } catch (Exception e) {

                e.printStackTrace();
                throw new GeneralException("INTERNAL SERVER ERROR");
            }
	}
       
	@Override
	public Response<EntityModel<RoleMaxDto>> details(Long id) {
            Response response=new Response<>(ResponseCode.SUCCESS,true,"VIEW ROLE DETAILS",null);
            try {

                Role role=roleRepository.getOne(id);
                RoleMaxDto data=new RoleMaxDto();
                data.setId(role.getId());
                data.setCode(role.getCode());
                data.setName(role.getName());
                data.setDescription(role.getDescription());
                data.setStatus(role.isStatus());
                
                List<PermissionMinDto> permissions=new ArrayList();
                for(RolePermission rolePermission: role.getRolePermissions()){
                    
                    permissions.add(permissionService.getPermissionMinDto(rolePermission.getPermissionID()));
                }
                
                data.setPermissions(permissions);
                
                response.setData(assembler2.toModel(data));
                
            }catch (Exception e) {

                log.error(e.getLocalizedMessage());
                e.printStackTrace();
                response.setCode(ResponseCode.FAILURE);
                response.setStatus(false);
                response.setMessage("INTERNAL SERVER ERROR");
            }
            
            return response;
	}

	@Override
	public Response<EntityModel<Role>> save(RoleDto data, Long id) {
            try {

                Role role = new Role();
                if (id > 0L) {
                   
                    role = roleRepository.getOne(id);
                    role.setUpdatedAt(LocalDateTime.now());
                } 
               
                role.setCode(data.getCode());
                role.setName(data.getName());
                role.setDescription(data.getDescription());
                role.setStatus(data.isStatus());
                role=roleRepository.saveAndFlush(role);
                        
                return new Response<>(ResponseCode.SUCCESS,true,"ROLE DETAILS",assembler.toModel(role));       
            } catch (EntityNotFoundException e) {

                e.printStackTrace();
                throw new DataNotFoundException("ROLE DETAILS NOT FOUND");
            }catch (Exception e) {

                e.printStackTrace();
                throw new GeneralException("INTERNAL SERVER ERROR");
            }
	}

	@Override
	@Transactional
	public Response<EntityModel<Role>> savePermissions(RolePermissionsDto data) {
            try {
                List<RolePermission> role_permissions = new ArrayList();
                Role role=roleRepository.getOne(data.getRoleID());
                rolePermissionRepo.deleteRolePermissions(data.getRoleID());

                for (Long permission : data.getPermissions()) {
                    RolePermission role_permission = new RolePermission();
                    role_permission.setRoleID(data.getRoleID());
                    role_permission.setPermissionID(permission);

                    role_permissions.add(role_permission);
                }

                if (role_permissions.size() > 0) {

                        rolePermissionRepo.saveAll(role_permissions);
                }

                return new Response<>(ResponseCode.SUCCESS,true,"ROLE DETAILS",assembler.toModel(role));
                
            } catch (EntityNotFoundException e) {

                logger.error(e.toString());
                throw new DataNotFoundException("ROLE DETAILS NOT FOUND");

            } catch (Exception e) {

                logger.error(e.toString());
                throw new GeneralException("INTERNAL SERVER ERROR");
            }
	}

    @Override
    public RoleMinDto getRoleMinDto(Long id) {
        RoleMinDto response=null;
        try{
        
            Optional<Role> existing=roleRepository.findById(id);
            if(!existing.isPresent()){
            
                return response;
            }
            
            Role data=existing.get();
            response=new RoleMinDto();
            response.setId(data.getId());
            response.setCode(data.getCode());
            response.setName(data.getName());
            response.setDescription(data.getDescription());            
            response.setStatus(data.isStatus());
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }
}
