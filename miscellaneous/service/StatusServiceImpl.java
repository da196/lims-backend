/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.miscellaneous.dto.StatusDto;
import tz.go.tcra.lims.miscellaneous.dto.StatusMinDto;
import tz.go.tcra.lims.miscellaneous.repository.StatusRepository;
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
public class StatusServiceImpl implements StatusService {

	@Autowired
	private StatusRepository statusRepo;

	@Autowired
	private PagedResourcesAssembler<Status> pagedResourcesAssembler;

	@Autowired
	private StatusAssembler assembler;

	@Autowired
	private AppUtility utility;

	@Override
	public Response<EntityModel<Status>> saveStatus(StatusDto data, Long id) {
		Response<EntityModel<Status>> response = new Response<>(ResponseCode.SUCCESS, true, "STATUS SAVED SUCCESSFULLY",
				null);
		try {

			LimsUser actor = utility.getUser();
			Status status = new Status();

			if (id > 0) {

				Optional<Status> existing = statusRepo.findById(id);

				if (!existing.isPresent()) {

					throw new DataNotFoundException("STATUS NOT FOUND");
				}

				status = existing.get();
				status.setUpdatedAt(LocalDateTime.now());
				status.setUpdatedBy(actor);
			} else {

				status.setCreatedBy(actor);
			}

			status.setName(data.getName());
			status.setDisplayName(data.getDisplayName());
			status.setGroup(data.getGroup());
			status.setPhase(data.getPhase());
			status.setDescription(data.getDescription());

			status = statusRepo.save(status);

			response.setData(assembler.toModel(status));

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
	public Response<List<StatusMinDto>> listStatus() {
		Response<List<StatusMinDto>> response = new Response<>(ResponseCode.SUCCESS, true,
				"STATUS RETRIEVED SUCCESSFULLY", null);
		try {

			List<StatusMinDto> data = statusRepo.findByActive(Boolean.TRUE);

			if (data.size() == 0) {

				response.setMessage("STATUS NOT FOUND");
				return response;
			}

			response.setData(data);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<Status>>> listAllStatus(String keyword, Pageable pageable) {
		Response<CollectionModel<EntityModel<Status>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"STATUS RETRIEVED SUCCESSFULLY", null);
		try {

			Page<Status> pagedData = null;
			if (keyword.equalsIgnoreCase("All")) {

				pagedData = statusRepo.findAll(pageable);

			} else {

				pagedData = statusRepo.findAllByKeyword(keyword, pageable);
			}

			if (!pagedData.hasContent()) {

				response.setMessage("STATUS NOT FOUND ");
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
	public Response<EntityModel<Status>> getStatusDetails(Long id) {
		Response<EntityModel<Status>> response = new Response<>(ResponseCode.SUCCESS, true,
				"STATUS RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<Status> existing = statusRepo.findById(id);

			if (!existing.isPresent()) {

				throw new DataNotFoundException("STATUS NOT FOUND");
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
	public Response<EntityModel<Status>> activateDeactivateStatus(Long id) {
		Response<EntityModel<Status>> response = new Response<>(ResponseCode.SUCCESS, true,
				"STATUS DETAILS UPDATED SUCCESSFULLY", null);
		try {
			LimsUser actor = utility.getUser();

			Status data = statusRepo.getOne(id);
			Boolean active = data.getActive() ? false : true;
			data.setActive(active);
			data.setUpdatedBy(actor);
			data.setUpdatedAt(LocalDateTime.now());
			data = statusRepo.saveAndFlush(data);

			response.setData(assembler.toModel(data));

		} catch (EntityNotFoundException e) {

			throw new DataNotFoundException("STATUS NOT FOUND");

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}
		return response;
	}

}
