package tz.go.tcra.lims.communications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tz.go.tcra.lims.communications.dto.CommunicationChannel;
import tz.go.tcra.lims.communications.entity.NotificationsError;

@Repository
public interface NotificationsErrorRepository extends JpaRepository<NotificationsError, Long> {

	List<NotificationsError> findByActiveAndChannel(boolean b, CommunicationChannel email);

}
