/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class ActivityAttachmentDto {
    
    @NotNull(message="activity id is required")
    @Min(1)
    private Long activityId;
    
    @NotNull(message="attachment uri cannot be null")
    private MultipartFile uri;
     
    @NotNull(message="attachment type cannot be null")
    @Min(1)
    private Long attachmentType;
}
