package tz.go.tcra.lims.feestructure.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.entity.views.LicenceView;
import tz.go.tcra.lims.feestructure.dto.FeeDto;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;
import tz.go.tcra.lims.feestructure.dto.FeeStructureMinDto;
import tz.go.tcra.lims.feestructure.entity.Feeable;
import tz.go.tcra.lims.miscellaneous.service.ListOfValueService;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.feestructure.repository.FeeStructureRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityRepository;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.portal.application.dto.PayableFeesDto;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.licence.repository.LicenceRepository;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.product.entity.EntityProduct;
import tz.go.tcra.lims.product.entity.LicenceProduct;
import tz.go.tcra.lims.product.repository.EntityProductRepository;
import tz.go.tcra.lims.product.repository.LicenceProductRepository;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.utils.AppUtility;

/**
 * @author DonaldSj
 */
@Slf4j
@Service
public class FeeStructureServiceImpl implements FeeStructureService {

	@Autowired
	private FeeStructureAssembler assembler;

        @Autowired
	private PagedResourcesAssembler<FeeStructure> pagedResourcesAssembler;
        
	@Autowired
	private FeeStructureRepository feeStructureRepo;
        
	@Autowired
	private ListOfValueRepository listOfValueRepo;

        @Autowired
	private LicenceeEntityRepository licenceeEntityRepo;
        
        @Autowired
	private EntityProductRepository entityProductRepo;
        
        @Autowired
	private LicenceRepository licenceRepo;
        
        @Autowired
	private LicenceProductRepository licenceProductRepo;
        
	@Autowired
	private ListOfValueService listOfValueService;
        
        @Autowired
	private AppUtility utility;
        
	@Override
	public Response<EntityModel<FeeStructure>> findFeeStructureById(Long feeId) {

		try {
			if (feeStructureRepo.existsByIdAndActive(feeId, true)) {

				FeeStructure feeStructure = feeStructureRepo.findByIdAndActive(feeId, true);

				feeStructure.setDeletedAt(LocalDateTime.now());
				EntityModel<FeeStructure> licenseFeeStructureModel = new EntityModel<>(
						feeStructureRepo.saveAndFlush(feeStructure));
				return new Response<>(ResponseCode.SUCCESS, true, "Data deleted Successfully",
						licenseFeeStructureModel);

			} else {
                            
                            return new Response<>(ResponseCode.NO_RECORD_FOUND, false, "There is no entry Found", null);

			}
		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!", null);
		}
	}

	@Override
	public Response<CollectionModel<EntityModel<FeeStructure>>> getListOfAllFeeStructures(String keyword,
			Pageable pageable) {
            Response<CollectionModel<EntityModel<FeeStructure>>> response=new Response(ResponseCode.SUCCESS,true,"FEE STRUCTURES RETRIEVED SUCCESSFULY",null);
            try {
                
                Page<FeeStructure> data=null;
                if(keyword.trim().isEmpty()){
                
                    data=feeStructureRepo.findByActive(true,pageable);
                }else{
                
                    data=feeStructureRepo.findByActiveAndKeyword(true, keyword, pageable);
                }
                
                if(!data.hasContent()){
                
                    response.setMessage("FEE STRUCTURES NOT FOUND");
                    return response;
                }
                
                response.setData(pagedResourcesAssembler.toModel(data));
                
            } catch (Exception e) {

                e.printStackTrace();
                response.setMessage("FAILURE TO RETRIEVE DATA");
            }
            
            return response;
	}

    @Override
    public FeeStructureMinDto composeFeeStructureMinDto(FeeStructure data) throws Exception {
        FeeStructureMinDto response=new FeeStructureMinDto();
        response.setId(data.getId());
        response.setCode(data.getCode());
        response.setAccountCode(data.getAccountCode());
        response.setName(data.getName());
        response.setFeeAmount(data.getFeeAmount());
        response.setFeePercent(data.getFeePercent());
        response.setPeriod(data.getPeriod());
        response.setFrequency(data.getFrequency());
        response.setCurrency(listOfValueService.composeListOfValueMinDto(data.getFeeCurrency()));
        response.setFeeType(listOfValueService.composeListOfValueMinDto(data.getFeeType()));
        response.setApplicableState(data.getApplicableState());
        response.setActive(data.getActive());
        
        return response;
    }

