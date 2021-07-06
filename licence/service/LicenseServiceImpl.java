package tz.go.tcra.lims.licence.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.entity.IndividualLicenceApplicationContentResource;
import tz.go.tcra.lims.entity.IndividualLicenceApplicationDetail;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.task.entity.TaskActivity;
import tz.go.tcra.lims.entity.LicenceApplicationCoverage;
import tz.go.tcra.lims.entity.LicenceApplicationEntity;
import tz.go.tcra.lims.entity.LicenceApplicationServiceDetail;
import tz.go.tcra.lims.entity.LicenceApplicationShareholder;
import tz.go.tcra.lims.entity.LicenceCategory;
import tz.go.tcra.lims.feestructure.service.FeeStructureService;
import tz.go.tcra.lims.task.entity.LicencePresentation;
import tz.go.tcra.lims.geolocation.dto.GeoLocationMinDto;
import tz.go.tcra.lims.geolocation.service.GeoLocationService;
import tz.go.tcra.lims.licence.dto.LicenceCancellationDto;
import tz.go.tcra.lims.licence.dto.LicenceMaxDto;
import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;
import tz.go.tcra.lims.licence.dto.LicenseMinDto;
import tz.go.tcra.lims.task.dto.PresentationDto;
import tz.go.tcra.lims.licence.repository.LicenceAcquiredSpectrumRepository;
import tz.go.tcra.lims.licence.repository.LicenceApplicationCoverageRepository;
import tz.go.tcra.lims.licence.repository.LicenceApplicationServiceRepository;
import tz.go.tcra.lims.task.repository.LicencePresentationRepository;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueMinDto;
import tz.go.tcra.lims.miscellaneous.enums.LicenceApplicationStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.LicenceStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.miscellaneous.repository.StatusRepository;
import tz.go.tcra.lims.payment.service.BillingService;
import tz.go.tcra.lims.portal.geolocation.service.LocationService;
import tz.go.tcra.lims.portal.application.dto.ApplicantEntityDto;
import tz.go.tcra.lims.portal.application.dto.IndividualLicenceDto;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.uaa.service.UserService;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.utils.exception.OperationNotAllowedException;
import tz.go.tcra.lims.task.repository.TaskActivityRepository;
import tz.go.tcra.lims.task.service.TaskService;
import tz.go.tcra.lims.task.service.TaskStatusHistroyService;
import tz.go.tcra.lims.licence.repository.LicenceApplicationIndividualDetailRepository;
import tz.go.tcra.lims.licence.repository.LicenceRepository;
import tz.go.tcra.lims.portal.application.dto.PayableFeesDto;

/**
 * @author DonaldSj
 */
@Slf4j
@Service
public class LicenseServiceImpl implements LicenseService {

    @Autowired
    private PagedResourcesAssembler<LicenseMinDto> pagedResourcesAssembler;
    
    @Autowired
    private PagedResourcesAssembler<PresentationDto> presentationPagedResourcesAssembler;
    
    @Autowired
    private LicenceRepository licenceRepo;
    
    @Autowired
    private LicenceApplicationServiceRepository licenseApplicationServiceRepo;
    
    @Autowired
    private LicenceApplicationCoverageRepository licenseApplicationCoverageRepo;
    
    @Autowired
    private LicenceApplicationIndividualDetailRepository individualDetailRepository;
    
    @Autowired
    private StatusRepository statusRepo;
     
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private ListOfValueRepository listOfValueRepo;
    
    @Autowired
    private LicencePresentationRepository presentationRepo;
    
    @Autowired
    private LicenceAcquiredSpectrumRepository acquiredSpectrumRepo;
    
    @Autowired
    private TaskActivityRepository activityRepo;
    
    @Autowired
    private BillingService billingService;
    
    @Autowired
    private LocationService locationService;

    @Autowired
    private GeoLocationService geoLocationService;
    
    @Autowired
    private LicenseCategoryService licenseCategoryService;
    
    @Autowired
    private LicenseDetailService licenseDetailService;

    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private FeeStructureService feeService;
    
    @Autowired
    private TaskStatusHistroyService statusHistoryService;
    
    @Value("${lims.licence.cancellation.workflow.type.code}")
    private String cancellationLicenceWorkflowTypeCode;
    
