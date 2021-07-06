package tz.go.tcra.lims.miscellaneous.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueDto;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueMinDto;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;
import tz.go.tcra.lims.utils.Response;

@Service
public interface ListOfValueService {

	Response<EntityModel<ListOfValue>> saveListOfValue(ListOfValueDto listOfValueDto);

	Response<EntityModel<ListOfValue>> findListOfValueById(Long id);

	Response<EntityModel<ListOfValue>> updateListOfValue(ListOfValueDto listOfValueDto, Long id);

	Response<EntityModel<ListOfValue>> deleteListOfValue(Long id);

	Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValues();

	Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValueByParent(ListOfValue parent);

	Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValueByType(ListOfValueTypeEnum type);

	ListOfValueMinDto composeListOfValueMinDto(ListOfValue data) throws Exception;

	Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValuesPageable(String keyword,
			Pageable pageable);
}
