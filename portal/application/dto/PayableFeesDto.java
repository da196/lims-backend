/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.application.dto;

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
public class PayableFeesDto {
    
    private Long id;
    private String feeType;
    private Long feeTypeId;
    private Long currencyId;
    private String currencyName;
    private Double feeAmount;
}
