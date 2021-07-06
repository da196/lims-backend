/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.miscellaneous.dto.StatusDto;
import tz.go.tcra.lims.miscellaneous.dto.StatusMinDto;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface StatusService {

	Response<EntityModel<Status>> saveStatus(StatusDto data, Long id);

	Response<List<StatusMinDto>> listStatus();

	Response<CollectionModel<EntityModel<Status>>> listAllStatus(String keyword, Pageable pageable);

	Response<EntityModel<Status>> getStatusDetails(Long id);

	Response<EntityModel<Status>> activateDeactivateStatus(Long id);
}
