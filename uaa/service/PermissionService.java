/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.uaa.dto.PermissionMinDto;

import tz.go.tcra.lims.uaa.entity.Permission;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface PermissionService {

    Response<CollectionModel<EntityModel<Permission>>> listAll(int page,int size,String sortName,String sortType);
    Response<EntityModel<Permission>> details(Long id);
    Response<CollectionModel<EntityModel<Permission>>> getPermissionByGroupName(String groupName);
    PermissionMinDto getPermissionMinDto(Long id);
}