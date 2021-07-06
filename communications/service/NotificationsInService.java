package tz.go.tcra.lims.communications.service;

import org.springframework.stereotype.Service;

import tz.go.tcra.lims.communications.entity.NotificationsIn;

@Service
public interface NotificationsInService {
	NotificationsIn saveNotificationsIn(NotificationsIn notificationsIn);

}
