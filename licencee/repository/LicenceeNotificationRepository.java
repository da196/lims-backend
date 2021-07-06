/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.licencee.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tz.go.tcra.lims.licencee.dto.LicenceeNotificationMinDto;
import tz.go.tcra.lims.licencee.entity.LicenceeEntity;
import tz.go.tcra.lims.licencee.entity.LicenceeNotification;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface LicenceeNotificationRepository extends JpaRepository<LicenceeNotification,Long>{
    
    List<LicenceeNotification> findByLicencee(LicenceeEntity licenceeEntity);
    
    Optional<LicenceeNotification> findByIdAndLicencee(Long id,LicenceeEntity licenceeEntity);
    
    @Query("SELECT new tz.go.tcra.lims.licencee.dto.LicenceeNotificationMinDto("
            + "e.id,"
            + "e.licencee.name,"
            + "e.message,"
            + "e.notificationCategory.name,"
            + "e.notificationCategory.code,"
            + "to_char(e.createdAt,'DD MMM YYYY HH:MI:ss')) "
            + "FROM LicenceeNotification e "
            + "WHERE CONCAT(e.licencee.name,e.notificationCategory.name,e.notificationCategory.code) LIKE '%?1%' "
            + "ORDER BY e.createdAt DESC")
    Page<LicenceeNotificationMinDto> findLicenceeNotificationsByKeyword(String keyword,Pageable page);
    
    @Query("SELECT new tz.go.tcra.lims.licencee.dto.LicenceeNotificationMinDto("
            + "e.id,"
            + "e.licencee.name,"
            + "e.message,"
            + "e.notificationCategory.name,"
            + "e.notificationCategory.code,"
            + "to_char(e.createdAt,'DD MMM YYYY HH:MI:ss')) "
            + "FROM LicenceeNotification e "
            + "ORDER BY e.createdAt DESC")
    Page<LicenceeNotificationMinDto> findAllLicenceeNotifications(Pageable page);
}
