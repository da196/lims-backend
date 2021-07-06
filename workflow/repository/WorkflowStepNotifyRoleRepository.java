/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;
import tz.go.tcra.lims.workflow.entity.WorkflowStepNotifyRole;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface WorkflowStepNotifyRoleRepository extends JpaRepository<WorkflowStepNotifyRole,Long>{
    
    void deleteByStep(Long step);
}
