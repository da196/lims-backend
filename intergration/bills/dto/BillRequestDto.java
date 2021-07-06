package tz.go.tcra.lims.intergration.bills.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillRequestDto {

	private String billType;

	private String description;

	private ClientDto client;

	private String billDate;

	private String billExpiryDate;

	private Currency currency;

	private String sourceRef;

	private List<Item> items;

	private String modeOfSettlement;

}
