package tz.go.tcra.lims.portal.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.go.tcra.lims.licence.dto.LicenseDetailMaxDto;

/**
 * @author DonaldSj
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LicenseCategoryDto {

    private Long id;
    private String code;
    private String categoryName;
    private String displayName;
    private List<LicenseDetailMaxDto> services;
}
