/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.task.entity.LicencePresentation;
import tz.go.tcra.lims.task.dto.PresentationDto;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicencePresentationRepository extends JpaRepository<LicencePresentation,Long>{
    
    Optional<LicencePresentation> findFirstByLicenceIdAndWorkflowIdAndWorkflowStepIdAndActive(Long licenceId,Long workflowId,Long workflowStepId,Boolean active);
    
    @Query("SELECT new tz.go.tcra.lims.task.dto.PresentationDto("
            + "e.id,"
            + "e.licence.applicantEntity.name,"
            + "e.licence.applicationNumber,"
            + "e.licence.licenceNumber,"
            + "e.licence.licenseProduct.displayName,"
            + "e.presentationDate,"         
            + "e.remark) "
            + "FROM LicencePresentation e WHERE e.status=?1")
    Page<PresentationDto> findAllPresentations(WorkflowDecisionEnum decision,Pageable pageable);
    
    @Query("SELECT e FROM LicencePresentation e WHERE e.licence.applicantEntity.referingEntityId=?1")
    List<LicencePresentation> findApplicantPresentations(Long applicantEntityId);
}