    @Override
    public List<PayableFeesDto> findFeeStructureByFeeable(Feeable feeable) {
        List<PayableFeesDto> response=new ArrayList();
        try{
        
            List<FeeStructure> feeStructure = feeStructureRepo.findByFeeableAndActive(feeable, true);
            for (FeeStructure feeSt : feeStructure) {

                PayableFeesDto fee = new PayableFeesDto();
                fee.setId(feeSt.getId());
                fee.setCurrencyId(feeSt.getFeeCurrency().getId());
                fee.setCurrencyName(feeSt.getFeeCurrency().getName());
                fee.setFeeType(feeSt.getFeeType().getName());
                fee.setFeeTypeId(feeSt.getFeeType().getId());
                fee.setFeeAmount(feeSt.getFeeAmount());

                response.add(fee);
            }
            
            
        }catch(Exception e){
        
            log.error(e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    @Transactional
    public Response<EntityModel<FeeStructure>> saveFeeStructure(FeeDto data,Long id) {
        Response<EntityModel<FeeStructure>> response=new Response(ResponseCode.SUCCESS,true,"FEE STRUCTURE SAVED SUCCESSFULLY",null);
        try{        
            LimsUser actor = utility.getUser();
            FeeStructure fee=new FeeStructure();
            if(id > 0){
            
                Optional<FeeStructure> feeExistance=feeStructureRepo.findById(id);
                if(!feeExistance.isPresent()){
                
                    throw new DataNotFoundException("FEE STRUCTURE NOT FOUND");
                }
                fee = feeExistance.get();
                fee.setActive(Boolean.TRUE);
                fee.setUpdatedBy(actor.getId());
                fee.setUpdatedAt(LocalDateTime.now());
            }else{
            
                fee.setCreatedBy(actor.getId());
                fee.setCreatedAt(LocalDateTime.now());
            }
            
            Optional<ListOfValue> currency = listOfValueRepo.findById(data.getFeeCurrency());
            if (!currency.isPresent()) {

                throw new DataNotFoundException("CURRENCY NOT VALID");
            }

            Optional<ListOfValue> feeType = listOfValueRepo.findById(data.getFeeType());
            if (!feeType.isPresent()) {

                throw new DataNotFoundException("FEE TYPE NOT VALID");
            }
            
            Feeable feeable=this.getFeable(data.getFeableId(), data.getFeeableType());
            
            fee.setCode(data.getCode());
            fee.setAccountCode(data.getAccountCode());
            fee.setName(data.getName());
            fee.setFrequency(data.getFrequency());
            fee.setFeePercent(data.getFeePercent());
            fee.setFeeAmount(data.getFeeAmount());
            fee.setPeriod(data.getPeriod());
            fee.setFeeCurrency(currency.get());
            fee.setApplicableState(data.getApplicableState());
            fee.setFeeType(feeType.get());
            fee.setFeeable(feeable);
            fee = feeStructureRepo.saveAndFlush(fee);
            
            response.setData(assembler.toModel(fee));
            
        }catch (DataNotFoundException e) {

            log.error(e.getMessage());
            throw new DataNotFoundException(e.getLocalizedMessage());
            
        }catch(Exception e){
        
            e.printStackTrace();
            throw new GeneralException("INTERNAL_SERVER_ERROR");
        }
        
        return response;
    }

    @Override
    public Feeable getFeable(Long feableId, FeeableTypeEnum type) throws DataNotFoundException,Exception{
        Feeable response=null;
        
        if(type.equals(FeeableTypeEnum.ENTITY)){

            Optional<LicenceeEntity> licencee=licenceeEntityRepo.findById(feableId);
            if(!licencee.isPresent()){
                throw new DataNotFoundException("LICENCEE ENTITY NOT FOUND");
            }
            
            response=licencee.get();
            
        }else if(type.equals(FeeableTypeEnum.ENTITY_PRODUCT)){
        
            if(type.equals(FeeableTypeEnum.ENTITY_PRODUCT)){

                Optional<EntityProduct> entityProduct=entityProductRepo.findById(feableId);
                if(!entityProduct.isPresent()){
                    throw new DataNotFoundException("ENTITY PRODUCT NOT FOUND");
                }

                response=entityProduct.get();
            }
        }else if(type.equals(FeeableTypeEnum.LICENSE_PRODUCT)){
        
            if(type.equals(FeeableTypeEnum.LICENSE_PRODUCT)){

                Optional<LicenceProduct> licenceProduct=licenceProductRepo.findById(feableId);
                if(!licenceProduct.isPresent()){
                    throw new DataNotFoundException("LICENCE PRODUCT NOT FOUND");
                }

                response=licenceProduct.get();
            }
            
        }else if(type.equals(FeeableTypeEnum.LICENSE)){
        
            if(type.equals(FeeableTypeEnum.LICENSE)){

                Optional<Licence> licence=licenceRepo.findById(feableId);
                if(!licence.isPresent()){
                    throw new DataNotFoundException("LICENCE NOT FOUND");
                }

                response=licence.get();
            }
            
        }else{}
        
        
        return response;
    }

    @Override
    public Response<EntityModel<FeeStructure>> activateDeactivateFeeStructureById(Long id) {
        Response<EntityModel<FeeStructure>> response = new Response<>(ResponseCode.SUCCESS, true,
				"FEE STRUCTURE DETAILS UPDATED SUCCESSFULLY", null);
        try {
                LimsUser actor = utility.getUser();

                FeeStructure data = feeStructureRepo.getOne(id);
                Boolean active = data.getActive() ? false : true;
                data.setActive(active);
                data.setUpdatedBy(actor.getId());
                data.setUpdatedAt(LocalDateTime.now());
                data = feeStructureRepo.saveAndFlush(data);

                response.setData(assembler.toModel(data));

        } catch (EntityNotFoundException e) {

                log.error(e.getMessage());
                e.printStackTrace();
                response.setMessage("LICENSE PRODUCT DETAILS NOT FOUND");

        } catch (Exception e) {

                log.error(e.getMessage());
                e.printStackTrace();
                throw new GeneralException("INTERNAL SERVER ERROR");
        }
        return response;
    }

    @Override
    public Response<List<FeeStructure>> getListOfAllFeeStructuresByFeableType(FeeableTypeEnum feableType) {
        Response<List<FeeStructure>> response=new Response(ResponseCode.SUCCESS,true,"FEE STRUCTURES RETRIEVED SUCCESSFULY",null);
        try {

            List<FeeStructure> data=feeStructureRepo.findByFeeableTypeAndActive(feableType,true);
           
            if(data.size() == 0){

                response.setMessage("FEE STRUCTURES NOT FOUND");
                return response;
            }

            response.setData(data);

        } catch (Exception e) {

            e.printStackTrace();
            response.setMessage("FAILURE TO RETRIEVE DATA");
        }

        return response;
    }
}
