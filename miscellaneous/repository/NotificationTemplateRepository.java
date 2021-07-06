/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.miscellaneous.entity.NotificationTemplate;
import tz.go.tcra.lims.miscellaneous.dto.NotificationTemplateMinDto;

/**
 *
 * @author emmanuel.mfikwa
 */
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

	@Query("SELECT new tz.go.tcra.lims.miscellaneous.dto.NotificationTemplateMinDto(e.id,e.name,e.displayName) "
			+ "FROM NotificationTemplate e " + "WHERE e.active=?1 " + "ORDER BY e.name ASC")
	List<NotificationTemplateMinDto> findByActive(Boolean active);

	@Query("SELECT e FROM NotificationTemplate e WHERE e.name=?1 OR e.displayName=?1")
	Page<NotificationTemplate> findByNameOrDisplayName(String name, Pageable page);

	Page<NotificationTemplate> findByActive(boolean b, Pageable pageable);

	@Query("SELECT b FROM NotificationTemplate b WHERE  "
			+ "CONCAT(b.name,b.displayName,b.textTemplate,b.emailTemplate,b.staffEmailTemplate,b.active)"
			+ " LIKE %?1%")
	Page<NotificationTemplate> findByKeywordAndActive(String keyword, boolean b, Pageable pageable);
}
