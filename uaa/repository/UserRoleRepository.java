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

import tz.go.tcra.lims.uaa.entity.UserRole;

/**
 *
 * @author emmanuel.mfikwa
 */

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    Optional<UserRole> findFirstByRoleID(Long roleId);
    
    List<UserRole> findByRoleID(Long roleId);
    
    Optional<UserRole> findByUserID(Long userId);
    
    @Query("SELECT e.userID FROM UserRole e "
            + "INNER JOIN LimsUser e2 ON e.userID=e2.id WHERE e.roleID=?1 AND e2.status=?2")
    List<Long> getUserIdByRoleIDAndActive(Long roleId,Boolean active);
    
    @Modifying
    @Query("DELETE FROM UserRole e WHERE e.userID=?1")
    void deleteByUserID(Long userId);
}
