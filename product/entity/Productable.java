package tz.go.tcra.lims.product.entity;

import tz.go.tcra.lims.miscellaneous.enums.ProductTypeEnum;

public interface Productable {
    Long getId();
    ProductTypeEnum getProductType();
}
