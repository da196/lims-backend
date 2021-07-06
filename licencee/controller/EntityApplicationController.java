package tz.go.tcra.lims.licencee.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.licencee.dto.EntityApplicationMaxDto;
import tz.go.tcra.lims.licencee.dto.EntityApplicationMinDto;
import tz.go.tcra.lims.licencee.service.EntityApplicationService;
import tz.go.tcra.lims.utils.Response;

/**
 * @author emmanuel.mfikwa
 */

@RestController
@RequestMapping(value = "/v1/entity-applications")
public class EntityApplicationController {

    @Autowired
    private EntityApplicationService service;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ENTITY_APPLICATION_VIEW_DETAILS')")
    public Response<EntityApplicationMaxDto> findEntityApplicationById(@PathVariable(name = "id") Long id) {

        return service.findEntityApplicationById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ENTITY_APPLICATION_VIEW_ALL')")
    public Response<CollectionModel<EntityModel<EntityApplicationMinDto>>> getListOfAllEntityApplications(
            @RequestParam(name = "keyword", defaultValue = "All") String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

            return service.getListOfAllEntityApplications(keyword,pageable);
    }
}
