/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.uaa.dto.EntityCategoryDto;
import tz.go.tcra.lims.licencee.entity.EntityCategory;
import tz.go.tcra.lims.licencee.repository.EntityCategoryRepository;
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
public class EntityCategoryServiceImpl implements EntityCategoryService {

	@Autowired
	private EntityCategoryRepository entityCategoryRepo;

	@Autowired
	private EntityCategoryModelAssembler assembler;

	@Override
	public Response<EntityModel<EntityCategory>> saveEntityCategory(EntityCategoryDto data, Long id) {
		try {

			EntityCategory dt = new EntityCategory();

			if (id > 0) {

				dt = entityCategoryRepo.getOne(id);
				dt.setUpdatedAt(LocalDateTime.now());
			} else {

				dt.setCreatedAt(LocalDateTime.now());
			}

			dt.setName(data.getName());
			dt.setCode(data.getCode());
			dt.setDescription(data.getDescription());

			dt = entityCategoryRepo.saveAndFlush(dt);

			return new Response<>(ResponseCode.SUCCESS, true, "entity category saved", assembler.toModel(dt));

		} catch (Exception e) {

			throw new GeneralException("Internal server error");
		}
	}

	@Override
	public Response<EntityModel<EntityCategory>> findEntityCategoryById(long id) {
		try {
			EntityCategory entityCategory = entityCategoryRepo.findEntityCategoryById(id)
					.orElseThrow(() -> new DataNotFoundException("Entity category could not be found!"));

			log.info("Viewed entity category with id: " + entityCategory.getName());
			return new Response<>(ResponseCode.SUCCESS, true, "Successful..", assembler.toModel(entityCategory));

		} catch (DataNotFoundException e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage());
		} catch (Exception e) {
			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
			throw new GeneralException("Requested data could not be found!");
		}
	}

	@Override
	public Response<CollectionModel<EntityModel<EntityCategory>>> getListOfEntityCategory() {
		try {

			return new Response<>(ResponseCode.SUCCESS, true, "LIST OF ENTITY CATEGORIES",
					assembler.toCollectionModel(entityCategoryRepo.findAll()));

		} catch (Exception e) {

			e.printStackTrace();
			throw new GeneralException("internal server error");
		}
	}

	@Override
	public EntityCategory details(long id) {
		EntityCategory entityCategory = null;
		try {
			entityCategory = entityCategoryRepo.getOne(id);

		} catch (Exception e) {

			log.error("EXCEPTION ERROR OCCURRED: " + e.getMessage());
			e.printStackTrace();
		}

		return entityCategory;
	}

	@Override
	public List<EntityCategory> getEntityCategoryByStatus(boolean status) {
		List<EntityCategory> response = new ArrayList();
		try {

			response = entityCategoryRepo.findByActive(status);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return response;
	}

}
