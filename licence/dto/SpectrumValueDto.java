/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
@AllArgsConstructor
public class SpectrumValueDto {

    @NotNull(message="lower band is required")
    private Double lowerBand;
    
    @NotNull(message="upper band is required")
    private Double upperBand;
}
