/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.miscellaneous.entity.Form;
import tz.go.tcra.lims.miscellaneous.entity.FormItem;
import tz.go.tcra.lims.miscellaneous.dto.FormDto;
import tz.go.tcra.lims.miscellaneous.dto.FormItemDto;
import tz.go.tcra.lims.miscellaneous.dto.FormItemMaxDto;
import tz.go.tcra.lims.miscellaneous.dto.FormItemMinDto;
import tz.go.tcra.lims.miscellaneous.dto.FormMaxDto;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface FormService {
    
    Response<EntityModel<Form>> saveForm(FormDto data,Long id);
    Response<CollectionModel<EntityModel<Form>>> listForms();
    Response<CollectionModel<EntityModel<Form>>> listAllForms();
    Response<FormMaxDto> getFormDetails(Long id);
    Response<EntityModel<Form>> activateDeactivateForm(Long id);
    List<FormItemMinDto> getFormItemsMinDto(Long formId);
    Response<List<FormItemMinDto>> getFormItems(Long formId);
    Response<List<FormItem>> saveFormItems(List<FormItemDto> data,Long id);
    Response<FormItemMaxDto> getFormItem(Long formItemId);
    Response<FormItem> saveFormItem(FormItemDto data,Long formId);
}
