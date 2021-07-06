/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.payment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingDto {

	private List<Long> feeIds;

	private Long licenceId;

	private Long currency;

	private Double rate;

	private Double amount;

	String paymentPurpose;

}
