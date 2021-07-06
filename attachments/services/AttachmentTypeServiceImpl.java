package tz.go.tcra.lims.attachments.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.attachments.dto.AttachmentTypeDto;
import tz.go.tcra.lims.attachments.repository.AttachmentTypeRepository;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.GeneralException;

@Service
@Slf4j
public class AttachmentTypeServiceImpl implements AttachmentTypeService {

	@Autowired
	private AttachmentTypeRepository attachmentTypeRepository;

	@Autowired
	private AttachmentTypeModelAssembler assembler;

	@Autowired
	private PagedResourcesAssembler<AttachmentType> pagedResourcesAssembler;

	@Override
	public Response<EntityModel<AttachmentType>> saveAttachmentType(AttachmentTypeDto data, Long id) {
		Response<EntityModel<AttachmentType>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ATTACHMENT TYPE SAVED SUCCESSFULLY", null);
		try {
			AttachmentType attachmentType = new AttachmentType();

			if (id > 0L) {

				Optional<AttachmentType> existing = attachmentTypeRepository.findById(id);
				if (!existing.isPresent()) {

					throw new DataNotFoundException("ATTACHMENT TYPE NOT FOUND");
				}
				attachmentType = existing.get();
			}

			attachmentType.setName(data.getName());
			attachmentType.setDescription(data.getDescription());
			attachmentType.setAttachmentTypePurpose(data.getAttachmentTypePurpose());
			attachmentType.setIsPrimary(data.getIsPrimary());
			attachmentType = attachmentTypeRepository.save(attachmentType);
			response.setData(assembler.toModel(attachmentType));

		} catch (DataNotFoundException e) {

			log.error(e.getMessage());
			throw new DataNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<AttachmentType>> findAttachmentTypeById(Long id) {
		Response<EntityModel<AttachmentType>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ATTACHMENT TYPE DETAILS RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<AttachmentType> existing = attachmentTypeRepository.findById(id);

			if (!existing.isPresent()) {

				response.setMessage("ATTACHMENT TYPE DETAILS NOT FOUND");
				return response;
			}

			AttachmentType data = existing.get();

			response.setData(assembler.toModel(data));
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<AttachmentType>>> getListOfAttachmentTypes(String keyword,
			Pageable pageable) {
		Response<CollectionModel<EntityModel<AttachmentType>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ATTACHMENT TYPES RETRIEVED SUCCESSFULLY", null);
		try {
			Page<AttachmentType> attachmentTypes = null;

			if (keyword.equalsIgnoreCase("All")) {
				attachmentTypes = attachmentTypeRepository.findByActive(true, pageable); //
			} else {
				attachmentTypes = attachmentTypeRepository.findByKeywordAndActive(keyword, true, pageable);
			}
			if (attachmentTypes.isEmpty()) {

				response.setMessage("ATTACHMENT TYPES NOT FOUND");
				return response;
			}

			response.setData(pagedResourcesAssembler.toModel(attachmentTypes));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<AttachmentType>> deleteAttachmentType(Long id) {
		Response<EntityModel<AttachmentType>> response = new Response<>(ResponseCode.SUCCESS, true,
				"ATTACHMENT TYPE DELETED SUCCESSFULLY", null);
		try {
			Optional<AttachmentType> existing = attachmentTypeRepository.findById(id);

			if (!existing.isPresent()) {

				throw new DataNotFoundException("ATTACHMENT TYPE NOT FOUND");
			}

			AttachmentType data = existing.get();
			data.setActive(false);
			data.setUpdatedAt(LocalDateTime.now());
			data = attachmentTypeRepository.saveAndFlush(data);

			response.setData(assembler.toModel(data));

		} catch (DataNotFoundException e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new DataNotFoundException(e.getMessage());

		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}
}
