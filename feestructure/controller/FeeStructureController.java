package tz.go.tcra.lims.feestructure.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.feestructure.dto.FeeDto;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.feestructure.service.FeeStructureService;
import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/license-fees")
public class FeeStructureController {

    @Autowired
    private FeeStructureService service;

    @GetMapping(value = "/{feeId}")
    @PreAuthorize("hasRole('ROLE_FEE_STRUCTURE_VIEW')")
    public Response<EntityModel<FeeStructure>> findFeeStructureById(@PathVariable(name = "feeId") Long feeId) {
        return service.findFeeStructureById(feeId);
    }

    @GetMapping("list")
    @PreAuthorize("hasRole('ROLE_FEE_STRUCTURE_VIEW_ALL')")
    public Response<CollectionModel<EntityModel<FeeStructure>>> getListOfAllFeeStructures(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return service.getListOfAllFeeStructures(keyword,pageable);
    }
    
    @GetMapping("list-by-feable-type")
    @PreAuthorize("hasRole('ROLE_FEE_STRUCTURE_VIEW_ALL')")
    public Response<List<FeeStructure>> getListOfAllFeeStructures(@RequestParam("feeableType") FeeableTypeEnum type) {
        
        return service.getListOfAllFeeStructuresByFeableType(type);
    }
    
    @PostMapping("save")
    @PreAuthorize("hasRole('ROLE_FEE_STRUCTURE_CREATE')")
    public Response<EntityModel<FeeStructure>> saveFeeStructures(@Valid @RequestBody FeeDto data) {
        
        return service.saveFeeStructure(data,0L);
    }
    
    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ROLE_FEE_STRUCTURE_EDIT')")
    public Response<EntityModel<FeeStructure>> updateFeeStructures(
            @PathVariable("id") Long id,
            @Valid @RequestBody FeeDto data) {
        
        return service.saveFeeStructure(data, id);
    }
    
    @PutMapping("/de-activate/{id}")
    @PreAuthorize("hasRole('ROLE_PRODUCT_DELETE')")
    public Response<EntityModel<FeeStructure>> activateDeactivateFeeStructureById(
                    @PathVariable(name = "id") Long id) {
            return service.activateDeactivateFeeStructureById(id);
    }
}
