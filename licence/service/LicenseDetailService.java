package tz.go.tcra.lims.licence.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import tz.go.tcra.lims.entity.LicenceServiceDetail;
import tz.go.tcra.lims.licence.dto.LicenseDetailDto;
import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;
import tz.go.tcra.lims.utils.Response;

public interface LicenseDetailService {

	Response<EntityModel<LicenceServiceDetail>> saveLicenseDetail(LicenseDetailDto licenseDetailDto, Long id);

	Response<EntityModel<LicenceServiceDetail>> findLicenseDetailById(Long id);

	Response<EntityModel<LicenceServiceDetail>> deleteLicenseDetailById(Long id);

	Response<CollectionModel<EntityModel<LicenceServiceDetail>>> getListOfLicenseDetail(String keyword,
			Pageable pageable);

	LicenseDetailMaxDto composeLicenceService(LicenceServiceDetail data);
}
