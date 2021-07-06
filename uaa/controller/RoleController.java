/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.controller;

import javax.validation.Valid;

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
import tz.go.tcra.lims.uaa.dto.RoleDto;
import tz.go.tcra.lims.uaa.dto.RoleMaxDto;
import tz.go.tcra.lims.uaa.dto.RolePermissionsDto;
import tz.go.tcra.lims.uaa.entity.Role;
import tz.go.tcra.lims.uaa.service.RoleService;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
@RestController
@RequestMapping("/v1/role")
public class RoleController {

    @Autowired
    private RoleService service;

    @GetMapping("/list")
    public Response<CollectionModel<EntityModel<Role>>> list(){

        return service.listAll();
    }

    @GetMapping("/single/{id}")
    public Response<EntityModel<RoleMaxDto>> details(@PathVariable("id") Long id){

        return service.details(id);
    }

    @PostMapping("/save")
    public Response<EntityModel<Role>> save(@Valid @RequestBody RoleDto data){
        return service.save(data, 0L);
    }

    @PutMapping("/save/{id}")
    public Response<EntityModel<Role>> save(@PathVariable("id") Long id,@Valid @RequestBody RoleDto data){
        return service.save(data, id);
    }

    @PostMapping("/save-permissions")
    public Response<EntityModel<Role>> save(@Valid @RequestBody RolePermissionsDto data){
        return service.savePermissions(data);
    }
}
