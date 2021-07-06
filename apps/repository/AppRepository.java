/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tz.go.tcra.lims.entity.App;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface AppRepository extends JpaRepository<App,Integer>{
    
}
