package tz.go.tcra.lims.intergration.temp;

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
public class ERMSAck {

	private Boolean status;
	
	private String messsage;
}
