/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantMinDto {
    
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private String physicalAddress;
    private String postalAddress;
}
