package tz.go.tcra.lims.licence.controller;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.licence.dto.LicenceCancellationDto;
import tz.go.tcra.lims.licence.dto.LicenceMaxDto;

import tz.go.tcra.lims.licence.dto.LicenseMinDto;
import tz.go.tcra.lims.task.dto.PresentationDto;
import tz.go.tcra.lims.licence.service.LicenseService;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/license")
public class LicenseController {

	@Autowired
	private LicenseService service;

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_LICENSE_VIEW')")
	public Response<LicenceMaxDto> findLicenseById(@PathVariable(name = "id") Long id) {
            
            return service.findLicenseById(id);
	}


	@GetMapping("/all")
	@PreAuthorize("hasRole('ROLE_LICENSE_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<LicenseMinDto>>> getListOfAllLicenses(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "15") int size,
			@RequestParam(name = "sort_name", defaultValue = "id") String sortName,
			@RequestParam(name = "sort_type", defaultValue = "DESC") String sortType,
			@RequestParam(name = "productid", defaultValue = "0") Long product,
                        @RequestParam(name = "rootid", defaultValue = "0") Long root) {

		return service.getListOfAllLicences(page, size, sortName, sortType, product,root);
	}

	@GetMapping("/list-non-draft")
	@PreAuthorize("hasRole('ROLE_LICENSE_VIEW_NON_DRAFT')")
	public Response<CollectionModel<EntityModel<LicenseMinDto>>> getListOfNonDraftLicenses(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "15") int size,
			@RequestParam(name = "sort_name", defaultValue = "id") String sortName,
			@RequestParam(name = "sort_type", defaultValue = "DESC") String sortType,
			@RequestParam(name = "productid", defaultValue = "0") Long product,
                        @RequestParam(name = "rootid", defaultValue = "0") Long root) {

		return service.getListOfNonDraftLicences(page, size, sortName, sortType, product,root);
	}
        
        @GetMapping("/presentations")
	@PreAuthorize("hasRole('ROLE_LICENSE_VIEW_PRESENTATIONS')")
	public Response<CollectionModel<EntityModel<PresentationDto>>> getListOfPresentations(Pageable page) {

            return service.getListOfPresentations(page);
	}
        
        @GetMapping("/presentation-attachment/{id}")
	@PreAuthorize("hasRole('ROLE_VIEW_PRESENTATION_ATTACHMENT')")
	public Response<AttachmentMaxDto> getPresentationAttachment(@Min(1) @PathVariable("id") Long id) {

            return service.getPresentation(id);
	}
        
        @PostMapping(value = "/initiate-cancellation")
        @PreAuthorize("hasRole('ROLE_LICENCE_CANCELLATION')")
	public Response<Licence> initiateLicenceCancellation(@Valid @RequestBody LicenceCancellationDto data) {
            
            return service.licenceCancellation(data);
	}
}
