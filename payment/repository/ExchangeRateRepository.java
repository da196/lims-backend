package tz.go.tcra.lims.payment.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.payment.entity.ExchangeRate;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

	boolean existsByStartDateAndActive(Date startDate, boolean b);

	boolean existsByIdAndActive(Long id, boolean b);

	ExchangeRate findByIdAndActive(Long id, boolean b);

	List<ExchangeRate> findByActive(boolean b);

	boolean existsByStartDateOrExpireDateAndActive(Date exchangeDate, Date exchangeDate2, boolean b);

	boolean existsByStartDateAfterAndExpireDateBeforeAndActive(Date exchangeDate, Date exchangeDate2, boolean b);

	ExchangeRate findByStartDateOrExpireDateAndActive(Date exchangeDate, Date exchangeDate2, boolean b);

	ExchangeRate findByStartDateAfterAndExpireDateBeforeAndActive(Date exchangeDate, Date exchangeDate2, boolean b);

	boolean existsByStartDateAndActiveAndCurrecyFromAndCurrecyTo(Date startDate, boolean b, ListOfValue currency1,
			ListOfValue currency2);

	Page<ExchangeRate> findByActive(boolean b, Pageable pageable);

	@Query("SELECT b FROM ExchangeRate b WHERE  " + "CONCAT(b.currecyFrom.code,b.currecyTo.code,b.amount)"
			+ " LIKE %?1%" + "AND b.active = ?2")
	Page<ExchangeRate> findByKeywordAndActive(String keyword, boolean b, Pageable pageable);

}
