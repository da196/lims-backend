/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.service;

import tz.go.tcra.lims.uaa.entity.LimsUser;

/**
 * @author emmanuel.mfikwa
 */
public interface AuditService {

    void saveAuditActivity(LimsUser actor, String activity);
}
