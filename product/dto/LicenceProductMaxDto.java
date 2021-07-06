package tz.go.tcra.lims.product.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.feestructure.dto.FeeStructureMinDto;
import tz.go.tcra.lims.licence.dto.LicenseCategoryMinDto;
import tz.go.tcra.lims.workflow.dto.WorkflowMinDto;

@Setter
@Getter
@NoArgsConstructor
public class LicenceProductMaxDto {

    private Long id;
    private String code;
    private String name;
    private String displayName;
    private String description;
    private Integer duration;
    private LicenseCategoryMinDto category;
    private List<FeeStructureMinDto> feeStructures;
    private List<WorkflowMinDto> workflows;
}
