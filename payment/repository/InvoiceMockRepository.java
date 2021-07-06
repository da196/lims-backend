package tz.go.tcra.lims.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.payment.entity.InvoiceMock;

@Repository
public interface InvoiceMockRepository extends JpaRepository<InvoiceMock, Long> {

	boolean existsByIdAndActive(Long id, boolean b);

	InvoiceMock findByIdAndActive(Long id, boolean b);

	List<InvoiceMock> findByActive(boolean b);

}
