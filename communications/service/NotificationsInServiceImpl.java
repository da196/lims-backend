package tz.go.tcra.lims.communications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tz.go.tcra.lims.communications.entity.NotificationsIn;
import tz.go.tcra.lims.communications.repository.NotificationsInRepository;

@Service
public class NotificationsInServiceImpl implements NotificationsInService {

	@Autowired
	private NotificationsInRepository notificationsInRepository;

	@Override
	public NotificationsIn saveNotificationsIn(NotificationsIn notificationsIn) {

		return notificationsInRepository.saveAndFlush(notificationsIn);
	}

}
