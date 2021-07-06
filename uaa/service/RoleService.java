/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import tz.go.tcra.lims.uaa.dto.*;
import tz.go.tcra.lims.uaa.entity.Role;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface RoleService {

    Response<CollectionModel<EntityModel<Role>>> listAll();
    Response<EntityModel<RoleMaxDto>> details(Long id);
    Response<EntityModel<Role>> save(RoleDto data, Long id);
    Response<EntityModel<Role>> savePermissions(RolePermissionsDto data);
    RoleMinDto getRoleMinDto(Long id);
}
