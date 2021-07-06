/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import tz.go.tcra.lims.licencee.dto.LicenceeEntityDto;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.task.dto.ActorMinDto;
import tz.go.tcra.lims.uaa.dto.ApplicantMinDto;
import tz.go.tcra.lims.uaa.dto.ChangePasswordDto;
import tz.go.tcra.lims.uaa.dto.ProfileDto;
import tz.go.tcra.lims.uaa.dto.ProfileMaxDto;
import tz.go.tcra.lims.uaa.dto.UserCreationRequestDto;
import tz.go.tcra.lims.uaa.dto.UserDto;
import tz.go.tcra.lims.uaa.dto.UserMaxDto;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.uaa.entity.UserRole;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface UserService {

	public Response<CollectionModel<EntityModel<UserMaxDto>>> listInternalUsers(Pageable pageable);

	public List<UserMaxDto> listByStatus(boolean status);

	public Response<EntityModel<UserMaxDto>> details(Long id);

	public Response<EntityModel<UserMaxDto>> saveUser(UserCreationRequestDto data, Long id);

	public Response<EntityModel<UserMaxDto>> saveUserRole(List<Long> data, Long user_id);

	public SaveResponseDto changePassword(ChangePasswordDto data);

	public Response completeProfile(ProfileDto data);

	public Response updatePersonalProfile(UserDto data);

	public Response updateEntityProfile(LicenceeEntityDto data);

	public void saveShareholders(List<ShareholderDto> data, Long entity_id);

	public Response<ProfileMaxDto> getProfile(Long user_id);

	public ProfileMaxDto getProfile();

	UserMaxDto composeUserMaxDto(LimsUser data);

	// currently used when mapping user to a task
	UserRole getUserRole(Long roleId) throws DataNotFoundException, Exception;

	ActorMinDto getActorMinDto(Long actorId) throws DataNotFoundException, Exception;

	public Response<CollectionModel<EntityModel<ApplicantMinDto>>> listApplicants(String keyword, Pageable pageable);

	UserDto composeUserDto(LimsUser data);

	public Response<CollectionModel<EntityModel<UserMaxDto>>> listAll(String keyword, Pageable pageable);
}
