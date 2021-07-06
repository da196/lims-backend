package tz.go.tcra.lims.communications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tz.go.tcra.lims.communications.entity.NotificationsIn;
import tz.go.tcra.lims.communications.service.NotificationsInService;

@RestController
@RequestMapping("/notificationsIn")
public class NotificationsInController {
	@Autowired
	private NotificationsInService notificationsInService;

	@PostMapping(value = "/save")
	NotificationsIn saveNotificationsIn(@RequestBody NotificationsIn notificationsIn) {

		return notificationsInService.saveNotificationsIn(notificationsIn);
	}

}
