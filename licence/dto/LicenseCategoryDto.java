/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.LicenceCategoryFlagEnum;

/**
 * @author DonaldSj
 */

@Data
public class LicenseCategoryDto {

    @NotBlank(message="code is required")
    private String code;
    
    @NotBlank(message="name is required")
    private String name;
    
    @NotBlank(message="display name is required")
    private String displayName;
    
    private String Description;
    
    @NotNull(message="flag is required")
    private LicenceCategoryFlagEnum flag;
    
    @Min(0)
    private Long parentId;
    
    @NotNull(message="services cannot be null")
    private List<Long> services;
}
