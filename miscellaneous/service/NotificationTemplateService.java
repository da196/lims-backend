/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import tz.go.tcra.lims.miscellaneous.entity.NotificationTemplate;
import tz.go.tcra.lims.miscellaneous.dto.NotificationTemplateDto;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface NotificationTemplateService {

	Response<EntityModel<NotificationTemplate>> saveNotificationTemplate(NotificationTemplateDto data, Long id);

	Response<CollectionModel<EntityModel<NotificationTemplate>>> listNotificationTemplates(String keyword,
			Pageable pageable);

	Response<CollectionModel<EntityModel<NotificationTemplate>>> listAllNotificationTemplates(int page, int size,
			String sortName, String sortType, String name);

	Response<EntityModel<NotificationTemplate>> getNotificationTemplateDetails(Long id);

	Response<EntityModel<NotificationTemplate>> activateDeactivateNotification(Long id);
}
