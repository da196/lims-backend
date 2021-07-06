/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.uaa.entity.UserRegistration;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration,Long>{
    
    public UserRegistration findByEmail(String email);
    
    @Query("SELECT e FROM UserRegistration e WHERE e.token.token=?1")
    public UserRegistration findByToken(String token);
}
