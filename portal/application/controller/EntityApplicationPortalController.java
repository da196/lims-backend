package tz.go.tcra.lims.portal.application.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.miscellaneous.enums.EntityApplicationTypeEnum;
import tz.go.tcra.lims.portal.application.dto.TaskActionPortalDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMaxPortalDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMinPortalDto;
import tz.go.tcra.lims.portal.application.dto.LicenceeEntityMinMinPortalDto;
import tz.go.tcra.lims.portal.application.dto.LicenceeEntityMinPortalDto;
import tz.go.tcra.lims.portal.application.services.EntityApplicationPortalService;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.SaveResponseDto;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/p/entity-application")
public class EntityApplicationPortalController {

    @Autowired
    private EntityApplicationPortalService service;

    @PostMapping("/save")
    public Response<EntityApplicationMaxPortalDto> saveApplication(
            @Valid @RequestBody EntityApplicationDto data,
            @RequestParam("type") EntityApplicationTypeEnum type) {

        return service.saveEntityApplication(data, 0L,type);
    }

    @PutMapping("/update/{id}")
    public Response<EntityApplicationMaxPortalDto> updateApplication(
            @PathVariable("id") Long id,
            @Valid @RequestBody EntityApplicationDto data,
            @RequestParam("type") EntityApplicationTypeEnum type) {

        return service.saveEntityApplication(data, id,type);
    }

    @GetMapping("/list")
    public Response<List<EntityApplicationMinPortalDto>> listEntityApplication() {

        return service.getEntityApplicationByApplicant();
    }

    @GetMapping("/{id}")
    public Response<EntityApplicationMaxPortalDto> getEntityApplicationById(@PathVariable("id") Long id) {

        return service.findEntityApplicationById(id);
    }
    
    @PostMapping("/save-activity/{id}")
    public Response<SaveResponseDto> savePortalActivity(
            @PathVariable("id") Long id,
            @RequestBody TaskActionPortalDto data) {

        return service.savePortalActivity(data, id);
    }
    
    @GetMapping("/list-entities")
    public Response<List<LicenceeEntityMinMinPortalDto>> listEntities() {

        return service.getListOfEntities();
    }
    
    @GetMapping("/entities/{id}")
    public Response<LicenceeEntityMinPortalDto> findEntityById(@PathVariable("id") Long id) {

        return service.findLicenceeById(id);
    }
}
