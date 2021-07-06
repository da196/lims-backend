/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.uaa.entity.RolePermission;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission,Long>{
    
    @Query("SELECT e FROM RolePermission e WHERE e.roleID=?1")
    public List<RolePermission> findByRoleID(Long role_id);
    
    public Optional<RolePermission> findByRoleIDAndPermissionID(Long role_id,Long permission_id);
    
    @Modifying
    @Query("DELETE FROM RolePermission e WHERE e.roleID=?1")
    public void deleteRolePermissions(Long role_id);
}
