package tz.go.tcra.lims.miscellaneous.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.miscellaneous.dto.ListOfValueMinDto;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;

@Repository
public interface ListOfValueRepository extends JpaRepository<ListOfValue, Long> {

	boolean existsByNameOrCodeAndActive(String name, String code, boolean b);

	boolean existsByIdAndActive(Long id, boolean b);

	ListOfValue findByIdAndActive(Long id, boolean b);

	List<ListOfValue> findByActive(boolean b);

	List<ListOfValue> findByParent(ListOfValue parent);

	boolean existsByIdInAndActive(List<Long> listOfValuesId, boolean b);

	List<ListOfValue> findByType(ListOfValueTypeEnum type);

	List<ListOfValue> findByTypeAndActive(ListOfValueTypeEnum type, boolean b);

	Page<ListOfValue> findByActive(boolean b, Pageable pageable);

	@Query("SELECT b FROM ListOfValue b WHERE  " + "CONCAT(b.name,b.type,b.active)" + " LIKE %?1%")
	Page<ListOfValue> findByKeywordAndActive(String keyword, boolean b, Pageable pageable);

	@Query("SELECT new tz.go.tcra.lims.miscellaneous.dto.ListOfValueMinDto(e.id,e.code,e.name) FROM ListOfValue e WHERE e.id=?1")
	ListOfValueMinDto findByIdMinDto(Long id);

	ListOfValue findFirstByCodeAndActive(String string, boolean b);

}
