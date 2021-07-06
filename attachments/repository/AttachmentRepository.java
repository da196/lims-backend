package tz.go.tcra.lims.attachments.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.entity.Attachment;

import org.springframework.data.jpa.repository.Modifying;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.entity.Attachable;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.miscellaneous.enums.AttachableTypeEnum;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    
    List<Attachment> findByAttachableIdAndAttachableTypeAndAttachmentTypeNotAndStatus(Long attachableId,AttachableTypeEnum attachableType,AttachmentType attachmentType,boolean status);

    Optional<Attachment> findFirstByAttachableIdAndAttachableTypeAndAttachmentTypeAndStatus(Long attachableId,AttachableTypeEnum attachableType,AttachmentType attachmentType,boolean status);
    
    @Query("SELECT "
            + "new tz.go.tcra.lims.attachments.dto.AttachmentMaxDto("
            + "e.uri,"
            + "e.attachmentName,"
            + "e.attachmentType.id,"
            + "e.attachmentType.name) "
            + "FROM Attachment e WHERE e.attachable=?1")
    List<AttachmentMaxDto> findCustomByAttachable(Attachable attachable);
    
//    Page<Attachment> findByAttachableIDAndAttachableType(Long attachableId,String attachableType,Pageable page);

//    @Query("SELECT e FROM Attachment e WHERE e.attachableID=?1 AND e.attachableType=?2")
//    List<Attachment> findByAttachableIDAndAttachableTypePortal(Long attachableId,String attachableType);
    
//    @Query("SELECT e FROM Attachment e WHERE e.attachmentType.id=?1")
//    Page<Attachment> findByAttachmentTypeID(Long attachmentTypeId,Pageable page);

    Optional<Attachment> findFirstByAttachableIdAndAttachableTypeAndAttachmentType(Long attachableId,AttachableTypeEnum attachableType,AttachmentType attachmentType);

//    void deleteAttachableAttachment(String attachableType, Long attachableId);

    @Modifying
    @Query("DELETE FROM Attachment e WHERE e.attachable=?1")
    void deleteByAttachable(Attachable attachableType);
    
    @Modifying
    @Query("DELETE FROM Attachment e WHERE e.attachable=?1 AND e.attachmentType=?2")
    void deleteByAttachableAndAttachmentType(Attachable attachableType,AttachmentType attachmentType);
}
