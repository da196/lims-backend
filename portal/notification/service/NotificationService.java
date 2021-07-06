/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.portal.notification.service;

import java.util.List;
import tz.go.tcra.lims.portal.notification.dto.NotificationDto;
import tz.go.tcra.lims.portal.notification.dto.NotificationMaxPortalDto;
import tz.go.tcra.lims.portal.notification.dto.NotificationMinPortalDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.SaveResponseDto;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface NotificationService {
 
    Response<List<NotificationMinPortalDto>> listAll();
    Response<SaveResponseDto> saveNotification(NotificationDto data);
    Response<NotificationMaxPortalDto> findById(Long id);
}
