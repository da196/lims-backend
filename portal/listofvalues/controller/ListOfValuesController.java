package tz.go.tcra.lims.portal.listofvalues.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.miscellaneous.dto.ListOfValueDto;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;
import tz.go.tcra.lims.portal.listofvalues.service.ListOfValuerService;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping("v1/p/list-of-values")
public class ListOfValuesController {

	@Autowired
	private ListOfValuerService listOfValuerService;

	@GetMapping(value = "/{id}")
	Response<ListOfValueDto> findListOfValueById(@PathVariable("id") Long id) {

		return listOfValuerService.findListOfValueById(id);

	}

	@GetMapping(value = "/list")
	Response<List<ListOfValueDto>> getListOfListOfValues() {
		return listOfValuerService.getListOfListOfValues();

	}

	@GetMapping(value = "/getByParentId/{parentId}")
	Response<List<ListOfValueDto>> getListOfListOfValueByParentId(@PathVariable("parentId") Long parentId) {
		return listOfValuerService.getListOfListOfValueByParentId(parentId);

	}

	@GetMapping(value = "/getByType/{type}")
	Response<List<ListOfValueDto>> getListOfListOfValueByType(@PathVariable("type") ListOfValueTypeEnum type) {

		return listOfValuerService.getListOfListOfValueByType(type);

	}

}
