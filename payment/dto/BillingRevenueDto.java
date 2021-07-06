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
import tz.go.tcra.lims.payment.entity.Billing;
import tz.go.tcra.lims.payment.entity.BillingCharges;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingRevenueDto {

	private Billing billing;

	private List<BillingCharges> fees;

	private LicenceDto licence;

	private String billable;

}
