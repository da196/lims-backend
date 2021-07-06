/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.FormTypeEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class FormDto {
    
    @NotNull(message="code is required")
    private String code;
    
    @NotNull(message="name is required")
    private String name;
    private String description;
    
    @NotNull(message="type is required")
    private FormTypeEnum type;
}
