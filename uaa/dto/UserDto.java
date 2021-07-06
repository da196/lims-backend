/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import tz.go.tcra.lims.attachments.dto.AttachmentMaxDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String firstName;;
    private String middleName;;
    private String lastName;
    private String gender;
    private String phone;
    private String physicalAddress;
    private String postalAddress;
    private long countryDto;
    private long region;
    private long district;
    private List<AttachmentDto> attachments;
}
