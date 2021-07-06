package tz.go.tcra.lims.intergration.temp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hcmis-development team
 * @date Dec 23, 2020
 * @version 1.0.0
 */

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ERMSBillResponse {

	private List<ERMSBillResponseData> bills;
	
	private ERMSBillResponseData bill;

	private String message;
}
