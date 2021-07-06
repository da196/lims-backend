/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.uaa.entity.UserPermission;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission,Integer>{
    
}
