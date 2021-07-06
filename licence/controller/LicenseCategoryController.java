package tz.go.tcra.lims.licence.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.licence.dto.LicenseCategoryDto;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMaxDto;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto;
import tz.go.tcra.lims.licence.service.LicenseCategoryService;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "v1/license-categories")
public class LicenseCategoryController {

	@Autowired
	private LicenseCategoryService licenseCategoryService;

	@PostMapping("/save")
	@PreAuthorize("hasRole('ROLE_LICENSE_CATEGORY_SAVE')")
	public Response<EntityModel<LicenceCategory>> saveLicenseCategory(
			@Valid @RequestBody LicenseCategoryDto licenseCategoryDto) {
            
            return licenseCategoryService.saveLicenseCategory(licenseCategoryDto,0L);
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ROLE_LICENSE_CATEGORY_EDIT')")
	public Response<EntityModel<LicenceCategory>> updateLicenseCategory(@Valid @RequestBody LicenseCategoryDto licenseCategoryDto, 
                @PathVariable("id") Long id) {
            
            return licenseCategoryService.saveLicenseCategory(licenseCategoryDto, id);
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_LICENSE_CATEGORY_VIEW')")
	public Response<EntityModel<LicenceCategory>> findLicenseCategoryById(
                @PathVariable("id") Long id) {
            
            return licenseCategoryService.findLicenseCategoryById(id);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_LICENSE_CATEGORY_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<LicenceCategory>>> getListOfLicenseCategory() {
            
		return licenseCategoryService.getListOfLicenseCategory();
	}

	@PutMapping("/de-activate/{id}")
	@PreAuthorize("hasRole('ROLE_LICENSE_CATEGORY_ACTIVATION_DEACTIVATION')")
	public Response<EntityModel<LicenceCategory>> activateDeactivateLicenseCategoryById(
                @PathVariable("id") Long id) {
            
            return licenseCategoryService.activateDeactivateLicenseCategoryById(id);
	}

	@GetMapping("/list-query")
	@PreAuthorize("hasRole('ROLE_LICENSE_CATEGORY_VIEW')")
	public Response<List<LicenseCategoryMaxDto>> getListOfLicenseCategories(
                @RequestParam(name = "parent",defaultValue="0") Long parentId,
                @RequestParam(name = "flag",defaultValue="") String flag) {
            
            return licenseCategoryService.getListOfLicenseCategories(parentId,flag);
	}

	@GetMapping(value = "/list-min")
	@PreAuthorize("hasRole('ROLE_LICENSE_CATEGORY_VIEW_ALL')")
	public Response<List<LicenseCategoryMinDto>> getListOfLicenseCategories() {
            
		return licenseCategoryService.getLicenseCategoriesMin();
	}

}