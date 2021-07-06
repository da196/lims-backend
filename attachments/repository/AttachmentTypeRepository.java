package tz.go.tcra.lims.attachments.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.miscellaneous.enums.AttachmentTypePurposeEnum;

@Repository
public interface AttachmentTypeRepository extends JpaRepository<AttachmentType, Long> {

	Optional<AttachmentType> findAttachmentTypeById(Long id);

	boolean existsByIdAndActive(Long id, boolean b);

	AttachmentType findByIdAndActive(Long id, boolean b);

	List<AttachmentType> findByActive(boolean b);

	List<AttachmentType> findByAttachmentTypePurpose(AttachmentTypePurposeEnum purpose);

	Page<AttachmentType> findByActive(boolean b, Pageable pageable);

	@Query("SELECT b FROM AttachmentType b WHERE  " + "CONCAT(b.name,b.attachmentTypePurpose,b.description)"
			+ " LIKE %?1%")
	Page<AttachmentType> findByKeywordAndActive(String keyword, boolean b, Pageable pageable);
}
