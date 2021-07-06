package tz.go.tcra.lims.reports.assemblers;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.entity.views.LicenceView;
import tz.go.tcra.lims.reports.controller.LicenseReportsController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author DonaldSj
 */

@Component
public class LicenceGeneralReportAssembler implements RepresentationModelAssembler<LicenceView, EntityModel<LicenceView>> {
    @Override
    public EntityModel<LicenceView> toModel(LicenceView licenceGeneralReports) {
        return EntityModel.of(licenceGeneralReports,
                linkTo(methodOn(LicenseReportsController.class).findLicenseDetailById()).withSelfRel(),
                linkTo(methodOn(LicenseReportsController.class).getAllLicenceGeneralReport("", Pageable.unpaged())).withRel("licenseGeneralReports"));
    }

    @Override
    public CollectionModel<EntityModel<LicenceView>> toCollectionModel(Iterable<? extends LicenceView> licenceGeneralReports) {
        return StreamSupport.stream(licenceGeneralReports.spliterator(), false).map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
