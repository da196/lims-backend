/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.payment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.BillingAttachedToEnum;

/**
 * @author DonaldSj
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitiateBillByInternalOfficerDto {

	private List<FeesDto> fees;
	private Long currency_id;
	private int expire_days;
	private Long licence_id;
	private int payment_mode;
	private BillingAttachedToEnum billable;

}
