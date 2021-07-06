/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.licencee.dto.LicenceeEntityDto;
import tz.go.tcra.lims.uaa.dto.ApplicantMinDto;
import tz.go.tcra.lims.uaa.dto.ChangePasswordDto;
import tz.go.tcra.lims.uaa.dto.ProfileDto;
import tz.go.tcra.lims.uaa.dto.ProfileMaxDto;
import tz.go.tcra.lims.uaa.dto.UserCreationRequestDto;
import tz.go.tcra.lims.uaa.dto.UserDto;
import tz.go.tcra.lims.uaa.dto.UserMaxDto;
import tz.go.tcra.lims.uaa.service.UserService;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.SaveResponseDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@CrossOrigin
@RestController
@RequestMapping("/v1/user")
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/all")
	@PreAuthorize("hasRole('ROLE_USER_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<UserMaxDto>>> list(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 30) Pageable pageable) {

		return service.listAll(keyword, pageable);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_USER_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<UserMaxDto>>> internals(
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return service.listInternalUsers(pageable);
	}

	@GetMapping("/applicants-list")
	@PreAuthorize("hasRole('ROLE_USER_VIEW_ALL')")
	public Response<CollectionModel<EntityModel<ApplicantMinDto>>> applicants(
			@RequestParam(name = "keyword", defaultValue = "All") String keyword,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return service.listApplicants(keyword, pageable);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER_VIEW_DETAILS')")
	public Response<EntityModel<UserMaxDto>> details(@PathVariable("id") Long id) {

		return service.details(id);
	}

	@PostMapping("/save")
	@PreAuthorize("hasRole('ROLE_USER_CREATE')")
	public Response<EntityModel<UserMaxDto>> save(@Valid @RequestBody UserCreationRequestDto data) {

		return service.saveUser(data, 0L);
	}

	@PutMapping("/assign-role/{id}")
	@PreAuthorize("hasRole('ROLE_USER_ROLE_ASSIGNMENT')")
	public Response<EntityModel<UserMaxDto>> assignRole(@RequestBody List<Long> roles, @PathVariable("id") Long id) {

		return service.saveUserRole(roles, id);
	}

	@PostMapping("/complete-profile")
	public Response save(@RequestBody ProfileDto data) {

		return service.completeProfile(data);
	}

	@PutMapping("/save/{id}")
	@PreAuthorize("hasRole('ROLE_USER_EDIT')")
	public Response<EntityModel<UserMaxDto>> update(@PathVariable("id") Long id,
			@Valid @RequestBody UserCreationRequestDto data) {

		return service.saveUser(data, id);
	}

	@PutMapping("/change-password")
	public SaveResponseDto changePassword(@Valid @RequestBody ChangePasswordDto data) {

		return service.changePassword(data);
	}

	@PutMapping("/update/personal-profile")
	public Response updatePersonalProfile(@Valid @RequestBody UserDto data) {

		return service.updatePersonalProfile(data);
	}

	@PutMapping("/update/entity-profile")
	public Response updateEntityProfile(@Valid @RequestBody LicenceeEntityDto data) {

		return service.updateEntityProfile(data);
	}

	@GetMapping("/profile/{userId}")
	public Response<ProfileMaxDto> getProfile(@PathVariable("userId") Long id) {

		return service.getProfile(id);
	}

	@GetMapping("/profile")
	public ProfileMaxDto getProfile() {

		return service.getProfile();
	}
}
