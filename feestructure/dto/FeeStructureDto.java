package tz.go.tcra.lims.feestructure.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.ApplicableStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.FeePeriodEnum;

/**
 * @author DonaldSj
 */

@Data
public class FeeStructureDto {

    @Min(0)
    private Long id;
    
    @NotBlank(message="fee name is required")
    private String name;
    
    private String code;
    private String accountCode;
    
    @Min(1)
    private Long feeType;
    
    @NotNull(message="applicable state is required")
    private ApplicableStateEnum applicableState;
    
    @Min(1)
    private Long feeCurrency;
    
    @Min(1)
    private Double feeAmount;
    private Double feePercent;
    
    @Min(0)
    private Integer frequency;
    
    @NotNull(message="fee period is required")
    private FeePeriodEnum period;
}
