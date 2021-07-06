/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.controller;

import java.util.List;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.uaa.dto.EntityCategoryDto;
import tz.go.tcra.lims.licencee.entity.EntityCategory;
import tz.go.tcra.lims.uaa.service.EntityCategoryService;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
@RestController
@RequestMapping("/v1/entity-category")
public class EntityCategoryController {
    
    @Autowired
    private EntityCategoryService service;

    @GetMapping("/{id}")
    public Response<EntityModel<EntityCategory>> findById(@PathVariable long id) throws NotFoundException {
        return service.findEntityCategoryById(id);
    }

    @GetMapping(value = "/list")
    public Response<CollectionModel<EntityModel<EntityCategory>>> getListOfEntityCategories() {
        return service.getListOfEntityCategory();
    }
    
    @PostMapping(value = "/save")
    public Response<EntityModel<EntityCategory>> saveEntityCategory(@RequestBody EntityCategoryDto data) {
        
        return service.saveEntityCategory(data, 0L);
    }
    
    @PutMapping(value = "/save/{id}")
    public Response<EntityModel<EntityCategory>> updateEntityCategory(@PathVariable("id") Long id,@RequestBody EntityCategoryDto data) {
        
        return service.saveEntityCategory(data, id);
    }
    
    @GetMapping("/licencee/{id}")
    public EntityCategory details(@PathVariable long id) {
        
        return service.details(id);
    }

    @GetMapping(value = "/licencee/list")
    public List<EntityCategory> listEntityCategories() {
        
        return service.getEntityCategoryByStatus(true);
    }
}
