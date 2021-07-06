package tz.go.tcra.lims.attachments.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.AttachmentTypePurposeEnum;

@NoArgsConstructor
@Setter
@Getter
public class AttachmentTypeDto {

	private String name;

	private String description;

	private Boolean isPrimary;

	private AttachmentTypePurposeEnum attachmentTypePurpose;

	private Boolean approved;

	private Boolean active;
}
