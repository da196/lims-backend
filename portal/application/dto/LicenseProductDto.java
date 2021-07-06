package tz.go.tcra.lims.portal.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.entity.LicenceServiceDetail;
import tz.go.tcra.lims.feestructure.entity.FeeStructure;

import java.util.List;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
public class LicenseProductDto {

    private Long productId;

    private String productName;

    private List<LicenceServiceDetail> licenseServices;

    private List<FeeStructure> fees;

}
