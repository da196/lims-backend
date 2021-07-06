package tz.go.tcra.lims.reports.assemblers;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.reports.controller.LicenseReportsController;
import tz.go.tcra.lims.reports.dto.LicenceApplicationStatsDto;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author DonaldSj
 */

@Component
public class LicenceApplicationsStatsAssembler implements RepresentationModelAssembler<LicenceApplicationStatsDto, EntityModel<LicenceApplicationStatsDto>> {
    @Override
    public EntityModel<LicenceApplicationStatsDto> toModel(LicenceApplicationStatsDto licenceApplicationStats) {
        return EntityModel.of(licenceApplicationStats,
                linkTo(methodOn(LicenseReportsController.class).findLicenseDetailById()).withSelfRel(),
                linkTo(methodOn(LicenseReportsController.class).getAllLicenceApplicationsReport("all", Pageable.unpaged())).withRel("licenceApplications"));
    }

    @Override
    public CollectionModel<EntityModel<LicenceApplicationStatsDto>> toCollectionModel(Iterable<? extends LicenceApplicationStatsDto> licenceApplicationStats) {
        return StreamSupport.stream(licenceApplicationStats.spliterator(), false).map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
