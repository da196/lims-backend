package tz.go.tcra.lims.communications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.communications.dto.CommunicationChannel;
import tz.go.tcra.lims.communications.entity.NotificationsIn;

@Repository
public interface NotificationsInRepository extends JpaRepository<NotificationsIn, Long> {

	List<NotificationsIn> findByActive(boolean b);

	List<NotificationsIn> findByActiveAndChannel(boolean b, CommunicationChannel email);

}
