/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author emmanuel.mfikwa
 */
@Getter
@Setter
@NoArgsConstructor
@Data
public class FormFindingDto {
    
    private Long id;
    
    @NotBlank(message="finding is required")
    private String finding;
    
    private String remarks;
}
