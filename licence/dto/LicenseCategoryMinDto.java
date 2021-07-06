package tz.go.tcra.lims.licence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author DonaldSj
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCategoryMinDto {

    private Long id;
    private String code;
    private String categoryName;
    private String displayName;
    private Boolean active;
}
