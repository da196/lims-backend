package tz.go.tcra.lims.portal.application.services;

import java.util.List;
import java.util.Set;

import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.entity.LicenceApplicationEntity;
import tz.go.tcra.lims.licence.dto.LicenceCancellationDto;
import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;
import tz.go.tcra.lims.licence.dto.SpectrumValueDto;
import tz.go.tcra.lims.miscellaneous.enums.LicenceApplicationStateEnum;
import tz.go.tcra.lims.payment.dto.BillingChargesDto;
import tz.go.tcra.lims.payment.dto.BillingReceiptDto;
import tz.go.tcra.lims.payment.dto.ControlNumberAvailability;
import tz.go.tcra.lims.portal.application.dto.IndividualLicenseApplicationDto;
import tz.go.tcra.lims.portal.application.dto.ActivityPortalDto;
import tz.go.tcra.lims.portal.application.dto.LicencePortalMaxDto;
import tz.go.tcra.lims.portal.application.dto.LicencePortalMinDto;
import tz.go.tcra.lims.portal.application.dto.LicenseCategoryDto;
import tz.go.tcra.lims.portal.application.dto.PayableFeesDto;
import tz.go.tcra.lims.portal.application.dto.PresentationPortalDto;
import tz.go.tcra.lims.portal.application.dto.PresentationPortalRequestDto;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;

/**
 * @author DonaldSj
 */

public interface ApplicationService {

    Response<List<LicenseCategoryDto>> getLicenseCategoriesByParent(Long parentId);

    Response<SaveResponseDto> saveLicenseApplication(IndividualLicenseApplicationDto data, Long id,
                    LicenceApplicationStateEnum state);

    Response<LicencePortalMaxDto> submitLicenseApplication(IndividualLicenseApplicationDto data, Long id,
                    LicenceApplicationStateEnum state);

    Response<List<LicencePortalMinDto>> getLicenseByApplicant();

    Response<LicencePortalMaxDto> getLicenseById(Long id);

    Response<List<LicenseDetailMaxDto>> getLicenceCategoryServices(Long categoryId);

    LicenceApplicationEntity saveApplicantEntity(LicenceeEntity entity) throws Exception;

    void saveLicenceServices(List<Long> data, Licence license) throws DataNotFoundException, Exception;

    void saveCoverageAreas(Set<Long> data, Licence license) throws DataNotFoundException, Exception;

    Response<List<PayableFeesDto>> getLicenceCategoryFeeStructure(Long id);

    Response<List<LicencePortalMinDto>> getLicenseBillingByApplicant();

    Response<List<BillingChargesDto>> getBillingChargesByInvoiceNumber(String invoinceNumber);

    Response<LicencePortalMinDto> getLicenseBillingByInvoiceNumber(String invoinceNumber);

    void saveIndividualLicenceDetails(IndividualLicenseApplicationDto data, Licence license)
                    throws DataNotFoundException, Exception;

    Response<List<LicencePortalMinDto>> getLicenseBillingByApplicantBasedOnControlNoGivenOrNot(
                    ControlNumberAvailability billingStatusEnum);

    Response<BillingReceiptDto> getBillingReceiptByInvoiceNumber(String invoinceNumber);

    Response<List<PresentationPortalDto>> viewPresentations();

    ActivityPortalDto composeActivity(TaskActivity activity);

    Response<SaveResponseDto> savePresentation(PresentationPortalRequestDto data);

    String getWorkflowTypeCode(LicenceApplicationStateEnum applicationState);

    Response<SaveResponseDto> licenceCancellation(LicenceCancellationDto data);
    
    void saveSpectrumAcquired(List<SpectrumValueDto> data,Long licenceId) throws Exception;

    Response<List<LicencePortalMinDto>> getLicenseResubmittedByApplicant();
}
