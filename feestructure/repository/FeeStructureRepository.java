package tz.go.tcra.lims.feestructure.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.feestructure.entity.Feeable;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;

/**
 * @author DonaldSj
 */

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {

	boolean existsByNameOrCodeAndActive(String name, String code, boolean b);

	boolean existsByIdAndActive(Long feeId, boolean b);

	FeeStructure findByIdAndActive(Long feeId, boolean b);

	List<FeeStructure> findByActive(boolean b);

        Page<FeeStructure> findByActive(boolean b,Pageable pageable);
        
        @Query("SELECT e FROM FeeStructure e WHERE e.active=?1 AND "
                + "CONCAT(e.name,e.code,e.accountCode,e.applicableState,e.feeType.name,e.name) LIKE '%?2%' "
                + "ORDER BY id DESC")
        Page<FeeStructure> findByActiveAndKeyword(boolean b,String keyword,Pageable pageable);
        
	@Query("SELECT e FROM FeeStructure e WHERE e.feeable=?1 AND e.feeType=?2")
	FeeStructure findByFeeableAndFeeType(Feeable feeable, ListOfValue type);

	@Query("SELECT e FROM FeeStructure e WHERE e.feeable=?1 AND e.applicableState=?2")
	List<FeeStructure> findByFeeableAndApplicableState(Feeable feeable, ApplicableStateEnum applicableState);
	
        @Query("SELECT e FROM FeeStructure e WHERE e.feeable=?1")
        List<FeeStructure> findByFeeable(Feeable feeable);
        
        @Query("SELECT e FROM FeeStructure e WHERE e.feeable=?1 AND e.active=?2")
        List<FeeStructure> findByFeeableAndActive(Feeable feeable,Boolean active);
        
        List<FeeStructure> findByFeeableTypeAndActive(FeeableTypeEnum feeableType,Boolean active);
	
        @Query("SELECT e.id FROM FeeStructure e WHERE e.feeable=?1 AND e.applicableState=?2")
	List<Long> findFeeIdByFeeableAndApplicableState(Feeable feeable, ApplicableStateEnum applicableState);
        
        @Query("SELECT e.id FROM FeeStructure e WHERE e.feeable=?1 AND e.applicableState=?2 AND e.active=?3")
	List<Long> findFeeIdByFeeableAndApplicableStateAndActive(Feeable feeable, ApplicableStateEnum applicableState,Boolean active);

	boolean existsByIdInAndActive(List<Long> feeIds, boolean b);

	List<FeeStructure> findByIdInAndActive(List<Long> feeIds, boolean b);
        
        @Modifying
        @Transactional
        @Query("UPDATE FeeStructure e SET e.active=false WHERE e.feeable=?1")
        void deactivateFeesByFeeable(Feeable feeable);
}
