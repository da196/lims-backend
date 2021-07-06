package tz.go.tcra.lims.product.dto;

import tz.go.tcra.lims.feestructure.dto.FeeStructureDto;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.EntityApplicationTypeEnum;

@Data
public class EntityProductDto {

    @NotBlank(message="code is required")
    private String code;
    
    @NotBlank(message="name is required")
    private String name;
    
    @NotBlank(message="display is required")
    private String displayName;
    
    private String description;
    
    @NotNull(message="application type cannot be null")
    private EntityApplicationTypeEnum applicationType;
    
    @Valid
    private List<FeeStructureDto> feeStructure;
    
    @NotNull(message="workflow list cannot be null")
    private List<Long> workflows;
}
