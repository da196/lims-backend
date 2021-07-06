/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.notification.dto;

import java.util.List;
import lombok.Data;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class NotificationDto {
    
    private String message;
    private Long categoryId;
    private List<AttachmentDto> attachments;
}
