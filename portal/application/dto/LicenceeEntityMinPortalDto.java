/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

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
public class LicenceeEntityMinPortalDto {
    
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String fax;
    private String website;
    private String physicalAddress;
    private String postalAddress;
    private String postalCode;
    private String categoryName;
}
