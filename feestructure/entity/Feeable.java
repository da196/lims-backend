/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.feestructure.entity;

import tz.go.tcra.lims.miscellaneous.enums.FeeableTypeEnum;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface Feeable {
    
    Long getId();
    FeeableTypeEnum feeAbleType();
}
