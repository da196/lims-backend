package tz.go.tcra.lims.licence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.miscellaneous.enums.LicenceCategoryFlagEnum;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto;

@Repository
public interface LicenceCategoryRepository extends JpaRepository<LicenceCategory, Long> {

	boolean existsByIdAndActive(Long id, boolean active);

	LicenceCategory findByIdAndActive(Long id, boolean active);

	boolean existsByNameOrCodeAndActive(String name, String code, boolean active);

	List<LicenceCategory> findByActive(boolean active);

        @Query("SELECT e FROM LicenceCategory e WHERE e.parent=?1 AND e.active=?2")
	List<LicenceCategory> findAllByParentAndActive(Long parentId,Boolean active);

	@Query("SELECT e FROM LicenceCategory e WHERE e.parent IS NULL")
	List<LicenceCategory> findByAllNullParent();

	List<LicenceCategory> findByParent(Object object);
	
        @Query("SELECT e FROM LicenceCategory e WHERE e.parent=?1 AND e.active=?2")
        List<LicenceCategory> findByParentAndActivePortal(Long parentId,Boolean active);

        @Query("SELECT e FROM LicenceCategory e WHERE e.parent IS NULL AND e.active=?1")
        List<LicenceCategory> findByAllNullParentAndActivePortal(Boolean active);
        
        @Query("SELECT e FROM LicenceCategory e WHERE e.flag=?1 AND e.active=?2")
	List<LicenceCategory> findByFlagAndActive(LicenceCategoryFlagEnum flag,Boolean active);
        
        @Query("SELECT new tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto("
               + "e.id,"
               + "e.code,"
               + "e.name,"
               + "e.displayName,"
               + "e.active) FROM LicenceCategory e WHERE e.flag !=?1 AND e.active=?2")
        List<LicenseCategoryMinDto> findAllWithoutFlagMinAndActive(LicenceCategoryFlagEnum flag,Boolean active);
        
        @Query("SELECT new tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto(e.id,e.code,e.name,e.displayName,e.active) FROM LicenceCategory e WHERE e.flag=?1 AND e.active=?2")
        List<LicenseCategoryMinDto> findIdsByFlagAndActive(LicenceCategoryFlagEnum flag,Boolean active);
}
