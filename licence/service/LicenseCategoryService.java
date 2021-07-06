package tz.go.tcra.lims.licence.service;

import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.licence.dto.LicenseCategoryDto;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMaxDto;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;

/**
 * @author DonaldSj
 */

public interface LicenseCategoryService {

	Response<EntityModel<LicenceCategory>> saveLicenseCategory(LicenseCategoryDto licenseCategoryDto,Long id);

        Response<EntityModel<LicenceCategory>> activateDeactivateLicenseCategoryById(Long id);
        
	Response<EntityModel<LicenceCategory>> findLicenseCategoryById(Long id);	

	Response<List<LicenseCategoryMaxDto>> getListOfLicenseCategories(Long parentId,String flag);

	Response<CollectionModel<EntityModel<LicenceCategory>>> getListOfLicenseCategory();
        
	Response<List<LicenseCategoryMinDto>> getLicenseCategoriesMin();

	Response<CollectionModel<EntityModel<LicenceCategory>>> getListOfLicenseCategoryByFlag(String flag);
        
        List<LicenceCategory> getListOfLicenceCategoryTopHierachyByCategoryId(Long id);
        
        LicenseCategoryMinDto composeLicenseCategoryMinDto(LicenceCategory data);
        
        Long getRootLicenceCategoryByProduct(LicenceProduct data) throws DataNotFoundException,Exception;
        
        LicenceCategory getLevel1LicenceCategoryByProduct(LicenceProduct data) throws DataNotFoundException,Exception;
}
