package tz.go.tcra.lims.attachments.services;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import tz.go.tcra.lims.attachments.dto.AttachmentTypeDto;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.utils.Response;

public interface AttachmentTypeService {

	Response<EntityModel<AttachmentType>> saveAttachmentType(AttachmentTypeDto data, Long id);

	Response<EntityModel<AttachmentType>> findAttachmentTypeById(Long id);

	Response<EntityModel<AttachmentType>> deleteAttachmentType(Long id);

	Response<CollectionModel<EntityModel<AttachmentType>>> getListOfAttachmentTypes(String keyword, Pageable pageable);
}
