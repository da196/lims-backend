package tz.go.tcra.lims.payment.dto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.entity.LicenceServiceDetail;

@Setter
@Getter
@NoArgsConstructor
public class LicenceDto {

	private Long id;
	private Set<LicenceServiceDetail> appliedServices;
	private ApplicantDto applicant;
	private LicenceProductBDto licenceProduct;

}
