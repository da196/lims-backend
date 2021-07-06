/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.product.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.miscellaneous.enums.ProductTypeEnum;
import tz.go.tcra.lims.product.entity.ProductWorkflow;
import tz.go.tcra.lims.product.entity.Productable;
import tz.go.tcra.lims.workflow.entity.Workflow;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface ProductWorkflowRepository extends JpaRepository<ProductWorkflow,Long>{
    
    @Modifying
    @Query("DELETE FROM ProductWorkflow e WHERE e.productable=?1")
    void deleteByProductable(Productable productable);
    
    @Modifying
    @Query("UPDATE ProductWorkflow e SET e.active=?1 WHERE e.productableId=?2 AND e.productableType=?3")
    void deActivateByProductable(Boolean active,Long productableId,ProductTypeEnum productableType);
    
    List<ProductWorkflow> findByProductableIdAndProductableTypeAndWorkflowAndActive(Long productableId,ProductTypeEnum productableType,Workflow workflow,Boolean active);
    
    Optional<ProductWorkflow> findByProductableIdAndProductableTypeAndWorkflow(Long productableId,ProductTypeEnum productableType,Workflow workflow);
    
    @Query("SELECT e FROM ProductWorkflow e WHERE e.productable=?1 AND e.workflow.workflowType.code=?2 AND e.active=?3")
    Optional<ProductWorkflow> findByProductableAndWorkflowTypeCode(Productable productable,String code,Boolean active);
}
