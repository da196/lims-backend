/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tz.go.tcra.lims.workflow.entity.WorkflowType;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface WorkflowTypeRepository extends JpaRepository<WorkflowType,Long>{
    
    boolean existsByName(String name);
    
    List<WorkflowType> findByActive(Boolean active);
}
