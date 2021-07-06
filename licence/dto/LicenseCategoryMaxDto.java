/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.LicenceCategoryFlagEnum;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCategoryMaxDto {

    private Long id;
    private String code;
    private String name;
    private String displayName;
    private String Description;
    private Long parentId;
    private Boolean active;
    private LicenceCategoryFlagEnum flag;
    private List<LicenseDetailMaxDto> services;
}
