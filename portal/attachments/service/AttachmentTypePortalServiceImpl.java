package tz.go.tcra.lims.portal.attachments.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tz.go.tcra.lims.attachments.repository.AttachmentTypeRepository;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.miscellaneous.enums.AttachmentTypePurposeEnum;
import tz.go.tcra.lims.portal.attachments.dto.AttachmentTypePortalDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;

@Service
public class AttachmentTypePortalServiceImpl implements AttachmentTypePortalService {

	@Autowired
	AttachmentTypeRepository attachmentTypeRepository;

	@Override
	public Response<AttachmentTypePortalDto> findAttachmentTypeById(Long id) {

		AttachmentTypePortalDto attachmentTypePortalDto = new AttachmentTypePortalDto();
		if (attachmentTypeRepository.existsByIdAndActive(id, true)) {
			AttachmentType attachmentType = attachmentTypeRepository.findByIdAndActive(id, true);

			attachmentTypePortalDto.setActive(attachmentType.getActive());

			attachmentTypePortalDto.setAttachmentTypePurpose(attachmentType.getAttachmentTypePurpose().toString());
			attachmentTypePortalDto.setDescription(attachmentType.getDescription());
			attachmentTypePortalDto.setIsPrimary(attachmentType.getIsPrimary());
			attachmentTypePortalDto.setName(attachmentType.getName());
			attachmentTypePortalDto.setId(attachmentType.getId());

			return new Response<AttachmentTypePortalDto>(ResponseCode.SUCCESS, true, "Successful..",
					attachmentTypePortalDto);
		} else {

			return new Response<AttachmentTypePortalDto>(ResponseCode.NO_RECORD_FOUND, false,
					"Requested data could not be found!..", attachmentTypePortalDto);
		}
	}

	@Override
	public Response<List<AttachmentTypePortalDto>> getListOfAttachmentTypes() {
		List<AttachmentType> attachmentTypes = attachmentTypeRepository.findByActive(true);
		List<AttachmentTypePortalDto> attachmentTypePortalDtolist = new ArrayList<>();

		for (AttachmentType attachmentType : attachmentTypes) {
			AttachmentTypePortalDto attachmentTypePortalDto = new AttachmentTypePortalDto();

			attachmentTypePortalDto.setActive(attachmentType.getActive());
			attachmentTypePortalDto.setAttachmentTypePurpose(attachmentType.getAttachmentTypePurpose().toString());
			attachmentTypePortalDto.setDescription(attachmentType.getDescription());
			attachmentTypePortalDto.setIsPrimary(attachmentType.getIsPrimary());
			attachmentTypePortalDto.setName(attachmentType.getName());
			attachmentTypePortalDto.setId(attachmentType.getId());

			attachmentTypePortalDtolist.add(attachmentTypePortalDto);
		}

		return new Response<List<AttachmentTypePortalDto>>(ResponseCode.SUCCESS, true, "Successful..",
				attachmentTypePortalDtolist);
	}

	@Override
	public Response<List<AttachmentTypePortalDto>> getListOfAttachmentTypesByPurpose(
			AttachmentTypePurposeEnum purpose) {
		List<AttachmentType> attachmentTypes = attachmentTypeRepository.findByAttachmentTypePurpose(purpose);
		List<AttachmentTypePortalDto> attachmentTypePortalDtolist = new ArrayList<>();

		for (AttachmentType attachmentType : attachmentTypes) {
			AttachmentTypePortalDto attachmentTypePortalDto = new AttachmentTypePortalDto();

			attachmentTypePortalDto.setActive(attachmentType.getActive());
			attachmentTypePortalDto.setAttachmentTypePurpose(attachmentType.getAttachmentTypePurpose().toString());
			attachmentTypePortalDto.setDescription(attachmentType.getDescription());
			attachmentTypePortalDto.setIsPrimary(attachmentType.getIsPrimary());
			attachmentTypePortalDto.setName(attachmentType.getName());
			attachmentTypePortalDto.setId(attachmentType.getId());

			attachmentTypePortalDtolist.add(attachmentTypePortalDto);
		}

		return new Response<List<AttachmentTypePortalDto>>(ResponseCode.SUCCESS, true, "Successful..",
				attachmentTypePortalDtolist);
	}

}
