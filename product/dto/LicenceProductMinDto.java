package tz.go.tcra.lims.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LicenceProductMinDto {

    private Long id;
    private String code;
    private String name;
    private String displayName;
    private String description;
    private Integer duration;
    private Long categoryID;
    private String categoryName;
}
