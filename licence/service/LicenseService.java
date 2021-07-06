package tz.go.tcra.lims.licence.service;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;

import tz.go.tcra.lims.entity.IndividualLicenceApplicationDetail;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.entity.LicenceApplicationEntity;
import tz.go.tcra.lims.entity.LicenceApplicationShareholder;
import tz.go.tcra.lims.licence.dto.LicenceCancellationDto;
import tz.go.tcra.lims.licence.dto.LicenceMaxDto;
import tz.go.tcra.lims.licence.dto.LicenseMinDto;
import tz.go.tcra.lims.task.dto.PresentationDto;
import tz.go.tcra.lims.portal.application.dto.ApplicantEntityDto;
import tz.go.tcra.lims.portal.application.dto.IndividualLicenceDto;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

public interface LicenseService {

    Response<LicenceMaxDto> findLicenseById(Long id);

    Response<CollectionModel<EntityModel<LicenseMinDto>>> getListOfNonDraftLicences(int page, int size, String sortName,
                    String sortType, Long productId,Long rootId);

    ApplicantEntityDto composeLicenceApplicationEntity(LicenceApplicationEntity data);

    List<ShareholderDto> composeLicenceApplicationEntityShareholders(Set<LicenceApplicationShareholder> data);

    IndividualLicenceDto composeIndividualLicenceDto(IndividualLicenceApplicationDetail data);

    Response<CollectionModel<EntityModel<LicenseMinDto>>> getListOfAllLicences(int page, int size, String sortName,
			String sortType, Long productId,Long rootId);
    
    Response<CollectionModel<EntityModel<PresentationDto>>> getListOfPresentations(Pageable pageable);
    
    Response<AttachmentMaxDto> getPresentation(Long presentationId);
    
    Response<Licence> licenceCancellation(LicenceCancellationDto data);
}
