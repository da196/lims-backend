/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.licencee.dto.EntityApplicationMaxDto;
import tz.go.tcra.lims.licencee.dto.EntityApplicationMinDto;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface EntityApplicationService {
    
    Response<EntityApplicationMaxDto> findEntityApplicationById(Long id);
    
    Response<CollectionModel<EntityModel<EntityApplicationMinDto>>> getListOfAllEntityApplications(String keyword,
			Pageable pageable);
}
