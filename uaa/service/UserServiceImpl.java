/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import static tz.go.tcra.lims.config.LimsConfig.zone;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.attachments.services.AttachmentService;
import tz.go.tcra.lims.communications.dto.CommunicationChannel;
import tz.go.tcra.lims.communications.entity.NotificationsIn;
import tz.go.tcra.lims.communications.service.NotificationsInService;
import tz.go.tcra.lims.licencee.dto.LicenceeEntityDto;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.licencee.entity.EntityCategory;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.licencee.entity.LicenceeEntityShareholder;
import tz.go.tcra.lims.licencee.repository.EntityCategoryRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityRepository;
import tz.go.tcra.lims.licencee.repository.LicenceeEntityShareholderRepository;
import tz.go.tcra.lims.licencee.service.LicenceeEntityService;
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
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.uaa.repository.UserRoleRepository;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.DuplicateException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.utils.exception.OperationNotAllowedException;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EntityCategoryRepository entityCategoryRepo;

	@Autowired
	private LicenceeEntityRepository licenceeEntityRepo;

	@Autowired
	private UserRoleRepository userRoleRepo;

	@Autowired
	private LicenceeEntityShareholderRepository shareholderRepo;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private NotificationsInService notificationService;

	@Autowired
	private UserServiceModelAssembler2 assembler2;

	@Autowired
	private PagedResourcesAssembler<ApplicantMinDto> pagedResourcesAssembler;

	@Autowired
	private PagedResourcesAssembler<UserMaxDto> pagedResourcesAssembler2;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private LicenceeEntityService licenceeEntityService;

	@Autowired
	private RoleService roleService;

	@Value("${lims.new.password.mail.subject}")
	private String newPasswordMailSubject;

	@Value("${lims.new.password.mail.message}")
	private String newPasswordMailMessage;

	@Value("${lims.base.url}")
	private String baseUrl;

	@Override
	public Response<CollectionModel<EntityModel<UserMaxDto>>> listInternalUsers(Pageable pageable) {
		Response<CollectionModel<EntityModel<UserMaxDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"USERS RETRIEVED SUCCESSFULLY", null);
		try {
			List<LimsUser> users = userRepo.findInternalUsers(pageable);

			if (users.size() == 0) {

				response.setMessage("USERS NOT FOUND");
				return response;
			}

			List<UserMaxDto> data = new ArrayList();
			for (LimsUser user : users) {

				data.add(this.composeUserMaxDto(user));
			}

			response.setData(assembler2.toCollectionModel(data));

		} catch (Exception e) {

			log.error(e.toString());
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public List<UserMaxDto> listByStatus(boolean status) {
		List<UserMaxDto> response = new ArrayList();
		try {

			List<LimsUser> users = userRepo.findByStatus(status);

			for (LimsUser user : users) {
				UserMaxDto usr = new UserMaxDto();
				usr.setId(user.getId());
				usr.setFirstName(user.getFirstName());
				usr.setLastName(user.getLastName());
				usr.setEmail(user.getEmail());
				usr.setStatus(user.isStatus());

				response.add(usr);
			}
		} catch (Exception e) {

			log.error(e.toString());
		}

		return response;
	}

	@Override
	public Response<EntityModel<UserMaxDto>> details(Long id) {
		Response<EntityModel<UserMaxDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"USER DETAILS RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<LimsUser> user = userRepo.findById(id);

			if (!user.isPresent()) {

				response.setMessage("USER DETAILS NOT FOUND");
				return response;
			}

			response.setData(assembler2.toModel(this.composeUserMaxDto(user.get())));

		} catch (Exception e) {

			log.error(e.toString());
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<UserMaxDto>> saveUser(UserCreationRequestDto data, Long id) {
		Response<EntityModel<UserMaxDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"USER DETAILS SAVED SUCCESSFULLY", null);
		try {

			LimsUser user = new LimsUser();
			String password = null;
			if (id > 0) {

				user = userRepo.getOne(id);
				user.setUpdatedAt(LocalDateTime.now(ZoneId.of(zone)));

				if (data.isAdAuthentication() && user.getPassword() == null) {

					// generate random password;
					password = appUtility.generatePassayPassword();
					user.setPassword(appUtility.passwordEncode(password));
				}

			} else {

				if (data.isAdAuthentication()) {

					// generate random password;
					password = appUtility.generatePassayPassword();
					user.setPassword(appUtility.passwordEncode(password));

				} else {

					user.setPassword(appUtility.passwordEncode(appUtility.generatePassayPassword()));
				}
			}

			// create user entity
			user.setFirstName(data.getFirstName());
			user.setMiddleName(data.getMiddleName());
			user.setLastName(data.getLastName());
			user.setEmail(data.getEmail());
			user.setPhone(data.getPhone());
			user.setStatus(true);
			user.setComplete(true);
			user.setAdAuthentication(data.isAdAuthentication());
			user = userRepo.saveAndFlush(user);

			response.setData(assembler2.toModel(this.composeUserMaxDto(user)));

			if (password != null) {

				String msg = String.format(newPasswordMailMessage, data.getFirstName() + " " + data.getLastName(),
						baseUrl, password);
				NotificationsIn notification = new NotificationsIn();
				notification.setContact(data.getEmail());
				notification.setSubject(newPasswordMailSubject);
				notification.setMessage(msg);
				notification.setChannel(CommunicationChannel.EMAIL);
				notificationService.saveNotificationsIn(notification);
			}

		} catch (DataIntegrityViolationException e) {

			log.error(e.toString());
			throw new DuplicateException("Email Address Exists");

		} catch (EntityNotFoundException e) {

			log.error(e.toString());
			throw new DataNotFoundException("DATA NOT FOUND");
		} catch (Exception e) {

			log.error(e.toString());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	@Transactional
	public Response<EntityModel<UserMaxDto>> saveUserRole(List<Long> data, Long userId) {
		try {
			List<UserRole> userRoles = new ArrayList();
			Optional<LimsUser> usr = userRepo.findById(userId);

			if (!usr.isPresent()) {

				throw new DataNotFoundException("USER DETAILS NOT FOUND");
			}

			userRoleRepo.deleteByUserID(userId);
			UserRole dt = new UserRole();
			dt.setRoleID(data.get(0));
			dt.setUserID(userId);
			userRoles.add(dt);

			if (userRoles.size() > 0) {

				userRoleRepo.saveAll(userRoles);
			}

			return new Response<>(ResponseCode.SUCCESS, true, "USER ROLES ASSIGNED",
					assembler2.toModel(this.composeUserMaxDto(usr.get())));

		} catch (DataNotFoundException e) {

			log.error(e.toString());
			throw new DataNotFoundException(e.getLocalizedMessage());
		} catch (Exception e) {

			log.error(e.toString());
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
	}

	@Override
	public SaveResponseDto changePassword(ChangePasswordDto data) {
		SaveResponseDto response = new SaveResponseDto();
		response.setStatus(HttpStatus.CREATED.value());
		response.setDescription(HttpStatus.CREATED.toString());
		response.setTimestamp(System.currentTimeMillis());
		try {
			LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(zone));

			LimsUser user = appUtility.getUser();

			PasswordEncoder encoder = new BCryptPasswordEncoder();

			if (!encoder.matches(data.getOldPassword(), user.getPassword())) {

				throw new OperationNotAllowedException("incorrect password");
			}

			// reset user password
			user.setPassword(appUtility.passwordEncode(data.getNewPassword()));
			user.setUpdatedAt(currentTime);
			userRepo.saveAndFlush(user);

		} catch (OperationNotAllowedException e) {

			log.error(e.toString());
			throw new OperationNotAllowedException(e.getLocalizedMessage());

		} catch (Exception e) {

			log.error(e.toString());
			throw new GeneralException("internal server error");

		}

		return response;
	}

	@Override
	@Transactional
	public Response<SaveResponseDto> completeProfile(ProfileDto data) {
		Response<SaveResponseDto> response = new Response<>(ResponseCode.SUCCESS, true,
				"USER PROFILE SAVED SUCCESSFULLY", new SaveResponseDto(HttpStatus.CREATED.value(),
						HttpStatus.CREATED.toString(), System.currentTimeMillis()));
		try {

			Optional<EntityCategory> existing = entityCategoryRepo.findById(data.getEntity().getCategory());

			if (!existing.isPresent()) {

				throw new DataNotFoundException("ENTITY CATEGORY NOT FOUND");
			}

			EntityCategory category = existing.get();
			LimsUser user = appUtility.getUser();

			user.setFirstName(data.getPersonal().getFirstName());
			user.setMiddleName(data.getPersonal().getMiddleName());
			user.setLastName(data.getPersonal().getLastName());
			user.setGender(data.getPersonal().getGender());
			user.setPhone(data.getPersonal().getPhone());
			user.setPhysicalAddress(data.getPersonal().getPhysicalAddress());
			user.setPostalAddress(data.getPersonal().getPostalAddress());
			user.setCountryID(data.getPersonal().getCountryDto());
			user.setRegionID(data.getPersonal().getRegion());
			user.setDistrictID(data.getPersonal().getDistrict());
			user.setComplete(true);
			user.setUpdatedAt(LocalDateTime.now());

			user = userRepo.saveAndFlush(user);

			LicenceeEntity entity = new LicenceeEntity();
			if (user.getUserEntity() != null) {

				entity = user.getUserEntity();
			}

			entity.setName(data.getEntity().getName());
			entity.setCategory(category);
			entity.setEmail(data.getEntity().getEmail());
			entity.setPhone(data.getEntity().getPhone());
			entity.setWebsite(data.getEntity().getWebsite());
			entity.setFax(data.getEntity().getFax());
			entity.setPhysicalAddress(data.getEntity().getPhysicalAddress());
			entity.setPostalAddress(data.getEntity().getPostalAddress());
			entity.setPostalCode(data.getEntity().getPostalCode());
			entity.setCountryID(data.getEntity().getCountry());
			entity.setRegionID(data.getEntity().getRegion());
			entity.setDistrictID(data.getEntity().getDistrict());
			entity.setWardID(data.getEntity().getWard());
			entity.setRegCertNo(data.getEntity().getRegCertNo());
			entity.setBusinessLicenceNo(data.getEntity().getBusinessLicenceNo());
			entity.setTinNo(data.getEntity().getTinNo());
			entity.setCreatedAt(LocalDateTime.now());
			entity.setUser(user);

			entity = licenceeEntityRepo.saveAndFlush(entity);

			this.saveShareholders(data.getEntity().getShareholders(), entity.getId());

			attachmentService.saveAttachments(data.getPersonal().getAttachments(), user);
			attachmentService.saveAttachments(data.getEntity().getAttachments(), entity);

		} catch (DataNotFoundException e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new DataNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response updatePersonalProfile(UserDto data) {
		try {

			LimsUser user = appUtility.getUser();
			user.setFirstName(data.getFirstName());
			user.setMiddleName(data.getMiddleName());
			user.setLastName(data.getLastName());
			user.setGender(data.getGender());
			user.setPhone(data.getPhone());
			user.setPhysicalAddress(data.getPhysicalAddress());
			user.setPostalAddress(data.getPostalAddress());
			user.setRegionID(data.getRegion());
			user.setDistrictID(data.getDistrict());
			user.setUpdatedAt(LocalDateTime.now());

			userRepo.saveAndFlush(user);

			return new Response<>(ResponseCode.SUCCESS, true, "USER PROFILE UPDATED", null);
		} catch (Exception e) {

			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
	}

	@Override
	@Transactional
	public Response updateEntityProfile(LicenceeEntityDto data) {
		try {
			EntityCategory category = entityCategoryRepo.getOne(data.getCategory());

			Optional<LicenceeEntity> optionalEntity = licenceeEntityRepo
					.findLicenceeEntityByUser(appUtility.getUser().getId());

			LicenceeEntity entity = optionalEntity.get();
			entity.setName(data.getName());
			entity.setCategory(category);

			entity.setEmail(data.getEmail());
			entity.setPhone(data.getPhone());
			entity.setWebsite(data.getWebsite());
			entity.setFax(data.getFax());

			entity.setPhysicalAddress(data.getPhysicalAddress());
			entity.setPostalAddress(data.getPostalAddress());
			entity.setRegionID(data.getRegion());
			entity.setDistrictID(data.getDistrict());

			entity.setRegCertNo(data.getRegCertNo());
			entity.setBusinessLicenceNo(data.getBusinessLicenceNo());
			entity.setTinNo(data.getTinNo());
			entity.setCreatedAt(LocalDateTime.now());

			entity = licenceeEntityRepo.saveAndFlush(entity);
			this.saveShareholders(data.getShareholders(), entity.getId());

			attachmentService.saveAttachments(data.getAttachments(), entity);

			return new Response<>(ResponseCode.SUCCESS, true, "ENTITY PROFILE UPDATED", null);
		} catch (EntityNotFoundException e) {

			e.printStackTrace();
			throw new DataNotFoundException("ENTITY CATEGORY NOT FOUND");

		} catch (Exception e) {

			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
	}

	@Override
	public void saveShareholders(List<ShareholderDto> data, Long entity_id) {
		try {
			shareholderRepo.deleteByLicenceeEntity(entity_id);
			LicenceeEntityShareholder shareholder = null;
			for (ShareholderDto dt : data) {

				shareholder = new LicenceeEntityShareholder();
				shareholder.setFullname(dt.getFullname());
				shareholder.setNationality(dt.getNationality());
				shareholder.setShares(dt.getShares());
				shareholder.setLicenceeEntity(entity_id);
				shareholder=shareholderRepo.saveAndFlush(shareholder);
                                
                                attachmentService.saveAttachments(dt.getAttachments(), shareholder);
			}
		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
	}

	@Override
	public Response<ProfileMaxDto> getProfile(Long user_id) {
		Response response = new Response<>(ResponseCode.SUCCESS, true, "RETRIEVE PROFILE", null);
		try {

			ProfileMaxDto profile = new ProfileMaxDto();
			// set userDto
			Optional<LimsUser> existance = userRepo.findById(user_id);
			if (!existance.isPresent()) {

				throw new DataNotFoundException("USER NOT FOUND");
			}

			LimsUser user = existance.get();

			profile.setPersonal(this.composeUserDto(user));

			profile.setEntity(licenceeEntityService.composeLicenceeEntityMaxDto(user.getUserEntity()));

			response.setData(profile);
		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public ProfileMaxDto getProfile() {
		ProfileMaxDto response = new ProfileMaxDto();
		try {

			// set userDto
			LimsUser user = appUtility.getUser();

			response.setPersonal(this.composeUserDto(user));
			response.setEntity(licenceeEntityService.composeLicenceeEntityMaxDto(user.getUserEntity()));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public UserMaxDto composeUserMaxDto(LimsUser data) {
		UserMaxDto response = new UserMaxDto();
		response.setId(data.getId());
		response.setFirstName(data.getFirstName());
		response.setMiddleName(data.getMiddleName());
		response.setLastName(data.getLastName());
		response.setEmail(data.getEmail());
		response.setPhone(data.getPhone());
		response.setStatus(data.isComplete());

		Optional<UserRole> userRole = userRoleRepo.findByUserID(data.getId());

		if (userRole.isPresent()) {

			response.setRole(roleService.getRoleMinDto(userRole.get().getRoleID()).getName());
		}

		return response;
	}

	@Override
	public UserRole getUserRole(Long roleId) throws DataNotFoundException, Exception {

		Optional<UserRole> existing = userRoleRepo.findFirstByRoleID(roleId);

		if (!existing.isPresent()) {

			throw new DataNotFoundException("User with Role [" + roleId + "] not found");
		}

		return existing.get();
	}

	@Override
	public ActorMinDto getActorMinDto(Long actorId) throws DataNotFoundException, Exception {
		ActorMinDto response = null;
		try {
			Optional<ActorMinDto> existing = userRepo.findActorById(actorId);

			if (!existing.isPresent()) {

				return response;
			}

			response = existing.get();

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<ApplicantMinDto>>> listApplicants(String keyword, Pageable pageable) {
		Response<CollectionModel<EntityModel<ApplicantMinDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"USERS RETRIEVED SUCCESSFULLY", null);
		try {

			Page<ApplicantMinDto> data = null;
			if (keyword.equalsIgnoreCase("ALL")) {

				data = userRepo.findApplicantUsers(pageable);
			} else {

				data = userRepo.findApplicantUsersByKeyword(keyword, pageable);
			}

			if (!data.hasContent()) {

				response.setMessage("USERS NOT FOUND");
				return response;
			}

			response.setData(pagedResourcesAssembler.toModel(data));

		} catch (Exception e) {

			log.error(e.toString());
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public UserDto composeUserDto(LimsUser data) {
		UserDto response = new UserDto();
		response.setFirstName(data.getFirstName());
		response.setMiddleName(data.getMiddleName());
		response.setLastName(data.getLastName());
		response.setGender(data.getGender());
		response.setPhone(data.getPhone());
		response.setPhysicalAddress(data.getPhysicalAddress());
		response.setPostalAddress(data.getPostalAddress());
		response.setRegion(data.getRegionID());
		response.setDistrict(data.getDistrictID());
		response.setCountryDto(data.getCountryID());
		response.setAttachments(attachmentService.getAttachments(data));

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<UserMaxDto>>> listAll(String keyword, Pageable pageable) {
		Response<CollectionModel<EntityModel<UserMaxDto>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"USERS RETRIEVED SUCCESSFULLY", null);
		try {

			Page<UserMaxDto> data = null;
			if (keyword.equalsIgnoreCase("ALL")) {

				data = userRepo.findAllUsers(pageable);
			} else {

				data = userRepo.findAllUsersByKeyword(keyword, pageable);
			}

			if (!data.hasContent()) {

				response.setMessage("USERS NOT FOUND");
				return response;
			}

			response.setData(pagedResourcesAssembler2.toModel(data));

		} catch (Exception e) {

			log.error(e.toString());
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}
}
