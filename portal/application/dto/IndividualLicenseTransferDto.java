package tz.go.tcra.lims.portal.application.dto;

import java.util.Date;
import tz.go.tcra.lims.attachments.dto.AttachmentDto;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import tz.go.tcra.lims.licence.dto.SpectrumValueDto;

/**
 * @author DonaldSj
 */
@Data
public class IndividualLicenseTransferDto {
        
    @NotNull(message="include spectrum cannot be null")
    private Boolean includeSpectrum;
    
    @NotNull(message="include spectrum required cannot be null")
    private Boolean includeSpectrumRequired;
    
    @NotNull(message="spectrum cannot be null")
    @Valid
    private List<SpectrumValueDto> spectrumValue;
    
    @NotBlank(message="request description cannot be null")
    private String requestDescription;
    
    private Double investmentCost;
    private String investmentCostCurrency;
    
    @NotNull(message="commencement date cannot be null")
    private Date commencementDate;
    
    private String otherRelevantInfo;
    
    @NotNull(message="attchments cannot be null")
    @Valid
    private List<AttachmentDto> attachments;
    
    @NotNull(message="licence services cannot be null")
    private List<Long> licenseServices;
    
    @Min(1)
    private Long coverageId;
    
    @NotNull(message="coverage locations cannot be null")
    private Set<Long> coverageLocations;
    
    @NotNull(message="declaration cannot be null")
    private Boolean declaration;
    
    private ContentApplicationDto content;
    
    @Min(0)
    private Long referenceLicenceId;
    
    @Min(1)
    private Long entityId;
}
