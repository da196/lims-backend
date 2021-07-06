package tz.go.tcra.lims.intergration.bills.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hcmis-development team
 * @date Dec 22, 2020
 * @version 1.0.0
 */

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillRequestDataDto {

	private List<BillRequestDto> bills;

	private String subSpCode;

	private String generatedBy;

	private String spCode;

}
