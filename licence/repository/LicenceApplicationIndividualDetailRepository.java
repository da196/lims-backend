package tz.go.tcra.lims.licence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.IndividualLicenceApplicationDetail;
import tz.go.tcra.lims.entity.Licence;

/**
 * @author DonaldSj
 */

@Repository
public interface LicenceApplicationIndividualDetailRepository extends JpaRepository<IndividualLicenceApplicationDetail, Long> {
    
    Optional<IndividualLicenceApplicationDetail> findByLicense(Licence license);
}
