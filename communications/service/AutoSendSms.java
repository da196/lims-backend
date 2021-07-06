package tz.go.tcra.lims.communications.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.communications.dto.CommunicationChannel;
import tz.go.tcra.lims.communications.dto.SmsDto;
import tz.go.tcra.lims.communications.entity.NotificationsError;
import tz.go.tcra.lims.communications.entity.NotificationsIn;
import tz.go.tcra.lims.communications.entity.NotificationsOut;
import tz.go.tcra.lims.communications.repository.NotificationsErrorRepository;
import tz.go.tcra.lims.communications.repository.NotificationsInRepository;
import tz.go.tcra.lims.communications.repository.NotificationsOutRepository;

@Slf4j
@Service
public class AutoSendSms {

	@Autowired
	private NotificationsErrorRepository notificationsErrorRepository;

	@Autowired
	private NotificationsInRepository notificationsInRepository;

	@Autowired
	private NotificationsOutRepository notificationsOutRepository;

	@Value("${lims.communication.smsgatewayUrl}")
	private String smsgateWayUrl;

	@Value("${lims.communication.smsShortCode}")
	private String smsShortCode;

	@Value("${lims.communication.smsUsername}")
	private String username;

	@Value("${lims.communication.smsPassword}")
	private String password;

	public Boolean SendSms(SmsDto smsDto) {

		Boolean feedback = false;

		smsDto.setFrom(smsShortCode);
		log.info("SMS PAYLOAD" + smsDto.toString());
		try {
			// String smsgateWayUrl = "http://10.200.223.34:8888/api/v1/sms/send";
			log.info("WITHIN TRY CATCH BLOCK NOW  " + smsDto.toString());
			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBasicAuth(username, password);

			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(smsDto, headers);
			log.info("SMS HEADERS  " + headers);

			HttpEntity<String> response = restTemplate.exchange(smsgateWayUrl, HttpMethod.POST, request, String.class);
			feedback = true;

			log.info("SMSC GATE WAY RESPONSE  ");
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());

			feedback = false;
		}

		return feedback;
	}

	@Scheduled(fixedDelay = 60000) // 1 minute
	// @Scheduled(fixedRate = 60000)
	public List<NotificationsIn> sendIncomingMessagePerSms() {
		log.info("EXECUTING SMS");

		List<NotificationsIn> innotifications = notificationsInRepository.findByActiveAndChannel(true,
				CommunicationChannel.SMS);

		log.info("EXECUTING SMS" + innotifications);

		for (NotificationsIn notification : innotifications) {
			if (notification.getMessage() != null && notification.getSubject() != null
					&& notification.getContact() != null) {

				SmsDto smsDto = new SmsDto();

				smsDto.setText(notification.getMessage());
				smsDto.setTo(notification.getContact());

				log.info("ABOUT TO CALL THE MASTER SMS SENDER " + smsDto);

				if (SendSms(smsDto)) {
					log.info("SMS WAS SUCCESSFULLY SENT  " + smsDto);
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

					log.info("ABOUT TO CALL THEERROR SAVER  " + smsDto);
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
	public List<NotificationsError> sendErroredMessagePerSms() {

		List<NotificationsError> errornotifications = notificationsErrorRepository.findByActiveAndChannel(true,
				CommunicationChannel.SMS);

		for (NotificationsError notification : errornotifications) {
			if (notification.getMessage() != null && notification.getSubject() != null
					&& notification.getContact() != null) {
				SmsDto smsDto = new SmsDto();

				smsDto.setText(notification.getMessage());
				smsDto.setTo(notification.getContact());

				if (SendSms(smsDto)) {
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
