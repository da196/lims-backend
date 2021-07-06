/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licence.dto;

import lombok.Data;

/**
 *
 * @author emmanuel.mfikwa
 */
@Data
public class LicenceBillingChargesDto {

	private String feeType;
	private Double amount;
	private String currency;
	private String status;
}