    @Override
    public Response<LicenceMaxDto> findLicenseById(Long id) {
        Response<LicenceMaxDto> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCE DETAILS RETRIEVED SUCCESSFULLY",null);
        try {
             
            Optional<Licence> existing=licenceRepo.findById(id);
            
            if(!existing.isPresent()){
            
                response.setMessage("LICENCE DETAILS NOT FOUND");
                return response;
            }
            
            Licence licence=existing.get();          
            
            //retrieve all the licence services applied for
            List<LicenseDetailMaxDto> services=new ArrayList();
            Set<LicenceApplicationServiceDetail> appliedServices=licenseApplicationServiceRepo.findByLicenseId(licence.getId());
            for(LicenceApplicationServiceDetail appliedService : appliedServices){
            
                services.add(licenseDetailService.composeLicenceService(appliedService.getService()));
            }
            
            //retrieve all the licence coverage areas
            List<GeoLocationMinDto> locations=new ArrayList();
            Set<LicenceApplicationCoverage> appliedCoverages=licenseApplicationCoverageRepo.findByLicenseId(licence.getId());
            for(LicenceApplicationCoverage location : appliedCoverages){
            
                locations.add(geoLocationService.composeGeoLocationMinDto(location.getLocation()));
            }
            
            LicenceMaxDto data=new LicenceMaxDto();
            data.setId(licence.getId());
            data.setIsDraft(licence.getIsDraft());
            data.setIssueDate(licence.getIssuedDate());
            data.setExpireDate(licence.getExpireDate());
            data.setApplicationNumber(licence.getApplicationNumber());
            data.setLicenceNumber(licence.getLicenceNumber());
            data.setLicenseState(licence.getLicenseState().toString());
            data.setApplicationState(licence.getApplicationState().toString());
            data.setProduct(licence.getLicenseProduct().getDisplayName());
            data.setServices(services);
            data.setCoverageAreas(locations);
            data.setAttachments(attachmentService.getAttachmentsMax(licence));
            data.setCreator(userService.composeUserMaxDto(userRepo.getOne(licence.getApplicant())));
            data.setEntity(this.composeLicenceApplicationEntity(licence.getApplicantEntity()));
            
            Optional<IndividualLicenceApplicationDetail> individualLicenseDetail=individualDetailRepository.findByLicense(licence);
            
            if(individualLicenseDetail.isPresent()){
            
                data.setIndividual(this.composeIndividualLicenceDto(individualLicenseDetail.get()));
            }
            
            data.setSubmittedAt(licence.getSubmittedAt());
            data.setCreatedAt(licence.getCreatedAt());
            data.setStatus(statusRepo.getOne(licence.getStatus().getId()).getName());

            // extract category hierachy
            List<LicenceCategory> categoryHierachy = licenseCategoryService
                            .getListOfLicenceCategoryTopHierachyByCategoryId(
                                            licence.getLicenseProduct().getLicenseCategory().getId());

            if (categoryHierachy.size() > 0) {

                    data.setCoverage(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(0)));
            }

            int subCategoryLevelLeaf = categoryHierachy.size() - 4;
            if(subCategoryLevelLeaf > 0){

                data.setSubCategoryLeaf(licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevelLeaf)));
            }

            int subCategoryLevel = categoryHierachy.size() - 3;
            if (subCategoryLevel > 0) {

                    data.setSubCategory(
                                    licenseCategoryService.composeLicenseCategoryMinDto(categoryHierachy.get(subCategoryLevel)));
            }

            int categoryLevel1 = categoryHierachy.size() - 2;
            if (categoryLevel1 > 0) {

                    data.setCategory(licenseCategoryService
                                    .composeLicenseCategoryMinDto(categoryHierachy.get(categoryHierachy.size() - 2)));
            }
            
            data.setBills(billingService.getLicenseBillingByLicenseid(licence.getId(),BillingAttachedToEnum.LICENCE));
            
            data.setActivities(taskService.getTaskActivities(licence));
            data.setTrack(taskService.getTaskTracks(licence));
            data.setStatusHistory(statusHistoryService.getStatusHistoryByTrackable(licence));
            data.setSpectrumValues(acquiredSpectrumRepo.findSpectrumValueByLicenceId(licence.getId()));
            
            //populate licence fees and product fees
            List<PayableFeesDto> licenceFees=feeService.findFeeStructureByFeeable(licence);
            licenceFees.addAll(feeService.findFeeStructureByFeeable(licence.getLicenseProduct()));
            data.setFees(licenceFees);
            
            data.setLicenceCertificateUri(attachmentService.getLicenceCertificateUri(licence));
            
            response.setData(data);
        } catch (Exception e) {

            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }
        
        return response;
    }

    
    @Override
    public Response<CollectionModel<EntityModel<LicenseMinDto>>> getListOfNonDraftLicences(int page, int size,
                    String sortName, String sortType, 
                    Long productId,
                    Long rootId) {
        Response<CollectionModel<EntityModel<LicenseMinDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
                        "NON DRAFT LICENCES RETRIEVED SUCCESSFULLY", null);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortName).descending());
            if ("ASC".equals(sortType.toUpperCase())) {

                    pageable = PageRequest.of(page, size, Sort.by(sortName).ascending());
            }

            Page<LicenseMinDto> pagedData;
            if (productId > 0 && rootId > 0) {
                    
                pagedData = licenceRepo.findAllNonDraftLicencesByRootAndProduct(rootId,productId,pageable);
                
            }else if(rootId > 0){
                
                pagedData = licenceRepo.findAllNonDraftLicencesByRoot(rootId,pageable);
                
            }else if(productId > 0){
                
                pagedData = licenceRepo.findAllNonDraftLicencesByProduct(productId,pageable);
            }else{
            
                pagedData = licenceRepo.findAllNonDraftLicences(pageable);
            }
            
            
            if (!pagedData.hasContent()) {

                    response.setMessage("NON DRAFT LICENCE NOT FOUND ");
                    return response;
            }

            response.setData(pagedResourcesAssembler.toModel(pagedData));

        } catch (Exception e) {

            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }

        return response;
    }

    @Override
    public ApplicantEntityDto composeLicenceApplicationEntity(LicenceApplicationEntity data) {
        ApplicantEntityDto response = new ApplicantEntityDto();
        try {
            response.setName(data.getName());
            response.setPhone(data.getPhone());
            response.setCategoryName(data.getCategory().getName());
            response.setEmail(data.getEmail());
            response.setFax(data.getFax());
            response.setPhysicalAddress(data.getPhysicalAddress());
            response.setPostalAddress(data.getPostalAddress());
            response.setPostalCode(data.getPostalCode());
            response.setRegCertNo(data.getRegCertNo());
            response.setTinNo(data.getTinNo());
            response.setBusinessLicenceNo(data.getBusinessLicenceNo());
            response.setWebsite(data.getWebsite());

            response.setCountry(locationService.getGeoLocationById(data.getCountryID()).getName());
            response.setRegion(locationService.getGeoLocationById(data.getRegionID()).getName());
            response.setDistrict(locationService.getGeoLocationById(data.getDistrictID()).getName());
            response.setWard(locationService.getGeoLocationById(data.getWardID()).getName());

            if(data.getShareholders() != null){
            
                response.setShareholders(this.composeLicenceApplicationEntityShareholders(data.getShareholders()));
            }
            

        } catch (Exception e) {

            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public List<ShareholderDto> composeLicenceApplicationEntityShareholders(Set<LicenceApplicationShareholder> data) {
        List<ShareholderDto> response = new ArrayList();
        try {

            
            for (LicenceApplicationShareholder shareholder : data) {
                ShareholderDto sh = new ShareholderDto();
                sh.setFullname(shareholder.getFullname());
                sh.setNationality(shareholder.getNationality());
                sh.setShares(shareholder.getShares());

                response.add(sh);
            }
        } catch (Exception e) {

            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public IndividualLicenceDto composeIndividualLicenceDto(IndividualLicenceApplicationDetail data) {
        IndividualLicenceDto response = new IndividualLicenceDto();
        try {

            response.setIncludeSpectrum(data.getIncludeSpectrum());
            response.setIncludeSpectrumRequired(data.getIncludeSpectrumRequired());
            response.setRequestDescription(data.getRequestDescription());
            response.setInvestmentCost(data.getInvestmentCost());
            response.setInvestmentCostCurrency(data.getInvestmentCostCurrency());
            response.setOtherRelevantInfo(data.getOtherRelevantInfo());
            response.setCommencementDate(data.getCommencementDate());
            response.setBeamingSatelliteLatitude(data.getBeamingSatelliteLatitude());
            response.setBeamingSatelliteLocation(data.getBeamingSatelliteLocation());
            response.setBeamingSatelliteLongitude(data.getBeamingSatelliteLongitude());
            response.setFacilityOwnerCategory(listOfValueRepo.findByIdMinDto(data.getFacilityOwnerCategory()));
            response.setSatelliteUplinkRequired(data.getSatelliteUplinkRequired());
            response.setTransmitterFacilityLesser(data.getTransmitterFacilityLesser());
            
            if(data.getContentResources() != null){
                
                List<ListOfValueMinDto> resources=new ArrayList();
                for(IndividualLicenceApplicationContentResource resource : data.getContentResources()){
                
                    ListOfValueMinDto rsr=new ListOfValueMinDto();
                    rsr.setId(resource.getResource().getId());
                    rsr.setCode(resource.getResource().getCode());
                    rsr.setName(resource.getResource().getName());
                    
                    resources.add(rsr);
                }
                response.setResources(resources);
            }
            
        } catch (Exception e) {

            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<LicenseMinDto>>> getListOfAllLicences(int page, int size,
                    String sortName, String sortType, Long productId,Long rootId) {
        Response<CollectionModel<EntityModel<LicenseMinDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
                        "LICENCES RETRIEVED SUCCESSFULLY", null);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortName).descending());
            if ("ASC".equals(sortType.toUpperCase())) {

                    pageable = PageRequest.of(page, size, Sort.by(sortName).ascending());
            }

            Page<LicenseMinDto> pagedData;
            String searchable="";
            if (productId > 0) {
                    
                searchable +="".equals(searchable)?" WHERE e.licenseProduct.id="+productId:" AND e.licenseProduct.id="+productId;
            }
            
            if(rootId > 0){
                
                searchable +="".equals(searchable)?" WHERE e.rootLicenceCategoryId="+rootId:" AND e.rootLicenceCategoryId="+rootId;
            }
            
            pagedData = licenceRepo.findAllLicencesSearchable(pageable);
            if (!pagedData.hasContent()) {

                    response.setMessage("LICENCES NOT FOUND ");
                    return response;
            }

            response.setData(pagedResourcesAssembler.toModel(pagedData));

        } catch (Exception e) {

            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }

        return response;
    }

    @Override
    public Response<CollectionModel<EntityModel<PresentationDto>>> getListOfPresentations(Pageable pageable) {
        Response<CollectionModel<EntityModel<PresentationDto>>> response=new Response(ResponseCode.SUCCESS,true,"PRESENTATION LISTED SUCCESSFULLY",null);
        try{
        
            Page<PresentationDto> data=presentationRepo.findAllPresentations(WorkflowDecisionEnum.ACKNOWLEDGE,pageable);
            
            if(!data.hasContent()){
            
                response.setMessage("PRESENTATIONS NOT FOUND");
                return response;
            }
            
            response.setData(presentationPagedResourcesAssembler.toModel(data));
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    public Response<AttachmentMaxDto> getPresentation(Long presentationId) {
        Response<AttachmentMaxDto> response=new Response(ResponseCode.SUCCESS,true,"PRESENTATION ATTACHMENT RETRIEVED SUCCESSFULLY",null);
        try{
        
            Optional<LicencePresentation> presentation=presentationRepo.findById(presentationId);
            
            if(!presentation.isPresent()){
            
                response.setMessage("PRESENTATION NOT FOUND");
                return response;
            }
            
            AttachmentMaxDto attachment=attachmentService.getAttachmentMax(presentation.get());
            
            if(attachment == null){
                
                response.setMessage("ATTACHMENTS NOT FOUND");
                return response;
            }
            
            response.setData(attachment);
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    @Transactional
    public Response<Licence> licenceCancellation(LicenceCancellationDto data) {
        Response<Licence> response=new Response<>(ResponseCode.SUCCESS,true,"LICENCE CANCELLATION INITIATED SUCCESSFULLY",null);
        try{
            //verify licence existance
            Optional<Licence> licenceExistance=licenceRepo.findById(data.getLicenceId());
            if(!licenceExistance.isPresent()){
            
                throw new DataNotFoundException("LICENCE NOT FOUND");
            }
            
            Licence licence=licenceExistance.get();
            
            if(licence.getLicenseState() != LicenceStateEnum.ACTIVE &&
                    licence.getLicenseState() != LicenceStateEnum.SUSPENDED){
            
                throw new OperationNotAllowedException("LICENCE NOT ACTIVE/SUSPENDED");
            }
            
            licence.setApplicationState(LicenceApplicationStateEnum.CANCELLATION);
            licence.setUpdatedAt(LocalDateTime.now());
            licence=licenceRepo.save(licence);
            
            //save activity data
            TaskActivity activity=new TaskActivity();
            activity.setActivityName("Licence Cancellation Initiation");
            activity.setComments(data.getReason());
            activity.setDecision(WorkflowDecisionEnum.COMPLETE);
            activityRepo.save(activity);
            
            taskService.intiateLicenceTrack(licence,Boolean.FALSE, cancellationLicenceWorkflowTypeCode);
            
            response.setData(licence);
            
        }catch(OperationNotAllowedException e){
        
            throw new OperationNotAllowedException(e.getLocalizedMessage());
        }catch(DataNotFoundException e){
        
            throw new DataNotFoundException(e.getLocalizedMessage());
        }catch(Exception e){
        
            e.printStackTrace();
            throw new GeneralException("INTERNAL_SERVER_ERROR");
        }
        
        return response;
    }
}
