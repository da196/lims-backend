/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import tz.go.tcra.lims.licencee.dto.LicenceeEntityMaxDto;
import tz.go.tcra.lims.licencee.dto.LicenceeEntityMinDto;
import tz.go.tcra.lims.licencee.dto.LicenceeNotificationMaxDto;
import tz.go.tcra.lims.licencee.dto.LicenceeNotificationMinDto;
import tz.go.tcra.lims.licencee.dto.ShareholderDto;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.uaa.dto.ProfileMaxDto;
import tz.go.tcra.lims.utils.Response;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface LicenceeEntityService {
    
    public Response<CollectionModel<EntityModel<LicenceeEntityMinDto>>> listAllEntities(String keyword,
			Pageable pageable);
    
    public Response<CollectionModel<EntityModel<LicenceeEntityMinDto>>> listActiveEntities(String keyword,
			Pageable pageable);
    
    public Response<ProfileMaxDto> findProfileByEntityId(Long id);
    
    LicenceeEntityMinDto composeLicenceeEntityMinDto(LicenceeEntity data);
    
    LicenceeEntityMaxDto composeLicenceeEntityMaxDto(LicenceeEntity data);
    
    Response<CollectionModel<EntityModel<LicenceeNotificationMinDto>>> listAllNotification(String keyword,Pageable pageable);
    
    Response<LicenceeNotificationMaxDto> findNotificationById(Long id);
    
    List<ShareholderDto> findLicenceeEntityShareholdersByEntityId(Long id);
}
