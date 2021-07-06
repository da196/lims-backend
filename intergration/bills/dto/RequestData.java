package tz.go.tcra.lims.intergration.bills.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hcmis-development team
 * @date Dec 23, 2020
 * @version 1.0.0
 */


@NoArgsConstructor
@Getter
@Setter
public class RequestData<T> {

	private T data;
	
	private String signature;
}
