/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.payment.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class RequestForControlNumberDto {

	private Long licenseId;
	private List<Long> feeIds;

	private Double amount;
	private Double rate;

	private String companyName;

	private Long currency;

	private String description;
	private String paymentPurpose;

}
