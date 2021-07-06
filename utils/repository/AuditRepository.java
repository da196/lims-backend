package tz.go.tcra.lims.utils.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.Audit;


/**
 * @author DonaldSj
 */

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {


}
