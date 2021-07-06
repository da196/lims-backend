/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.entity.Audit;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.utils.repository.AuditRepository;

/**
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditRepository auditRepo;

    @Override
    public void saveAuditActivity(LimsUser actor, String activity) {
        try {

            Audit audit = new Audit();
            audit.setActor(actor);
            audit.setActivity(activity);

            auditRepo.saveAndFlush(audit);
        } catch (Exception e) {

            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


}
