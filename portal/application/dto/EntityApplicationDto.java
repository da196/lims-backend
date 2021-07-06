/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

import tz.go.tcra.lims.licencee.dto.NameChangeDto;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import tz.go.tcra.lims.licencee.dto.ShareholderChangeDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@Setter
@Getter
@Data
public class EntityApplicationDto {
    
    private List<AttachmentDto> attachments;
    private ShareholderChangeDto shareholderChange;
    private NameChangeDto changeOfName;
}
