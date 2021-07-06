/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueMinDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NotificationMaxPortalDto {
    
    private Long id;
    private String message;
    private ListOfValueMinDto category;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    private List<AttachmentMaxDto> attachments;
}
