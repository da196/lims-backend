package tz.go.tcra.lims.portal.application.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tz.go.tcra.lims.licence.dto.LicenceCancellationDto;

import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;
import tz.go.tcra.lims.miscellaneous.enums.LicenceApplicationStateEnum;
import tz.go.tcra.lims.payment.dto.BillingChargesDto;
import tz.go.tcra.lims.payment.dto.BillingReceiptDto;
import tz.go.tcra.lims.payment.dto.ControlNumberAvailability;
import tz.go.tcra.lims.portal.application.dto.TaskActionPortalDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMaxPortalDto;
import tz.go.tcra.lims.portal.application.dto.EntityApplicationMinPortalDto;
import tz.go.tcra.lims.portal.application.dto.IndividualLicenseApplicationDto;
import tz.go.tcra.lims.portal.application.dto.LicencePortalMaxDto;
import tz.go.tcra.lims.portal.application.dto.LicencePortalMinDto;
import tz.go.tcra.lims.portal.application.dto.LicenseCategoryDto;
import tz.go.tcra.lims.portal.application.dto.PayableFeesDto;
import tz.go.tcra.lims.portal.application.dto.PresentationPortalDto;
import tz.go.tcra.lims.portal.application.dto.PresentationPortalRequestDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.portal.application.services.ApplicationService;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/p/license")
public class LicenseApplicationController {

    @Autowired
    private ApplicationService service;

    @GetMapping("/category-services")
    public Response<List<LicenseDetailMaxDto>> getListOfLicenseCategoryServices(
                    @RequestParam(name = "categoryId") Long categoryId) {
            return service.getLicenceCategoryServices(categoryId);
    }

    @GetMapping("/category-list")
    public Response<List<LicenseCategoryDto>> getListOfLicenseCategories(
                    @RequestParam(name = "parent", defaultValue = "0") Long parent) {
            return service.getLicenseCategoriesByParent(parent);
    }

    @PostMapping("/apply")
    public Response<SaveResponseDto> saveLicenseApplication(@Valid @RequestBody IndividualLicenseApplicationDto data,
                    @RequestParam(name = "state", defaultValue = "NEW") LicenceApplicationStateEnum state) {
            return service.saveLicenseApplication(data, 0L, state);
    }

    @PutMapping("/update/{id}")
    public Response<SaveResponseDto> updateLicenseApplication(@PathVariable(name = "id") Long id,
                    @Valid @RequestBody IndividualLicenseApplicationDto data,
                    @RequestParam(name = "state", defaultValue = "NEW") LicenceApplicationStateEnum state) {
            return service.saveLicenseApplication(data, id, state);
    }

    @PutMapping("/submit/{id}")
    public Response<LicencePortalMaxDto> submitLicenseApplication(@PathVariable(name = "id") Long id,
                    @Valid @RequestBody IndividualLicenseApplicationDto data,
                    @RequestParam(name = "state", defaultValue = "NEW") LicenceApplicationStateEnum state) {
            return service.submitLicenseApplication(data, id, state);
    }

    @GetMapping("/my-applications")
    public Response<List<LicencePortalMinDto>> getLicenseByApplicant() {
            return service.getLicenseByApplicant();
    }

    @GetMapping("/{id}")
    public Response<LicencePortalMaxDto> getLicenseById(@PathVariable("id") Long id) {

            return service.getLicenseById(id);
    }

    @GetMapping("/category-id/{id}")
    public Response<List<PayableFeesDto>> getListOfLicenseFeeStructures(@PathVariable("id") Long id) {

            return service.getLicenceCategoryFeeStructure(id);
    }

    @GetMapping(value = "/my-billing")
    public Response<List<LicencePortalMinDto>> getLicenseBillingByApplicant() {
            return service.getLicenseBillingByApplicant();
    }

    @GetMapping(value = "/my-charges/{invoiceNumber}")
    public Response<List<BillingChargesDto>> getBillingChargesByInvoiceNumber(
                    @PathVariable("invoiceNumber") String invoinceNumber) {

            return service.getBillingChargesByInvoiceNumber(invoinceNumber);
    }

    @GetMapping(value = "/my-invoice/{invoiceNumber}")
    public Response<LicencePortalMinDto> getLicenseBillingByInvoiceNumber(
                    @PathVariable("invoiceNumber") String invoinceNumber) {

            return service.getLicenseBillingByInvoiceNumber(invoinceNumber);

    }

    @GetMapping(value = "/my-billing/{controlNumberAvailablility}")
    public Response<List<LicencePortalMinDto>> getLicenseBillingByApplicantBasedOnControlNoGivenOrNot(
                    @PathVariable("controlNumberAvailablility") ControlNumberAvailability billingStatusEnum) {
            return service.getLicenseBillingByApplicantBasedOnControlNoGivenOrNot(billingStatusEnum);
    }

    @GetMapping(value = "/my-receipt/{invoiceNumber}")
    public Response<BillingReceiptDto> getBillingReceByInvoiceNumber(
                    @PathVariable("invoiceNumber") String invoinceNumber) {
            return service.getBillingReceiptByInvoiceNumber(invoinceNumber);
    }

    @GetMapping(value = "/view-presentations")
    public Response<List<PresentationPortalDto>> viewPresentations() {

            return service.viewPresentations();
    }

    @PostMapping(value = "/save-presentation")
    public Response<SaveResponseDto> saveLicencePresentation(@Valid @RequestBody PresentationPortalRequestDto data) {

            return service.savePresentation(data);
    }

    @GetMapping(value = "/view-resubmitted-applications")
    public Response<List<LicencePortalMinDto>> getLicenseResubmittedByApplicant() {
            return service.getLicenseResubmittedByApplicant();
    }

    @PostMapping(value = "/initiate-cancellation")
    public Response<SaveResponseDto> initiateLicenceCancellation(@Valid @RequestBody LicenceCancellationDto data) {

        return service.licenceCancellation(data);
    }
}
