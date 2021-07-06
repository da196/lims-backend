package tz.go.tcra.lims.reports.assemblers;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.entity.views.LicenceApplicationView;
import tz.go.tcra.lims.reports.controller.LicenseReportsController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author DonaldSj
 */

@Component
public class LicenceApplicationReportAssembler implements RepresentationModelAssembler<LicenceApplicationView, EntityModel<LicenceApplicationView>> {
    @Override
    public EntityModel<LicenceApplicationView> toModel(LicenceApplicationView licenceApplications) {
        return EntityModel.of(licenceApplications,
                linkTo(methodOn(LicenseReportsController.class).findLicenseDetailById()).withSelfRel(),
                linkTo(methodOn(LicenseReportsController.class).getAllLicenceApplicationsReport("all", Pageable.unpaged())).withRel("licenceApplications"));
    }

    @Override
    public CollectionModel<EntityModel<LicenceApplicationView>> toCollectionModel(Iterable<? extends LicenceApplicationView> licenceApplications) {
        return StreamSupport.stream(licenceApplications.spliterator(), false).map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    }
}
