package tz.go.tcra.lims.licence.service;

import java.time.LocalDateTime;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.entity.LicenceServiceDetail;
import tz.go.tcra.lims.licence.dto.LicenseDetailDto;
import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DuplicateException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.licence.repository.LicenceDetailRepository;

@Slf4j
@Service
public class LicenseDetailServiceImpl implements LicenseDetailService {

	@Autowired
	private LicenceDetailRepository licenseDetailRepository;

	@Autowired
	private LicenseDetailAssembler assembler;

	@Autowired
	private PagedResourcesAssembler<LicenceServiceDetail> pagedResourcesAssembler;

	@Override
	public Response<EntityModel<LicenceServiceDetail>> saveLicenseDetail(LicenseDetailDto data, Long id) {
		Response<EntityModel<LicenceServiceDetail>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LICENCSE SERVICE SAVED SUCCESSFULLY", null);
		try {
			LicenceServiceDetail licenseDetail = new LicenceServiceDetail();
			if (id > 0L) {

				licenseDetail = licenseDetailRepository.getOne(id);
			}

			licenseDetail.setCode(data.getCode());
			licenseDetail.setName(data.getName());
			licenseDetail = licenseDetailRepository.saveAndFlush(licenseDetail);

			response.setData(assembler.toModel(licenseDetail));

		} catch (ConstraintViolationException e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new DuplicateException("DUPLICATE RECORD");
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<LicenceServiceDetail>> findLicenseDetailById(Long id) {

		try {
			if (licenseDetailRepository.existsByIdAndActive(id, true)) {
				LicenceServiceDetail licenseDetail = licenseDetailRepository.findById(id).get();

				EntityModel<LicenceServiceDetail> licenseDetailModel = new EntityModel<>(
						licenseDetailRepository.saveAndFlush(licenseDetail));
				return new Response<>(ResponseCode.SUCCESS, true, "Data updated Successfully", licenseDetailModel);
			} else {

				return new Response<>(ResponseCode.NO_RECORD_FOUND, true, "There is no entry Found", null);
			}
		} catch (Exception e) {
			log.error("Could not save LicenseDetail because: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!", null);
		}
	}

	@Override
	public Response<EntityModel<LicenceServiceDetail>> deleteLicenseDetailById(Long id) {
		try {
			if (licenseDetailRepository.existsByIdAndActive(id, true)) {
				LicenceServiceDetail licenseDetail = licenseDetailRepository.findByIdAndActive(id, true);

				licenseDetail.setActive(false);
				licenseDetail.setUpdatedAt(LocalDateTime.now());

				EntityModel<LicenceServiceDetail> entityModel = new EntityModel<>(
						licenseDetailRepository.saveAndFlush(licenseDetail));
				return new Response<>(ResponseCode.SUCCESS, true, "Data deleted Successfully", entityModel);
			} else {
				return new Response<>(ResponseCode.NO_RECORD_FOUND, false, "Requested data could not be found!", null);
			}

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!", null);
		}
	}

	@Override
	public Response<CollectionModel<EntityModel<LicenceServiceDetail>>> getListOfLicenseDetail(String keyword,
			Pageable pageable) {
		try {

			Page<LicenceServiceDetail> licenseDetaillist = null;
			if (keyword.equalsIgnoreCase("All")) {
				licenseDetaillist = licenseDetailRepository.findByActive(true, pageable);
			} else {

				licenseDetaillist = licenseDetailRepository.findByKeywordAndActive(keyword, true, pageable);
			}

			return new Response<>(ResponseCode.SUCCESS, true, "License Details RETRIEVED",
					pagedResourcesAssembler.toModel(licenseDetaillist));

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURED: " + e.getMessage());
			e.printStackTrace();

			return new Response<>(ResponseCode.FAILURE, false, "Internal error occured", null);
		}
	}

	@Override
	public LicenseDetailMaxDto composeLicenceService(LicenceServiceDetail data) {
		LicenseDetailMaxDto response = new LicenseDetailMaxDto();
		try {

			response.setId(data.getId());
			response.setCode(data.getCode());
			response.setName(data.getName());

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return response;
	}
}
