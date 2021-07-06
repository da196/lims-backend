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

import tz.go.tcra.lims.product.entity.EntityProduct;
import tz.go.tcra.lims.product.dto.EntityProductDto;
import tz.go.tcra.lims.product.dto.EntityProductMaxDto;
import tz.go.tcra.lims.product.service.EntityProductService;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/entity-products")
public class EntityProductController {

	@Autowired
	private EntityProductService service;

	@PostMapping("/save")
        @PreAuthorize("hasRole('ROLE_PRODUCT_SAVE')")
	public Response<EntityModel<EntityProduct>> saveEntityProduct(
			@Valid @RequestBody EntityProductDto data) {
		return service.saveEntityProduct(data, 0L);
	}

	@PutMapping("/update/{id}")
        @PreAuthorize("hasRole('ROLE_PRODUCT_EDIT')")
	public Response<EntityModel<EntityProduct>> updateEntityProduct(@PathVariable(name = "id") Long id,
			@Valid @RequestBody EntityProductDto data) {
		return service.saveEntityProduct(data, id);
	}

	@GetMapping("/{id}")
        @PreAuthorize("hasRole('ROLE_PRODUCT_VIEW')")
	public Response<EntityProductMaxDto> findEntityProductById(@PathVariable(name = "id") Long id) {
		return service.findEntityProductById(id);
	}

	@PutMapping("/de-activate/{id}")
        @PreAuthorize("hasRole('ROLE_PRODUCT_DELETE')")
	public Response<EntityModel<EntityProduct>> activateDeactivateEntityProductById(
			@PathVariable(name = "id") Long id) {
		return service.activateDeactivateEntityProductById(id);
	}

	@GetMapping("/list")
        @PreAuthorize("hasRole('ROLE_PRODUCT_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<EntityProduct>>> getListOfEntityProducts(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return service.getListOfEntityProducts(keyword, pageable);
	}

	@GetMapping("/all")
        @PreAuthorize("hasRole('ROLE_PRODUCT_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<EntityProduct>>> getAllEntityProducts() {
		return service.getAllEntityProducts();
	}

}
