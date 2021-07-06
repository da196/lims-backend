package tz.go.tcra.lims.portal.listofvalues.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tz.go.tcra.lims.miscellaneous.dto.ListOfValueDto;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;
import tz.go.tcra.lims.utils.Response;

@Service
public interface ListOfValuerService {

	Response<ListOfValueDto> findListOfValueById(Long id);

	Response<List<ListOfValueDto>> getListOfListOfValues();

	Response<List<ListOfValueDto>> getListOfListOfValueByParentId(Long parentId);

	Response<List<ListOfValueDto>> getListOfListOfValueByType(ListOfValueTypeEnum type);

}
