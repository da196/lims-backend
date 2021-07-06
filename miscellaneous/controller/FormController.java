package tz.go.tcra.lims.miscellaneous.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
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
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.miscellaneous.entity.Form;
import tz.go.tcra.lims.miscellaneous.entity.FormItem;
import tz.go.tcra.lims.miscellaneous.dto.FormDto;
import tz.go.tcra.lims.miscellaneous.dto.FormItemDto;
import tz.go.tcra.lims.miscellaneous.dto.FormItemMaxDto;
import tz.go.tcra.lims.miscellaneous.dto.FormItemMinDto;
import tz.go.tcra.lims.miscellaneous.dto.FormMaxDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.miscellaneous.service.FormService;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "v1/forms")
public class FormController {

	@Autowired
	private FormService service;

	@PostMapping("/save")
	@PreAuthorize("hasRole('ROLE_FORM_SAVE')")
	public Response<EntityModel<Form>> saveForm(@Valid @RequestBody FormDto data) {
            
            return service.saveForm(data,0L);
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ROLE_FORM_EDIT')")
	public Response<EntityModel<Form>> updateForm(@Valid @RequestBody FormDto data, 
                @PathVariable("id") Long id) {
            
            return service.saveForm(data, id);
	}

        @PostMapping("/save-item/{formId}")
	@PreAuthorize("hasRole('ROLE_FORM_ITEM_SAVE')")
	public Response<List<FormItem>> saveFormItem(
			@Valid @RequestBody List<FormItemDto> data,
                        @Min(1) @PathVariable("formId") Long id) {
            
            return service.saveFormItems(data,id);
	}
        
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_FORM_VIEW')")
	public Response<FormMaxDto> findFormById(
                @PathVariable("id") Long id) {
            
            return service.getFormDetails(id);
	}

        @GetMapping(value = "/form-item/{formItemid}")
	@PreAuthorize("hasRole('ROLE_FORM_VIEW')")
	public Response<FormItemMaxDto> findFormItemById(@PathVariable("formItemid") Long id) {
            
            return service.getFormItem(id);
	}
        
        @PutMapping("/form-item/update/{id}")
	@PreAuthorize("hasRole('ROLE_FORM_EDIT')")
	public Response<FormItem> updateFormItem(@Valid @RequestBody FormItemDto data, 
                @PathVariable("formId") Long id) {
            
            return service.saveFormItem(data, id);
	}
        
        @PostMapping("/list-items/{formId}")
	@PreAuthorize("hasRole('ROLE_FORM_ITEM_VIEW')")
	public Response<List<FormItemMinDto>> getNonParameterFormItems(
                @PathVariable("formId") Long id) {
            
            return service.getFormItems(id);
	}
        
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_FORM_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<Form>>> getListOfForms() {
            
		return service.listForms();
	}

	@PutMapping("/de-activate/{id}")
	@PreAuthorize("hasRole('ROLE_FORM_ACTIVATION_DEACTIVATION')")
	public Response<EntityModel<Form>> activateDeactivateFormById(
                @PathVariable("id") Long id) {
            
            return service.activateDeactivateForm(id);
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasRole('ROLE_FORM_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<Form>>> getListOfAllForms() {
            
            return service.listAllForms();
	}

}