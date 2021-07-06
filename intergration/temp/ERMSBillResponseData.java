/**
 *
 */
package tz.go.tcra.lims.intergration.temp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hcmis-development team
 * @date Feb 2, 2021
 * @version 1.0.0
 */


@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ERMSBillResponseData {

    private String billNumber;

    private String controlNumber;

    private String sourceRef;

    private String requestRef;
}
