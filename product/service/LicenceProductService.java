package tz.go.tcra.lims.product.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.product.dto.LicenceProductDto;
import tz.go.tcra.lims.product.dto.LicenceProductMaxDto;
import tz.go.tcra.lims.utils.Response;

/**
 * @author DonaldSj
 */

public interface LicenceProductService {

	Response<EntityModel<LicenceProduct>> saveLicenseProduct(LicenceProductDto data, Long id);

	Response<LicenceProductMaxDto> findLicenseProductById(Long id);

	Response<EntityModel<LicenceProduct>> activateDeactivateLicenseProductById(Long id);

	Response<CollectionModel<EntityModel<LicenceProduct>>> getListOfLicenseProducts(String keyword, Pageable pageable);

	Response<CollectionModel<EntityModel<LicenceProduct>>> getAllLicenseProducts();
}
