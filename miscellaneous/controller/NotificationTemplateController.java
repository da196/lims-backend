package tz.go.tcra.lims.miscellaneous.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.miscellaneous.entity.NotificationTemplate;
import tz.go.tcra.lims.miscellaneous.dto.NotificationTemplateDto;
import tz.go.tcra.lims.miscellaneous.service.NotificationTemplateService;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "v1/notification-template")
public class NotificationTemplateController {

	@Autowired
	private NotificationTemplateService service;

	@PostMapping("/save")
	@PreAuthorize("hasRole('ROLE_NOTIFICATION_TEMPLATE_SAVE')")
	public Response<EntityModel<NotificationTemplate>> saveNotificationTemplate(
			@RequestBody NotificationTemplateDto data) {

		return service.saveNotificationTemplate(data, 0L);
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ROLE_NOTIFICATION_TEMPLATE_EDIT')")
	public Response<EntityModel<NotificationTemplate>> updateNotificationTemplate(
			@RequestBody NotificationTemplateDto data, @PathVariable("id") Long id) {

		return service.saveNotificationTemplate(data, id);
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_NOTIFICATION_TEMPLATE_VIEW')")
	public Response<EntityModel<NotificationTemplate>> findNotificationTemplateById(@PathVariable("id") Long id) {

		return service.getNotificationTemplateDetails(id);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_NOTIFICATION_TEMPLATE_VIEW_ACTIVE')")
	public Response<CollectionModel<EntityModel<NotificationTemplate>>> getListOfNotificationTemplates(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return service.listNotificationTemplates(keyword, pageable);
	}

	@PutMapping("/de-activate/{id}")
	@PreAuthorize("hasRole('ROLE_NOTIFICATION_TEMPLATE_ACTIVATION_DEACTIVATION')")
	public Response<EntityModel<NotificationTemplate>> activateDeactivateNotificationTemplateById(
			@PathVariable("id") Long id) {

		return service.activateDeactivateNotification(id);
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasRole('ROLE_NOTIFICATION_TEMPLATE_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<NotificationTemplate>>> getListOfAllNotificationTemplates(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "15") int size,
			@RequestParam(name = "sort_name", defaultValue = "id") String sortName,
			@RequestParam(name = "sort_type", defaultValue = "DESC") String sortType,
			@RequestParam(name = "name", required = false) String name) {

		return service.listAllNotificationTemplates(page, size, sortName, sortType, name);
	}

}