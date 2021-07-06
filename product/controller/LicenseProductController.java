package tz.go.tcra.lims.product.controller;

import javax.validation.Valid;

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

import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.product.dto.LicenceProductDto;
import tz.go.tcra.lims.product.dto.LicenceProductMaxDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.product.service.LicenceProductService;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/licence-products")
public class LicenseProductController {

	@Autowired
	private LicenceProductService service;

	@PostMapping("/save")
        @PreAuthorize("hasRole('ROLE_PRODUCT_SAVE')")
	public Response<EntityModel<LicenceProduct>> saveLicenseProduct(
			@Valid @RequestBody LicenceProductDto licenceProductDto) {
		return service.saveLicenseProduct(licenceProductDto, 0L);
	}

	@PutMapping("/update/{id}")
        @PreAuthorize("hasRole('ROLE_PRODUCT_EDIT')")
	public Response<EntityModel<LicenceProduct>> updateLicenseProduct(@PathVariable(name = "id") Long id,
			@Valid @RequestBody LicenceProductDto licenceProductDto) {
		return service.saveLicenseProduct(licenceProductDto, id);
	}

	@GetMapping("/{id}")
        @PreAuthorize("hasRole('ROLE_PRODUCT_VIEW')")
	public Response<LicenceProductMaxDto> findLicenseProductById(@PathVariable(name = "id") Long id) {
		return service.findLicenseProductById(id);
	}

	@PutMapping("/de-activate/{id}")
        @PreAuthorize("hasRole('ROLE_PRODUCT_DELETE')")
	public Response<EntityModel<LicenceProduct>> activateDeactivateLicenseProductById(
			@PathVariable(name = "id") Long id) {
		return service.activateDeactivateLicenseProductById(id);
	}

	@GetMapping("/list")
        @PreAuthorize("hasRole('ROLE_PRODUCT_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<LicenceProduct>>> getListOfLicenseProducts(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return service.getListOfLicenseProducts(keyword, pageable);
	}

	@GetMapping("/all")
        @PreAuthorize("hasRole('ROLE_PRODUCT_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<LicenceProduct>>> getAllLicenseProducts() {
		return service.getAllLicenseProducts();
	}

}
