/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class LicenceCancellationDto {
    
    @NotNull(message="licence id is required")
    private Long licenceId;
    
    @NotNull(message="reason is required")
    private String reason;
}
