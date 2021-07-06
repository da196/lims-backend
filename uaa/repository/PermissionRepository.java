/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.uaa.entity.Permission;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{
    
    Optional<Permission> findPermissionById(Long id);
    Optional<Permission> findPermissionByName(String name);
    List<Permission> findPermissionByGroupName(String name);
    boolean existsByName(String name);

}
