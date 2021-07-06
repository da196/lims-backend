package tz.go.tcra.lims.portal.attachments.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.miscellaneous.enums.AttachmentTypePurposeEnum;
import tz.go.tcra.lims.portal.attachments.dto.AttachmentTypePortalDto;
import tz.go.tcra.lims.portal.attachments.service.AttachmentTypePortalService;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping("/v1/p/attachment_type")
public class AttachmentTypePortalController {

	@Autowired
	AttachmentTypePortalService attachmentTypePortalService;

	@GetMapping(value = "/{id}")
	public Response<AttachmentTypePortalDto> findAttachmentTypeById(Long id) {

		return attachmentTypePortalService.findAttachmentTypeById(id);

	}

	@GetMapping(value = "/list")
	public Response<List<AttachmentTypePortalDto>> getListOfAttachmentTypes() {

		return attachmentTypePortalService.getListOfAttachmentTypes();

	}

	@GetMapping(value = "/getByPurpose/{purpose}")
	public Response<List<AttachmentTypePortalDto>> getListOfAttachmentTypesByPurpose(
			@PathVariable("purpose") AttachmentTypePurposeEnum purpose) {
		return attachmentTypePortalService.getListOfAttachmentTypesByPurpose(purpose);

	}

}
