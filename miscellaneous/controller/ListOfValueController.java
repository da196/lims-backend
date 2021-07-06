package tz.go.tcra.lims.miscellaneous.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.miscellaneous.entity.ListOfValue;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueDto;
import tz.go.tcra.lims.miscellaneous.enums.ListOfValueTypeEnum;
import tz.go.tcra.lims.miscellaneous.service.ListOfValueService;
import tz.go.tcra.lims.utils.Response;

@RestController
@RequestMapping("v1/list-of-Value")
public class ListOfValueController {

	@Autowired
	private ListOfValueService listOfValueService;

	@PostMapping(value = "/save")
	@PreAuthorize("hasRole('ROLE_LIST_OF_VALUES_SAVE')")
	public Response<EntityModel<ListOfValue>> saveListOfValue(@RequestBody ListOfValueDto listOfValueDto) {

		return listOfValueService.saveListOfValue(listOfValueDto);

	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_LIST_OF_VALUES_VIEW')")
	public Response<EntityModel<ListOfValue>> findListOfValueById(@PathVariable("id") Long id) {

		return listOfValueService.findListOfValueById(id);

	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_LIST_OF_VALUES_EDIT')")
	public Response<EntityModel<ListOfValue>> updateListOfValueType(@RequestBody ListOfValueDto listOfValueDto,
			@PathVariable("id") Long id) {

		return listOfValueService.updateListOfValue(listOfValueDto, id);

	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_LIST_OF_VALUES_DELETE')")
	public Response<EntityModel<ListOfValue>> deleteListOfValue(@PathVariable("id") Long id) {

		return listOfValueService.deleteListOfValue(id);

	}

	@GetMapping(value = "/list")
	@PreAuthorize("hasRole('ROLE_LIST_OF_VALUES_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValues() {
		return listOfValueService.getListOfListOfValues();

	}

	@GetMapping(value = "/getByParent")
	@PreAuthorize("hasRole('ROLE_LIST_OF_VALUES_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValueByParent(
			@RequestBody ListOfValue parent) {
		return listOfValueService.getListOfListOfValueByParent(parent);
	}

	@GetMapping(value = "/getByType/{type}")
	@PreAuthorize("hasRole('ROLE_LIST_OF_VALUES_VIEW_ALL')")
	Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValueByType(
			@PathVariable("type") ListOfValueTypeEnum type) {

		return listOfValueService.getListOfListOfValueByType(type);
	}

	@GetMapping(value = "/list-pegeable")
	@PreAuthorize("hasRole('ROLE_LIST_OF_VALUES_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<ListOfValue>>> getListOfListOfValuesPageable(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return listOfValueService.getListOfListOfValuesPageable(keyword, pageable);

	}

}
