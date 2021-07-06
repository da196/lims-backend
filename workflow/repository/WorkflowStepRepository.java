/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.uaa.entity.Role;

import tz.go.tcra.lims.workflow.entity.Workflow;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {

    List<WorkflowStep> findByWorkflow(Workflow workflow);

    boolean existsByPreviousRoleAndWorkflowAndActive(Role nextRoleId, Long workFlowId, boolean b);

    WorkflowStep findByPreviousRoleAndWorkflowAndActive(Role nextRoleId, Long workFlowId, boolean b);
    
    Optional<WorkflowStep> findFirstByWorkflowAndActiveOrderByStepNumberAsc(Long workflowId,Boolean active);
    
    Optional<WorkflowStep> findFirstByWorkflowAndActiveAndStepNumberGreaterThanOrderByStepNumberAsc(Long workflowId,Boolean active,Integer stepNumber);
    
    Optional<WorkflowStep> findFirstByWorkflowAndActiveOrderByStepNumberDesc(Long workflowId,Boolean active);
    
    Optional<WorkflowStep> findFirstByWorkflowAndActiveAndStepNumberLessThanOrderByStepNumberDesc(Long workflowId,Boolean active,Integer stepNumber);
}
