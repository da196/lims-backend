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

import tz.go.tcra.lims.licencee.dto.LicenceeEntityMinDto;
import tz.go.tcra.lims.licencee.dto.LicenceeNotificationMaxDto;
import tz.go.tcra.lims.licencee.dto.LicenceeNotificationMinDto;
import tz.go.tcra.lims.licencee.service.LicenceeEntityService;
import tz.go.tcra.lims.uaa.dto.ProfileMaxDto;
import tz.go.tcra.lims.utils.Response;

/**
 * @author emmanuel.mfikwa
 */

@RestController
@RequestMapping(value = "/v1/licencee-entities")
public class LicenceeEntityController {

    @Autowired
    private LicenceeEntityService service;

    @GetMapping("/{entityId}")
    @PreAuthorize("hasRole('ROLE_ENTITY_VIEW_DETAILS')")
    public Response<ProfileMaxDto> findProfileByEntityId(@PathVariable(name = "entityId") Long id) {

        return service.findProfileByEntityId(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ENTITY_VIEW_ALL')")
    public Response<CollectionModel<EntityModel<LicenceeEntityMinDto>>> getListOfAllEntities(
            @RequestParam(name = "keyword", defaultValue = "All") String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

            return service.listAllEntities(keyword,pageable);
    }
    
    @GetMapping("/list-active")
    public Response<CollectionModel<EntityModel<LicenceeEntityMinDto>>> getListOfAllActiveEntities(
            @RequestParam(name = "keyword", defaultValue = "All") String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

            return service.listActiveEntities(keyword,pageable);
    }
    
    @GetMapping("/notifications")
    @PreAuthorize("hasRole('ROLE_ENTITY_NOTIFICATION_VIEW_ALL')")
    public Response<CollectionModel<EntityModel<LicenceeNotificationMinDto>>> getListOfAllNotifications(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

            return service.listAllNotification(keyword,pageable);
    }
    
    @GetMapping("/notifications/{id}")
    @PreAuthorize("hasRole('ROLE_ENTITY_NOTIFICATION_VIEW')")
    public Response<LicenceeNotificationMaxDto> getNotification(@PathVariable("id") Long id) {

            return service.findNotificationById(id);
    }
}
