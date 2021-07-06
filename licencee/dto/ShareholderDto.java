/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareholderDto {
    
    private String fullname;
    private String nationality;
    private Long shares;
    private List<AttachmentDto> attachments;
}
