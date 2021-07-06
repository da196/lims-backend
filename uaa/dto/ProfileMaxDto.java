/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.dto;

import tz.go.tcra.lims.licencee.dto.LicenceeEntityMaxDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileMaxDto {
    
    private UserDto personal;
    private LicenceeEntityMaxDto entity;
}
