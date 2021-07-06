/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author emmanuel.mfikwa
 */
@NoArgsConstructor
@Setter
@Getter
@Data
public class RoleDto {
    
    @NotBlank(message="code is required")
    private String code;
    
    @NotBlank(message="name is required")
    private String name;
    
    private String description;
    
    @NotBlank(message="status is required")
    private boolean status=true;
}
