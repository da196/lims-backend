package tz.go.tcra.lims.attachments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.attachments.dto.AttachmentTypeDto;
import tz.go.tcra.lims.attachments.services.AttachmentTypeService;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping(value = "/v1/attachment-types")
public class AttachmentTypeController {

	@Autowired
	private AttachmentTypeService service;

	@PostMapping(value = "/save")
	@PreAuthorize("hasRole('ROLE_ATTACHMENTS_TYPE_SAVE')")
	Response<EntityModel<AttachmentType>> saveAttachmentType(@RequestBody AttachmentTypeDto attachmentTypeDto) {

		return service.saveAttachmentType(attachmentTypeDto, 0L);
	}

	@PutMapping(value = "/update/{id}")
	@PreAuthorize("hasRole('ROLE_ATTACHMENTS_TYPE_EDIT')")
	Response<EntityModel<AttachmentType>> updateAttachmentType(@RequestBody AttachmentTypeDto attachmentTypeDto,
			@PathVariable("id") Long id) {

		return service.saveAttachmentType(attachmentTypeDto, id);
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ATTACHMENTS_TYPE_VIEW')")
	public Response<EntityModel<AttachmentType>> findAttachmentTypeById(@PathVariable Long id) {

		return service.findAttachmentTypeById(id);
	}

	@GetMapping(value = "/list")
	@PreAuthorize("hasRole('ROLE_ATTACHMENTS_TYPE_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<AttachmentType>>> getListOfAttachmentTypes(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return service.getListOfAttachmentTypes(keyword, pageable);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ATTACHMENTS_TYPE_DELETE')")
	Response<EntityModel<AttachmentType>> deleteAttachmentType(@PathVariable("id") Long id) {

		return service.deleteAttachmentType(id);

	}
}
