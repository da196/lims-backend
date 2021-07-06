/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.payment.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class RequestForControlNumberGepgDto {

	private String name;

	private Double amount;

	private String billID;

	private String billdescr;

	private Double billeqamount;
	private String billgendate;

	private String billexpdate;

	private String billgenby;

	private String billapprby;
	private String payercellnumber;
	private String payeremail;
	private String currency;

	private String billpayoption;
	private String gfscode;

	private String service;
	private String period;
	private String company;

	private String miscamount;
	private String payerID;
	private String payername;
	private String key;
	private String url;

}
