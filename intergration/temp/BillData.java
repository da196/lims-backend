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

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillData {

	private List<BillDto> bills;

	private String subSpCode;

	private String generatedBy;

	private String spCode;

}
