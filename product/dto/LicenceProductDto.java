package tz.go.tcra.lims.product.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.feestructure.dto.FeeStructureDto;

@Data
public class LicenceProductDto {

    @NotBlank(message="code is required")
    private String code;
    
    @NotBlank(message="name is required")
    private String name;
    
    @NotBlank(message="display is required")
    private String displayName;
    
    private String description;
    
    @Min(1)
    private Integer duration;
    
    @Min(1)
    private Long categoryID;
    
    @Valid
    private List<FeeStructureDto> feeStructure;
    
    @NotNull(message="workflow list cannot be null")
    private List<Long> workflows;
}
