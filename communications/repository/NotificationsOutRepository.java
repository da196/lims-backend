package tz.go.tcra.lims.communications.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.communications.entity.NotificationsOut;

@Repository
public interface NotificationsOutRepository extends JpaRepository<NotificationsOut, Long> {

}
