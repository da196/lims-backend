package tz.go.tcra.lims.portal.attachments.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentTypePortalDto {
	private Long id;
	private String name;

	private String description;

	private Boolean isPrimary;

	private String attachmentTypePurpose;

	private Boolean active;
}
