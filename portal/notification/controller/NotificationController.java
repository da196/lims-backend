package tz.go.tcra.lims.portal.notification.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.portal.notification.dto.NotificationDto;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.portal.notification.dto.NotificationMaxPortalDto;
import tz.go.tcra.lims.portal.notification.dto.NotificationMinPortalDto;
import tz.go.tcra.lims.portal.notification.service.NotificationService;
import tz.go.tcra.lims.utils.SaveResponseDto;

/**
 * @author DonaldSj
 */

@RestController
@RequestMapping(value = "/v1/p/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/list")
    public Response<List<NotificationMinPortalDto>> getListOfNotifications() {
        
        return service.listAll();
    }

    @GetMapping("/{id}")
    public Response<NotificationMaxPortalDto> getNotificationById(@PathVariable("id") Long id) {

        return service.findById(id);
    }
    
    @PostMapping("/save")
    public Response<SaveResponseDto> saveNotification(@RequestBody NotificationDto data) {

        return service.saveNotification(data);
    }
}
