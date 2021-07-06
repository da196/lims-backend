package tz.go.tcra.lims.miscellaneous.controller;

import java.util.List;

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

import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.miscellaneous.dto.StatusDto;
import tz.go.tcra.lims.miscellaneous.dto.StatusMinDto;
import tz.go.tcra.lims.miscellaneous.service.StatusService;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "v1/status")
public class StatusController {

	@Autowired
	private StatusService service;

	@PostMapping("/save")
	@PreAuthorize("hasRole('ROLE_STATUS_SAVE')")
	public Response<EntityModel<Status>> saveStatus(@RequestBody StatusDto data) {

		return service.saveStatus(data, 0L);
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ROLE_STATUS_EDIT')")
	public Response<EntityModel<Status>> updateStatus(@RequestBody StatusDto data, @PathVariable("id") Long id) {

		return service.saveStatus(data, id);
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_STATUS_VIEW')")
	public Response<EntityModel<Status>> findStatusById(@PathVariable("id") Long id) {

		return service.getStatusDetails(id);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_STATUS_VIEW_ACTIVE')")
	public Response<List<StatusMinDto>> getListOfStatus() {

		return service.listStatus();
	}

	@PutMapping("/de-activate/{id}")
	@PreAuthorize("hasRole('ROLE_STATUS_ACTIVATION_DEACTIVATION')")
	public Response<EntityModel<Status>> activateDeactivateStatusById(@PathVariable("id") Long id) {

		return service.activateDeactivateStatus(id);
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasRole('ROLE_STATUS_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<Status>>> getListOfAllStatus(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return service.listAllStatus(keyword, pageable);
	}

}