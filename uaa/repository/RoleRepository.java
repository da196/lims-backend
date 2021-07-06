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

import tz.go.tcra.lims.uaa.entity.Role;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

//    public List<Role> findByOrderByCodeDesc();

	public List<Role> findByStatus(Boolean status);

	Optional<Role> findByName(String name);

	public Role findByIdAndStatus(Long nextRoleId, boolean b);
}
