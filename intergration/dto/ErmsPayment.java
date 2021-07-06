package tz.go.tcra.lims.intergration.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ErmsPayment {
	private String sourceRef;
	private String amount;
	private UUID requestId;

}
