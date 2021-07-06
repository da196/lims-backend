package tz.go.tcra.lims.intergration.temp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author hcmis-development team
 * @date Dec 22, 2020
 * @version 1.0.0
 */

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillDto {

	private String billType;

	private String description;

//	private ClientDto client;

	private String billDate;

	private String sourceRef;

	private List<Item> items;

	private String modeOfSettlement;
	
//	private String billNumber;
//	
//	private String controlNumber;
//	
//	private String requestRef;

}
