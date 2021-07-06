
package tz.go.tcra.lims.attachments.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;


@Data
public class AttachmentDto {

    private MultipartFile files;

    private String uri;

    @NotBlank(message = "name is required")
    private String name;

    @Min(1)
    private Long attachmentType;

}
