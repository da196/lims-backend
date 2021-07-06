package tz.go.tcra.lims.portal.attachments.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tz.go.tcra.lims.miscellaneous.enums.AttachmentTypePurposeEnum;
import tz.go.tcra.lims.portal.attachments.dto.AttachmentTypePortalDto;
import tz.go.tcra.lims.utils.Response;

@Service
public interface AttachmentTypePortalService {

	Response<AttachmentTypePortalDto> findAttachmentTypeById(Long id);

	Response<List<AttachmentTypePortalDto>> getListOfAttachmentTypes();

	Response<List<AttachmentTypePortalDto>> getListOfAttachmentTypesByPurpose(AttachmentTypePurposeEnum purpose);
}
