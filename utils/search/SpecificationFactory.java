/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import tz.go.tcra.lims.miscellaneous.enums.SearchOperationEnum;

import java.util.Collections;

/**
 * @author emmanuel.mfikwa
 */
@Component
public class SpecificationFactory<T> {

    public Specification<T> isEqual(String key, Object arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperationEnum.EQUALITY, Collections.singletonList(arg)).build();
    }

    public Specification<T> isGreaterThan(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperationEnum.GREATER_THAN, Collections.singletonList(arg)).build();
    }
}
