/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

import tz.go.tcra.lims.workflow.entity.WorkflowStepDecision;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface WorkflowStepDecisionRepository extends JpaRepository<WorkflowStepDecision, Long> {

    void deleteByStep(Long step);

    @Query("SELECT e.decision FROM WorkflowStepDecision e WHERE e.step=?1 AND e.active=?2")
    List<WorkflowDecisionEnum> findDecisionEnumByStepAndActive(Long workFlowStepId, boolean b);
    
    List<WorkflowStepDecision> findByStepAndActive(Long workFlowStepId, boolean b);

    Optional<WorkflowStepDecision> findByStepAndDecision(Long workflowStepId,WorkflowDecisionEnum decision);
}
