package tz.go.tcra.lims.communications.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.communications.dto.CommunicationChannel;
import tz.go.tcra.lims.communications.entity.NotificationsError;
import tz.go.tcra.lims.communications.entity.NotificationsIn;
import tz.go.tcra.lims.communications.entity.NotificationsOut;
import tz.go.tcra.lims.communications.repository.NotificationsErrorRepository;
import tz.go.tcra.lims.communications.repository.NotificationsInRepository;
import tz.go.tcra.lims.communications.repository.NotificationsOutRepository;
import tz.go.tcra.lims.utils.AppUtility;

@Slf4j
@Service
public class AutoSendEmail {
	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private AppUtility appUtility;

	@Autowired
	private NotificationsErrorRepository notificationsErrorRepository;

	@Autowired
	private NotificationsInRepository notificationsInRepository;

	@Autowired
	private NotificationsOutRepository notificationsOutRepository;

	@Value("${lims.communication.sendermail}")
	private String sendermailAdress;

	public boolean sendEmail(String contentToSend, String subject, String email) {
		Boolean response = false;
		try {
			// String msg = String.format(contentToSend);
			String msg = contentToSend;
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(sendermailAdress);
			message.setTo(email);
			message.setSubject(subject);
			message.setText(msg);
			emailSender.send(message);
			response = true;

		} catch (Exception e) {
			log.info(e.getLocalizedMessage());

		}
		return response;
	}

	@Scheduled(fixedDelay = 60000) // 1 minute
	// @Scheduled(fixedRate = 60000) // 1 minute
	public List<NotificationsIn> sendIncomingMessagePerEmail() {
		log.info("EXECUTING");

		List<NotificationsIn> innotifications = notificationsInRepository.findByActiveAndChannel(true,
				CommunicationChannel.EMAIL);

		for (NotificationsIn notification : innotifications) {
			if (notification.getMessage() != null && notification.getSubject() != null
					&& notification.getContact() != null) {
				if (sendEmail(notification.getMessage(), notification.getSubject(), notification.getContact())) {
					// move message to notification out as the email has been sent OK
					NotificationsOut notificationsOut = new NotificationsOut();

					notificationsOut.setApproved(true);
					notificationsOut.setChannel(notification.getChannel());
					notificationsOut.setContact(notification.getContact());
					notificationsOut.setMessage(notification.getMessage());
					notificationsOut.setSubject(notification.getSubject());
					notificationsOut.setCreatedAt(LocalDateTime.now());
					if (notification.getCreatedAt() != null) {
						notificationsOut.setCreatedAt(notification.getCreatedAt());
					}
					notificationsOut.setUpdatedAt(LocalDateTime.now());
					notificationsOutRepository.saveAndFlush(notificationsOut);

					// now email has been sent , delete this message on incoming table

					notificationsInRepository.deleteById(notification.getId());

				} else {
					// email failed to be sent , write the message to errored message table

					NotificationsError notificationsError = new NotificationsError();

					notificationsError.setApproved(true);
					notificationsError.setChannel(notification.getChannel());
					notificationsError.setContact(notification.getContact());
					notificationsError.setMessage(notification.getMessage());
					notificationsError.setSubject(notification.getSubject());
					notificationsError.setErrorTimes(1);
					notificationsError.setCreatedAt(LocalDateTime.now());
					if (notification.getCreatedAt() != null) {
						notificationsError.setCreatedAt(notification.getCreatedAt());
					}
					notificationsError.setUpdatedAt(LocalDateTime.now());
					notificationsErrorRepository.saveAndFlush(notificationsError);

					// now email has been added to errored , delete this message on incoming table

					notificationsInRepository.deleteById(notification.getId());

				}

			}

		}

		return innotifications;
	}

	@Scheduled(fixedDelay = 3600000) // 60 minutes
	// @Scheduled(fixedRate = 3600000)
	public List<NotificationsError> sendErroredMessagePerEmail() {

		List<NotificationsError> errornotifications = notificationsErrorRepository.findByActiveAndChannel(true,
				CommunicationChannel.EMAIL);

		for (NotificationsError notification : errornotifications) {
			if (notification.getMessage() != null && notification.getSubject() != null
					&& notification.getContact() != null) {
				if (sendEmail(notification.getMessage(), notification.getSubject(), notification.getContact())) {
					// move message to notification out as the email has been sent OK
					NotificationsOut notificationsOut = new NotificationsOut();

					notificationsOut.setApproved(true);
					notificationsOut.setChannel(notification.getChannel());
					notificationsOut.setContact(notification.getContact());
					notificationsOut.setMessage(notification.getMessage());
					notificationsOut.setSubject(notification.getSubject());
					notificationsOut.setCreatedAt(LocalDateTime.now());
					if (notification.getCreatedAt() != null) {
						notificationsOut.setCreatedAt(notification.getCreatedAt());
					}
					notificationsOut.setUpdatedAt(LocalDateTime.now());
					notificationsOutRepository.saveAndFlush(notificationsOut);

					// now email has been sent , delete this message on incoming table

					notificationsErrorRepository.deleteById(notification.getId());

				} else {
					// email failed to be sent , write the message to errored message table

					notification.setErrorTimes(notification.getErrorTimes() + 1);

					notification.setUpdatedAt(LocalDateTime.now());
					notificationsErrorRepository.saveAndFlush(notification);

				}

			}

		}

		return errornotifications;
	}

}
