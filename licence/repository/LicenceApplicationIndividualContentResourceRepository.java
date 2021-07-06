package tz.go.tcra.lims.licence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.IndividualLicenceApplicationContentResource;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceApplicationIndividualContentResourceRepository extends JpaRepository<IndividualLicenceApplicationContentResource, Long> {
    
    Optional<IndividualLicenceApplicationContentResource> findByIndividualId(Long individualId);
    
    IndividualLicenceApplicationContentResource findFirstByIndividualIdAndResource(Long individualId,ListOfValue resource);
    
}
