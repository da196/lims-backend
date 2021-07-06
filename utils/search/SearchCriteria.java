/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import tz.go.tcra.lims.miscellaneous.enums.SearchOperationEnum;

import java.util.List;

/**
 * @author emmanuel.mfikwa
 */
@Data
@AllArgsConstructor
public class SearchCriteria {

    private String key;
    private SearchOperationEnum searchOperation;
    private boolean isOrOperation;
    private List<Object> arguments;
}
