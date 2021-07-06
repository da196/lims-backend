package tz.go.tcra.lims.miscellaneous.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueDto;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueMinDto;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

@Slf4j
@Service
public class ListOfValueServiceImpl implements ListOfValueService {

	@Autowired
	private ListOfValueRepository listOfValueRepository;

	@Autowired
	private ListOfValueModelAssembler listOfValueModelAssembler;

	@Autowired
	private PagedResourcesAssembler<ListOfValue> pagedResourcesAssembler;

	@Override
	public Response<EntityModel<ListOfValue>> saveListOfValue(ListOfValueDto listOfValueDto) {
		try {
			ListOfValue listOfValue = new ListOfValue();
			if (listOfValueRepository.existsByNameOrCodeAndActive(listOfValueDto.getName(), listOfValueDto.getCode(),
					true)) {
				return new Response<>(ResponseCode.DUPLICATE, false, "There is duplicate entry", null);

			} else {

				listOfValue.setActive(true);
				listOfValue.setApproved(1);
				listOfValue.setCode(listOfValueDto.getCode());
				listOfValue.setCreatedAt(LocalDateTime.now());
				listOfValue.setDescription(listOfValueDto.getDescription());
				listOfValue.setName(listOfValueDto.getName());
				listOfValue.setParent(listOfValueDto.getParentId());

				listOfValue.setUniqueID(UUID.randomUUID());
				listOfValue.setType(listOfValueDto.getType());

				EntityModel<ListOfValue> entityModel = new EntityModel<>(
						listOfValueRepository.saveAndFlush(listOfValue));
				return new Response<>(ResponseCode.SUCCESS, true, "Data saved Successfully", entityModel);

			}
		} catch (Exception e) {
			log.error("Could not save Geolocation because: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!", null);
		}
	}

	@Override
	public Response<EntityModel<ListOfValue>> updateListOfValue(ListOfValueDto listOfValueDto, Long id) {

		try {
			if (listOfValueRepository.existsByIdAndActive(id, true)) {

				ListOfValue listOfValue = listOfValueRepository.findByIdAndActive(id, true);
				listOfValue.setCode(listOfValueDto.getCode());
				listOfValue.setDescription(listOfValueDto.getDescription());
				listOfValue.setName(listOfValueDto.getName());
				listOfValue.setParent(listOfValueDto.getParentId());
				listOfValue.setType(listOfValueDto.getType());
				listOfValue.setUpdatedAt(LocalDateTime.now());

				EntityModel<ListOfValue> entityModel = new EntityModel<>(
						listOfValueRepository.saveAndFlush(listOfValue));
				return new Response<>(ResponseCode.SUCCESS, true, "Data updated Successfully", entityModel);
			} else {
				return new Response<>(ResponseCode.NO_RECORD_FOUND, false, "There is no entry Found", null);
			}
		} catch (Exception e) {
			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.FAILURE, false, "There is Internal server error!",
					listOfValueModelAssembler.toModel(null));
		}
	}

	@Override
	public Response<EntityModel<ListOfValue>> findListOfValueById(Long id) {

		try {
			if (listOfValueRepository.existsByIdAndActive(id, true)) {
				ListOfValue listOfValue = listOfValueRepository.findByIdAndActive(id, true);

				return new Response<>(ResponseCode.SUCCESS, true, "Successful..",
						listOfValueModelAssembler.toModel(listOfValue));
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
	public Response<EntityModel<ListOfValue>> deleteListOfValue(Long id) {
		try {
			if (listOfValueRepository.existsByIdAndActive(id, true)) {

				ListOfValue listOfValue = listOfValueRepository.findByIdAndActive(id, true);
				EntityModel<ListOfValue> entityModel = new EntityModel<>(
						listOfValueRepository.saveAndFlush(listOfValue));
				return new Response<>(ResponseCode.SUCCESS, true, "Data updated Successfully", entityModel);
//				if (listOfValue.getListOfValues().isEmpty() == true) {
//					listOfValue.setActive(false);
//					listOfValue.setUpdatedAt(LocalDateTime.now());
//
//					EntityModel<ListOfValue> entityModel = new EntityModel<>(
//							listOfValueRepository.saveAndFlush(listOfValue));
//					return new Response<>(ResponseCode.SUCCESS, true, "Data updated Successfully", entityModel);
//				} else {
//					return new Response<>(ResponseCode.UNAUTHORIZED, false, "Requested data is Parent to other data!",
//							null);
//
//				}

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
	public Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValues() {
		try {
			List<ListOfValue> listOfValuelist = listOfValueRepository.findByActive(true);
			if (listOfValuelist.size() == 0) {

				return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
						listOfValueModelAssembler.toCollectionModel(listOfValuelist));
			}

			return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
					listOfValueModelAssembler.toCollectionModel(listOfValuelist));

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();

			return null;
		}
	}

	@Override
	public Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValueByParent(ListOfValue parent) {

		try {

			List<ListOfValue> listOfValuelist = listOfValueRepository.findByParent(parent);

			if (listOfValuelist.size() == 0) {

				return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
						listOfValueModelAssembler.toCollectionModel(listOfValuelist));
			}

			return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
					listOfValueModelAssembler.toCollectionModel(listOfValuelist));

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();

			return null;

		}
	}

	@Override
	public Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValueByType(ListOfValueTypeEnum type) {
		try {

			List<ListOfValue> listOfValuelist = listOfValueRepository.findByType(type);

			if (listOfValuelist.size() == 0) {

				return new Response<>(ResponseCode.SUCCESS, true, "LIST OF VALUES RETRIEVED",
						listOfValueModelAssembler.toCollectionModel(listOfValuelist));
			}

			return new Response<>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
					listOfValueModelAssembler.toCollectionModel(listOfValuelist));

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();

			return null;

		}
	}

//	private Response<CollectionModel<EntityModel<ListOfValue>>> getCollectionModelResponse(
//			List<ListOfValue> listOfValuelist) {
//		List<EntityModel<ListOfValue>> listOfValues = listOfValuelist.stream() //
//				.map(listOfValueModelAssembler::toModel) //
//				.collect(Collectors.toList());
//
//		CollectionModel<EntityModel<ListOfValue>> inlistOfValues = listOfValueModelAssembler
//				.toCollectionModel(listOfValues);
//
//		return new Response<>(ResponseCode.SUCCESS, true, "Data updated Successfully", inlistOfValues);
//	}

	@Override
	public ListOfValueMinDto composeListOfValueMinDto(ListOfValue data) throws Exception {
		ListOfValueMinDto response = new ListOfValueMinDto();
		response.setId(data.getId());
		response.setCode(data.getCode());
		response.setName(data.getName());
		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValuesPageable(String keyword,
			Pageable pageable) {

		Response<CollectionModel<EntityModel<ListOfValue>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LIST OF VALUES  RETRIEVED SUCCESSFULLY", null);

		try {

			Page<ListOfValue> data = null;

			if (keyword.equalsIgnoreCase("All")) {
				data = listOfValueRepository.findByActive(true, pageable);

			} else {

				data = listOfValueRepository.findByKeywordAndActive(keyword, true, pageable);
			}
			if (data != null) {

				response.setData(pagedResourcesAssembler.toModel(data));
			}

		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			response.setCode(ResponseCode.FAILURE);
			response.setStatus(false);
			response.setMessage("FAILURE OCCURED");

		}

		return response;
	}

}
