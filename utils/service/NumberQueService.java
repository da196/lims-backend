/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.service;

import tz.go.tcra.lims.entity.Licence;
import tz.go.tcra.lims.miscellaneous.enums.NumberQueTypeEnum;

/**
 * @author emmanuel.mfikwa
 */
public interface NumberQueService {

    void runNumberQue();

    String generateApplicationNumber(Licence licence) throws Exception;

    String generateLicenceNumber(Licence licence) throws Exception;

    void queLicence(Licence licence, NumberQueTypeEnum type);
}
