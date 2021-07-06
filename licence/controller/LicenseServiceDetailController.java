package tz.go.tcra.lims.licence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.entity.LicenceServiceDetail;
import tz.go.tcra.lims.licence.dto.LicenseDetailDto;
import tz.go.tcra.lims.licence.service.LicenseDetailService;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping("/v1/license-service-detail")
public class LicenseServiceDetailController {

	@Autowired
	private LicenseDetailService licenseDetailService;

	@PostMapping(value = "/save")
	public Response<EntityModel<LicenceServiceDetail>> saveLicenseDetail(
			@RequestBody LicenseDetailDto licenseDetailDto) {
		return licenseDetailService.saveLicenseDetail(licenseDetailDto, 0L);
	}

	@PutMapping(value = "/update/{id}")
	public Response<EntityModel<LicenceServiceDetail>> updateLicenseDetail(
			@RequestBody LicenseDetailDto licenseDetailDto, @PathVariable("id") Long id) {
		return licenseDetailService.saveLicenseDetail(licenseDetailDto, id);
	}

	@GetMapping(value = "/{id}")
	public Response<EntityModel<LicenceServiceDetail>> findLicenseDetailById(@PathVariable("id") Long id) {
		return licenseDetailService.findLicenseDetailById(id);
	}

	@DeleteMapping(value = "/delete/{id}")
	public Response<EntityModel<LicenceServiceDetail>> deleteLicenseDetailById(@PathVariable("id") Long id) {
		return licenseDetailService.deleteLicenseDetailById(id);
	}

	@GetMapping(value = "/list")
	public Response<CollectionModel<EntityModel<LicenceServiceDetail>>> getListOfLicenseDetail(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return licenseDetailService.getListOfLicenseDetail(keyword, pageable);
	}
}
