package tz.go.tcra.lims.portal.listofvalues.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueDto;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;
import tz.go.tcra.lims.miscellaneous.repository.ListOfValueRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

@Slf4j
@Service
public class ListOfValuerServiceImpl implements ListOfValuerService {

	@Autowired
	private ListOfValueRepository listOfValueRepository;

	@Override
	public Response<ListOfValueDto> findListOfValueById(Long id) {
		ListOfValueDto listOfValueDto = new ListOfValueDto();

		try {
			if (listOfValueRepository.existsByIdAndActive(id, true)) {
				ListOfValue listOfValue = listOfValueRepository.findByIdAndActive(id, true);

				listOfValueDto.setCode(listOfValue.getCode());
				listOfValueDto.setDescription(listOfValue.getDescription());
				listOfValueDto.setName(listOfValue.getName());
				listOfValueDto.setParentId(listOfValue.getParent());

				listOfValueDto.setType(listOfValue.getType());

				return new Response<ListOfValueDto>(ResponseCode.SUCCESS, true, "Successful..", listOfValueDto);
			} else {
				return new Response<ListOfValueDto>(ResponseCode.NO_RECORD_FOUND, false,
						"Requested data could not be found!", listOfValueDto);

			}
		} catch (Exception e) {
			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			return new Response<ListOfValueDto>(ResponseCode.FAILURE, false, "There is Internal server error!",
					listOfValueDto);
		}
	}

	@Override
	public Response<List<ListOfValueDto>> getListOfListOfValues() {
		try {
			List<ListOfValue> listOfValuelist = listOfValueRepository.findByActive(true);

			List<ListOfValueDto> listOfValueDtoList = new ArrayList<>();
			if (listOfValuelist.size() == 0) {
				return new Response<List<ListOfValueDto>>(ResponseCode.SUCCESS, true, "LIST OF VALUES  RETRIEVED",
						listOfValueDtoList);
			} else {
				for (ListOfValue listOfValue : listOfValuelist) {
					ListOfValueDto listOfValueDto = new ListOfValueDto();

					listOfValueDto.setCode(listOfValue.getCode());
					listOfValueDto.setDescription(listOfValue.getDescription());
					listOfValueDto.setName(listOfValue.getName());
					listOfValueDto.setParentId(listOfValue.getParent());
					listOfValueDto.setType(listOfValue.getType());
					listOfValueDto.setId(listOfValue.getId());

					listOfValueDtoList.add(listOfValueDto);

				}

				return new Response<List<ListOfValueDto>>(ResponseCode.SUCCESS, true, "LIST OF VALUES  RETRIEVED",
						listOfValueDtoList);
			}

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			return new Response<>(ResponseCode.SUCCESS, true, "Exception error", null);
		}
	}

	@Override
	public Response<List<ListOfValueDto>> getListOfListOfValueByType(ListOfValueTypeEnum type) {

		try {
			List<ListOfValue> listOfValueList = listOfValueRepository.findByTypeAndActive(type, true);

			List<ListOfValueDto> listOfValueDtoList = new ArrayList<>();
			if (listOfValueList.size() == 0) {
				return new Response<List<ListOfValueDto>>(ResponseCode.SUCCESS, true, "LIST OF VALUES RETRIEVED",
						listOfValueDtoList);
			} else {
				for (ListOfValue listOfValue : listOfValueList) {
					ListOfValueDto listOfValueDto = new ListOfValueDto();

					listOfValueDto.setCode(listOfValue.getCode());
					listOfValueDto.setDescription(listOfValue.getDescription());
					listOfValueDto.setName(listOfValue.getName());
					listOfValueDto.setParentId(listOfValue.getParent());
					listOfValueDto.setType(listOfValue.getType());
					listOfValueDto.setId(listOfValue.getId());

					listOfValueDtoList.add(listOfValueDto);

				}

				return new Response<List<ListOfValueDto>>(ResponseCode.SUCCESS, true, "LIST OF VALUES RETRIEVED",
						listOfValueDtoList);
			}
		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();

			return new Response<>(ResponseCode.SUCCESS, true, "Exception error", null);

		}
	}

	@Override
	public Response<List<ListOfValueDto>> getListOfListOfValueByParentId(Long parentId) {

		try {
			List<ListOfValueDto> ListOfValueDtolist = new ArrayList<>();
			if (listOfValueRepository.existsByIdAndActive(parentId, true)) {
				ListOfValue listOfValueParent = listOfValueRepository.findByIdAndActive(parentId, true);

				List<ListOfValue> listOfValuelist = listOfValueRepository.findByParent(listOfValueParent);

				if (listOfValuelist.size() == 0) {
					return new Response<List<ListOfValueDto>>(ResponseCode.SUCCESS, true, "GEO LOCATIONS RETRIEVED",
							ListOfValueDtolist);
				} else {
					for (ListOfValue listOfValue : listOfValuelist) {
						ListOfValueDto listOfValueDto = new ListOfValueDto();

						listOfValueDto.setCode(listOfValue.getCode());
						listOfValueDto.setDescription(listOfValue.getDescription());
						listOfValueDto.setName(listOfValue.getName());
						listOfValueDto.setParentId(listOfValue.getParent());
						listOfValueDto.setType(listOfValue.getType());
						listOfValueDto.setId(listOfValue.getId());

						ListOfValueDtolist.add(listOfValueDto);

					}

					return new Response<List<ListOfValueDto>>(ResponseCode.SUCCESS, true, "LIST OF VALUES RETRIEVED",
							ListOfValueDtolist);
				}
			} else {

				return new Response<List<ListOfValueDto>>(ResponseCode.NO_RECORD_FOUND, false,
						"Requested data could not be found!", ListOfValueDtolist);

			}
		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();

			return new Response<>(ResponseCode.SUCCESS, true, "Exception error", null);

		}
	}

}
