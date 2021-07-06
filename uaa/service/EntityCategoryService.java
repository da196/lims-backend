/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.uaa.dto.EntityCategoryDto;
import tz.go.tcra.lims.licencee.entity.EntityCategory;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface EntityCategoryService {
    
    public Response<EntityModel<EntityCategory>> saveEntityCategory(EntityCategoryDto entityCategoryDto,Long id);

    public Response<EntityModel<EntityCategory>> findEntityCategoryById(long id);

    public Response<CollectionModel<EntityModel<EntityCategory>>> getListOfEntityCategory(); 
    
    EntityCategory details(long id);

    List<EntityCategory> getEntityCategoryByStatus(boolean status);    
}
