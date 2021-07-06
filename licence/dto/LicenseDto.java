package tz.go.tcra.lims.licence.dto;

import tz.go.tcra.lims.product.dto.ProductDto;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.entity.Status;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDto {

	private Long id;

	private LicenceApplicationEntityMaxDto licenceApplicationEntity;

	private ProductDto licenseProduct;

	private Boolean isDraft;

	private Date issuedDate;

	private Date expireDate;

	private String licenseCurrentState;
	private List<LicenseDetailMaxDto> appliedServices;
	private LicenseCategoryMinDto licenseCategory;

	private Boolean active;

	private Status status;

}
