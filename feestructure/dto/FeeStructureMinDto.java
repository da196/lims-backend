package tz.go.tcra.lims.feestructure.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.dto.ListOfValueMinDto;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.FeePeriodEnum;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
public class FeeStructureMinDto {

    private Long id;
    private String name;
    private String code;
    private String accountCode;
    private ListOfValueMinDto feeType;
    private ListOfValueMinDto currency;
    private Double feeAmount;
    private Double feePercent;
    private Integer frequency;
    private FeePeriodEnum period;
    private ApplicableStateEnum applicableState;
    private Boolean active;
}
