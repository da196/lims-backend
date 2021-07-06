package tz.go.tcra.lims.reports.assemblers;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.reports.controller.LicenseReportsController;
import tz.go.tcra.lims.reports.dto.LicenceGeneralStatsDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author DonaldSj
 */

@Component
public class LicenceGeneralStatsAssembler implements RepresentationModelAssembler<LicenceGeneralStatsDto, EntityModel<LicenceGeneralStatsDto>> {
    @Override
    public EntityModel<LicenceGeneralStatsDto> toModel(LicenceGeneralStatsDto licenceGeneralStatsDto) {
        return EntityModel.of(licenceGeneralStatsDto,
                linkTo(methodOn(LicenseReportsController.class).findLicenseDetailById()).withSelfRel(),
                linkTo(methodOn(LicenseReportsController.class).getAllLicenceGeneralReport("", Pageable.unpaged())).withRel("licenseGeneralReports"));
    }

    @Override
    public CollectionModel<EntityModel<LicenceGeneralStatsDto>> toCollectionModel(Iterable<? extends LicenceGeneralStatsDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
