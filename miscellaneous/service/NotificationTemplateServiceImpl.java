/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.miscellaneous.entity.NotificationTemplate;
import tz.go.tcra.lims.miscellaneous.dto.NotificationTemplateDto;
import tz.go.tcra.lims.miscellaneous.repository.NotificationTemplateRepository;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

	@Autowired
	private NotificationTemplateRepository templateRepo;

	@Autowired
	private PagedResourcesAssembler<NotificationTemplate> pagedResourcesAssembler;

	@Autowired
	private NotificationTemplateAssembler assembler;

	@Autowired
	private AppUtility utility;

	@Override
	public Response<EntityModel<NotificationTemplate>> saveNotificationTemplate(NotificationTemplateDto data, Long id) {
		Response<EntityModel<NotificationTemplate>> response = new Response<>(ResponseCode.SUCCESS, true,
				"NOTIFICATION TEMPLATE SAVED SUCCESSFULLY", null);
		try {

			LimsUser actor = utility.getUser();
			NotificationTemplate template = new NotificationTemplate();

			if (id > 0) {

				Optional<NotificationTemplate> existing = templateRepo.findById(id);

				if (!existing.isPresent()) {

					throw new DataNotFoundException("NOTIFICATION TEMPLATE NOT FOUND");
				}

				template = existing.get();
				template.setUpdatedAt(LocalDateTime.now());
				template.setUpdatedBy(actor);
			} else {

				template.setCreatedBy(actor);
			}

			template.setName(data.getName());
			template.setDisplayName(data.getDisplayName());
			template.setEmailTemplate(data.getEmailTemplate());
			template.setTextTemplate(data.getTextTemplate());
			template.setStaffEmailTemplate(data.getStaffEmailTemplate());

			template = templateRepo.save(template);

			response.setData(assembler.toModel(template));

		} catch (DataNotFoundException e) {

			log.error(e.getLocalizedMessage());
			throw new DataNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<NotificationTemplate>>> listNotificationTemplates(String keyword,
			Pageable pageable) {
		Response<CollectionModel<EntityModel<NotificationTemplate>>> response = new Response<>(ResponseCode.SUCCESS,
				true, "NOTIFICATION TEMPLATES RETRIEVED SUCCESSFULLY", null);
		try {

			Page<NotificationTemplate> data = null;
			if (keyword.equalsIgnoreCase("All")) {
				data = templateRepo.findByActive(true, pageable);
			} else {

				data = templateRepo.findByKeywordAndActive(keyword, true, pageable);
			}

			if (data == null) {

				response.setMessage("NOTIFICATION TEMPLATES NOT FOUND");
				return response;
			}

			response.setData(pagedResourcesAssembler.toModel(data));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<NotificationTemplate>>> listAllNotificationTemplates(int page, int size,
			String sortName, String sortType, String name) {
		Response<CollectionModel<EntityModel<NotificationTemplate>>> response = new Response<>(ResponseCode.SUCCESS,
				true, "NOTIFICATION TEMPLATES RETRIEVED SUCCESSFULLY", null);
		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortName).descending());
			if ("ASC".equals(sortType.toUpperCase())) {

				pageable = PageRequest.of(page, size, Sort.by(sortName).ascending());
			}

			Page<NotificationTemplate> pagedData = null;
			if (name != null) {

				pagedData = templateRepo.findByNameOrDisplayName(name, pageable);

			} else {

				pagedData = templateRepo.findAll(pageable);
			}

			if (!pagedData.hasContent()) {

				response.setMessage("NOTIFICATION TEMPLATES NOT FOUND ");
				return response;
			}

			response.setData(pagedResourcesAssembler.toModel(pagedData));

		} catch (Exception e) {

			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<NotificationTemplate>> getNotificationTemplateDetails(Long id) {
		Response<EntityModel<NotificationTemplate>> response = new Response<>(ResponseCode.SUCCESS, true,
				"NOTIFICATION TEMPLATE RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<NotificationTemplate> existing = templateRepo.findById(id);

			if (!existing.isPresent()) {

				throw new DataNotFoundException("NOTIFICATION TEMPLATE NOT FOUND");
			}

			response.setData(assembler.toModel(existing.get()));

		} catch (DataNotFoundException e) {

			log.error(e.getLocalizedMessage());
			throw new DataNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<NotificationTemplate>> activateDeactivateNotification(Long id) {
		Response<EntityModel<NotificationTemplate>> response = new Response<>(ResponseCode.SUCCESS, true,
				"NOTIFICATION TEMPLATE DETAILS UPDATED SUCCESSFULLY", null);
		try {
			LimsUser actor = utility.getUser();

			NotificationTemplate data = templateRepo.getOne(id);
			Boolean active = data.getActive() ? false : true;
			data.setActive(active);
			data.setUpdatedBy(actor);
			data.setUpdatedAt(LocalDateTime.now());
			data = templateRepo.saveAndFlush(data);

			response.setData(assembler.toModel(data));

		} catch (EntityNotFoundException e) {

			log.error(e.getMessage());
			e.printStackTrace();
			response.setMessage("NOTIFICATION TEMPLATE DETAILS NOT FOUND");

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
		return response;
	}

}
