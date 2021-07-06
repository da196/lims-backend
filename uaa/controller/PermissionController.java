/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.uaa.entity.Permission;
import tz.go.tcra.lims.uaa.service.PermissionService;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
@RestController
@RequestMapping("/v1/permission")
public class PermissionController {
    
    @Autowired
    private PermissionService service;
    
    @GetMapping("/list")
    public Response<CollectionModel<EntityModel<Permission>>> list(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "15") int size,
            @RequestParam(name = "sort_name", defaultValue = "id") String sortName,
            @RequestParam(name = "sort_type", defaultValue = "DESC") String sortType){
       
        return service.listAll(page,size,sortName,sortType);
    }
    
    @GetMapping("/list-group")
    public Response<CollectionModel<EntityModel<Permission>>> listByGroup(@RequestParam("group") String group){
       
        return service.getPermissionByGroupName(group);
    }
    
    @GetMapping("/{id}")
    public Response<EntityModel<Permission>> details(@PathVariable("id") Long id){
       
        return service.details(id);
    }
}
