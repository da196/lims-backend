package tz.go.tcra.lims.feestructure.dto;

import tz.go.tcra.lims.feestructure.entity.Feeable;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
public class FeeStructureMaxDto {

    private String name;
    private String code;
    private String accountCode;
    private Feeable feeable;
    private ApplicableStateEnum applicableState;
    private Long feeCurrency;
    private Double feeAmount;
    private Double feePercent;
    private String frequency;
    private String period;
}
