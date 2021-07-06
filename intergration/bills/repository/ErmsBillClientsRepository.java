package tz.go.tcra.lims.intergration.bills.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.intergration.bills.entity.ErmsBillClients;

@Repository
public interface ErmsBillClientsRepository extends JpaRepository<ErmsBillClients, Long> {

}
