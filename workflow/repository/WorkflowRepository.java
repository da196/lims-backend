/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.workflow.entity.Workflow;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

	List<Workflow> findByActive(Boolean active);

	Workflow findByIdAndActive(Long workFlowId, boolean b);

	boolean existsByIdAndActive(Long workFlowId, boolean b);

	Page<Workflow> findByActive(boolean status, Pageable pageable);

	@Query("SELECT b FROM Workflow b WHERE  " + "CONCAT(b.name,b.code,b.workflowType.name,b.active)" + " LIKE %?1%"
			+ "AND b.active = ?2")
	Page<Workflow> findByKeywordAndActive(String keyword, boolean status, Pageable pageable);

	@Query("SELECT b FROM Workflow b WHERE  " + "CONCAT(b.name,b.code,b.workflowType.name,b.active)" + " LIKE %?1%")
	Page<Workflow> findAllByKeyword(String keyword, Pageable pageable);
}
