package tz.go.tcra.lims.licence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.miscellaneous.enums.LicenceApplicationStateEnum;
import tz.go.tcra.lims.miscellaneous.enums.LicenceStateEnum;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseMinDto {

    private Long id;
    private String entityName;
    private String applicationNumber;
    private String licenceNumber;
    private LicenceStateEnum licenceState;
    private LicenceApplicationStateEnum applicationState;
    private String status;
    private Long productId;
    private String displayName;
    private Long categoryID;
    private String categoryName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issuedDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;
}
