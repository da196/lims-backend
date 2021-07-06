package tz.go.tcra.lims.uaa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.uaa.entity.Permission;
import tz.go.tcra.lims.uaa.entity.RolePermission;
import tz.go.tcra.lims.uaa.entity.UserRole;
import tz.go.tcra.lims.uaa.repository.PermissionRepository;
import tz.go.tcra.lims.uaa.repository.RolePermissionRepository;
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.uaa.repository.UserRoleRepository;
import tz.go.tcra.lims.utils.exception.AuthException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RolePermissionRepository rolePermissionRepo;

    @Autowired
    private PermissionRepository permissionRepo;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserRoleRepository userRoleRepo;
    
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails user_details = null;
        try {

            LimsUser db_user = repository.findByEmail(username);

            if (db_user == null) {

                throw new UsernameNotFoundException("user not found");
            }

            if (db_user.isStatus() == false) {

                throw new UsernameNotFoundException("user not active");
            }

            user_details = new User(db_user.getEmail(), db_user.getPassword(), this.getAuthority(db_user));

        } catch (UsernameNotFoundException e) {

            e.printStackTrace();
            throw new AuthException("User detail error");
        }

        return user_details;
    }

    private Set getAuthority(LimsUser user) {

        Set authorities = new HashSet<>();
        
        Optional<UserRole> userRole=userRoleRepo.findByUserID(user.getId());
        
        if(!userRole.isPresent()){
        
            return authorities;
        }
        
        List<RolePermission> rolePermissions=rolePermissionRepo.findByRoleID(userRole.get().getRoleID());
        for(RolePermission rolePermission : rolePermissions){
            Permission permission=permissionRepo.getOne(rolePermission.getPermissionID());
            SimpleGrantedAuthority authority=new SimpleGrantedAuthority(permission.getName().toUpperCase());
            if(!authorities.contains(authority)){

                authorities.add(authority);
            }
        }
            
        return authorities;
    }
}
