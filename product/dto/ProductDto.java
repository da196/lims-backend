package tz.go.tcra.lims.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String code;
    private String name;
    private String displayName;
    private Long categoryID;
    private String categoryName;
}
