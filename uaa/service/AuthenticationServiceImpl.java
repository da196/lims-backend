/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.miscellaneous.enums.LicenceCategoryFlagEnum;
import tz.go.tcra.lims.uaa.dto.AuthRequestDto;
import tz.go.tcra.lims.uaa.dto.AuthResponseDto;
import tz.go.tcra.lims.uaa.dto.AuthResponseMinDto;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.uaa.entity.Permission;
import tz.go.tcra.lims.uaa.entity.RolePermission;
import tz.go.tcra.lims.uaa.entity.UserLogin;
import tz.go.tcra.lims.uaa.entity.UserRole;
import tz.go.tcra.lims.uaa.repository.PermissionRepository;
import tz.go.tcra.lims.uaa.repository.RolePermissionRepository;
import tz.go.tcra.lims.uaa.repository.UserLoginRepository;
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.uaa.repository.UserRoleRepository;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.JwtUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.AuthException;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.licence.repository.LicenceCategoryRepository;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private AuthenticationManager authenticationManager;
    
    //autowired repositories
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private UserRoleRepository userRoleRepo;   
        
    @Autowired
    private UserLoginRepository userLoginRepo;
    
    @Autowired
    private RolePermissionRepository rolePermissionRepo;
    
    @Autowired
    private PermissionRepository permissionRepo;
    
    @Autowired
    private LicenceCategoryRepository licenceCategoryRepo;
           
    //autowired services
    @Autowired
    private JwtUtility jwtUtil;
    
    @Autowired
    private AppUtility utility;
    
    @Autowired
    private AuthenticationModelAssembler assembler;
    
    @Override
    @Transactional
    public Response<EntityModel<AuthResponseDto>> authenticate(AuthRequestDto data) {
        
        try {            
            authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword())
                        ); 
            
            LimsUser db_user=userRepo.findByEmail(data.getUsername());
            
            Map<String,Object> claims=new HashMap();
            List<Permission> permissions=new ArrayList();
               
            Optional<UserRole> userRole=userRoleRepo.findByUserID(db_user.getId());
        
            if(!userRole.isPresent()){

                throw new Exception("USER ROLE NOT FOUND");
            }
            
            List<RolePermission> rolePermissions=rolePermissionRepo.findByRoleID(userRole.get().getRoleID());
            for(RolePermission rolePermission : rolePermissions){

                Permission permission=permissionRepo.getOne(rolePermission.getPermissionID());

                if(!permissions.contains(permission.getId())){

                    claims.put("ROLE",permission.getName().toString().toUpperCase()); 
                }
            }
                
            AuthResponseDto auth=new AuthResponseDto();
            auth.setId(db_user.getId());
            auth.setFullname(db_user.getFullName());
            auth.setEmail(db_user.getEmail());
            auth.setUsername(db_user.getEmail());
            auth.setPermissions(this.getUserPermissions(userRole.get()));
            auth.setComplete(db_user.isComplete());
            auth.setToken("Bearer "+jwtUtil.generateToken(data.getUsername(),claims));
            auth.setCategories(licenceCategoryRepo.findIdsByFlagAndActive(LicenceCategoryFlagEnum.ROOT, Boolean.TRUE));
            
            this.saveLogin(db_user);
            
            return new Response<>(ResponseCode.SUCCESS,true,"Authenticated",assembler.toModel(auth));
            
        }catch(Exception e){
            
            log.error(e.getLocalizedMessage());
            throw new AuthException(e.getMessage());
        }
    }

    @Override
    public void saveLogin(LimsUser user) {
        try{        
            UserLogin userLogin=new UserLogin();
            userLogin.setUser(user);
            userLogin.setCreatedAt(LocalDateTime.now());
           
            userLoginRepo.saveAndFlush(userLogin);
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
    
    private List<String> getUserPermissions(UserRole role){
        List<String> response=new ArrayList();
        try{
            
            List<RolePermission> rolePermissions=rolePermissionRepo.findByRoleID(role.getRoleID());
            for(RolePermission rolePermission : rolePermissions){

                Permission permission=permissionRepo.getOne(rolePermission.getPermissionID());
                if(!response.contains(permission.getName())){

                    response.add(permission.getName());
                }
            }
            
        }catch(EntityNotFoundException e){
            
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new DataNotFoundException("INTERNAL SERVER ERROR");
            
        }catch(Exception e){
            
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new AuthException("INTERNAL SERVER ERROR");
        }
        return response;
    }

    @Override
    public AuthResponseMinDto authenticate2(AuthRequestDto data) {
        try {            
            authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword())
                        ); 
            
            LimsUser db_user=userRepo.findByEmail(data.getUsername());
            this.saveLogin(db_user);
            
            Map<String,Object> claims=new HashMap();            
            List<Permission> permissions=new ArrayList();
            
            Optional<UserRole> userRole=userRoleRepo.findByUserID(db_user.getId());
        
            if(!userRole.isPresent()){

                throw new Exception("USER ROLE NOT FOUND");
            }
            List<RolePermission> rolePermissions=rolePermissionRepo.findByRoleID(userRole.get().getRoleID());
            for(RolePermission rolePermission : rolePermissions){

                Permission permission=permissionRepo.getOne(rolePermission.getPermissionID());

                if(!permissions.contains(permission.getId())){

                    claims.put("ROLE",permission.getName().toString().toUpperCase()); 
                }
            }
                
            AuthResponseMinDto auth=new AuthResponseMinDto();
            auth.setId(db_user.getId());
            auth.setFullname(db_user.getFullName());
            auth.setEmail(db_user.getEmail());
            auth.setUsername(db_user.getEmail());
            auth.setComplete(db_user.isComplete());
            auth.setToken("Bearer "+jwtUtil.generateToken(data.getUsername(),claims));
            
            return auth;
            
        }catch(Exception e){
            
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            throw new AuthException(e.getMessage());
        }
    }

    @Override
    public Response<String> refreshToken() {
        Response<String> response=new Response<>(ResponseCode.SUCCESS,true,"TOKEN REFRESHED SUCCESSFULLY",null);
        try{
            LimsUser user=utility.getUser();
            
            Map<String,Object> claims=new HashMap();
            List<Permission> permissions=new ArrayList();
            
            Optional<UserRole> userRole=userRoleRepo.findByUserID(user.getId());
        
            if(!userRole.isPresent()){

                throw new DataNotFoundException("USER ROLE NOT FOUND");
            }
            
            List<RolePermission> rolePermissions=rolePermissionRepo.findByRoleID(userRole.get().getRoleID());
            for(RolePermission rolePermission : rolePermissions){

                Permission permission=permissionRepo.getOne(rolePermission.getPermissionID());

                if(!permissions.contains(permission.getId())){

                    claims.put("ROLE",permission.getName().toString().toUpperCase()); 
                }
            }
                
            String token="Bearer "+jwtUtil.generateToken(user.getEmail(),claims);
            
            response.setData(token);
            
        }catch(DataNotFoundException e){
        
            
            throw new DataNotFoundException(e.getLocalizedMessage());
            
        }catch(Exception e){
        
            e.printStackTrace();
            throw new GeneralException("INTERNAL_SERVER_ERROR");
        }
        
        return response;
    }
    
}
