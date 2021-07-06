/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.uaa.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.communications.dto.CommunicationChannel;
import tz.go.tcra.lims.communications.entity.NotificationsIn;
import tz.go.tcra.lims.communications.service.NotificationsInService;
import static tz.go.tcra.lims.config.LimsConfig.zone;
import tz.go.tcra.lims.uaa.dto.PasswordResetRequestDto;
import tz.go.tcra.lims.uaa.dto.RegistrationDto;
import tz.go.tcra.lims.uaa.dto.SignUpRequestDto;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.uaa.entity.Role;
import tz.go.tcra.lims.uaa.entity.UserAccessToken;
import tz.go.tcra.lims.uaa.entity.UserPasswordReset;
import tz.go.tcra.lims.uaa.entity.UserRegistration;
import tz.go.tcra.lims.uaa.entity.UserRole;
import tz.go.tcra.lims.uaa.repository.RoleRepository;
import tz.go.tcra.lims.uaa.repository.UserAccessTokenRepository;
import tz.go.tcra.lims.uaa.repository.UserPasswordResetRepository;
import tz.go.tcra.lims.uaa.repository.UserRegistrationRepository;
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.uaa.repository.UserRoleRepository;
import tz.go.tcra.lims.utils.AppUtility;
import tz.go.tcra.lims.utils.SaveResponseDto;
import tz.go.tcra.lims.utils.exception.BadRequestException;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.DuplicateException;
import tz.go.tcra.lims.utils.exception.ForbiddenException;
import tz.go.tcra.lims.utils.exception.GeneralException;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService{
    
    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserRegistrationRepository userRegistrationRepo;

    @Autowired
    private UserAccessTokenRepository userAccessTokenRepo;
    
    @Autowired
    private RoleRepository roleRepo;
    
    @Autowired
    private UserRoleRepository userRoleRepo;

    @Autowired
    private UserPasswordResetRepository userPasswordResetRepo;
    
    @Autowired
    private NotificationsInService notificationService;
    
    @Autowired
    private AppUtility appUtility;
    
    @Value("${lims.applicant.role.id}")
    private Long applicantRole;

    @Value("${lims.token.expiry.hours}")
    private long tokenExpiryHours;

    @Value("${lims.password.reset.mail.subject}")
    private String passwordResetMailSubject;

    @Value("${lims.password.reset.mail.message}")
    private String passwordResetMailMessage;

    @Value("${lims.password.reset.base.url}")
    private String passwordResetBaseUrl;

    @Value("${lims.account.activation.mail.subject}")
    private String accountActivationMailSubject;

    @Value("${lims.account.activation.mail.message}")
    private String accountActivationMailMessage;

    @Value("${lims.account.activation.base.url}")
    private String accountActivationBaseUrl;
    
    @Override
    @Transactional
    public SaveResponseDto saveRegistration(SignUpRequestDto data) {
        SaveResponseDto response=new SaveResponseDto();
        response.setStatus(HttpStatus.CREATED.value());
        response.setDescription(HttpStatus.CREATED.toString());
        response.setTimestamp(System.currentTimeMillis());
        try{

            LocalDateTime currentTime=LocalDateTime.now(ZoneId.of(zone));

            //save user registration entity
            UserRegistration registration=userRegistrationRepo.findByEmail(data.getEmail());
            LocalDateTime expireAt=currentTime.plusHours(tokenExpiryHours);
            String token=appUtility.generateToken();
            UserAccessToken oldToken=null;

            if(registration == null){

                registration=new UserRegistration();

            }else{

                if(registration.getVerified() == 1){

                    throw new DuplicateException("user with email address exists!");
                }else{
                    
                    oldToken=registration.getToken();
                }
            }

            //save token entity
            UserAccessToken access_token=new UserAccessToken();
            access_token.setToken(token);
            access_token.setExpireAt(expireAt);
            access_token.setStatus(false);
            access_token.setCreatedAt(currentTime);
            access_token=userAccessTokenRepo.saveAndFlush(access_token);

            registration.setEmail(data.getEmail());
            registration.setPassword(appUtility.passwordEncode(data.getPassword()));
            registration.setVerified(0);
            registration.setToken(access_token);
            registration.setCreatedAt(currentTime);

            userRegistrationRepo.saveAndFlush(registration);

            if(oldToken != null){
                //delete the old user access token
                userAccessTokenRepo.delete(oldToken);
            }
            
            String msg=String.format(accountActivationMailMessage,accountActivationBaseUrl+"?token="+token);
            NotificationsIn notification=new NotificationsIn();
            notification.setContact(data.getEmail());
            notification.setSubject(accountActivationMailSubject);
            notification.setMessage(msg);
            notification.setChannel(CommunicationChannel.EMAIL);
            notificationService.saveNotificationsIn(notification);
            
        }catch(DuplicateException e){

            log.error(e.toString());
            throw new DuplicateException(e.getMessage());

        }catch(EntityNotFoundException e){

            log.error(e.toString());
            throw new DataNotFoundException("ROLE NOT FOUND");
        }catch(Exception e){

            log.error(e.toString());
            e.printStackTrace();
            throw new GeneralException("INTERNAL SERVER ERROR");
        }

        return response;
    }

    @Override
    @Transactional
    public SaveResponseDto activateUser(String data) {
        SaveResponseDto response=new SaveResponseDto();
        response.setStatus(HttpStatus.CREATED.value());
        response.setDescription(HttpStatus.CREATED.toString());
        response.setTimestamp(System.currentTimeMillis());
        try{

            //check token availability
            UserAccessToken access_token=userAccessTokenRepo.findByToken(data);

            if(access_token == null){

                throw new ForbiddenException("token not valid!");
            }

            //check if token has already been used
            if(access_token.isStatus()){

                throw new ForbiddenException("token has already been used!");
            }

            LocalDateTime currentTime=LocalDateTime.now();

            //check if token has expired
            if(access_token.getExpireAt().isBefore(currentTime)){

                throw new ForbiddenException("token has expired!");
            }

            access_token.setStatus(true);
            access_token.setUpdatedAt(currentTime);
            access_token=userAccessTokenRepo.saveAndFlush(access_token);

            //update user registration entity details
            UserRegistration registration=userRegistrationRepo.findByToken(data);
            registration.setVerified(1);
            registration.setUpdatedAt(currentTime);
            registration=userRegistrationRepo.saveAndFlush(registration);

            //create user entity
            LimsUser user=new LimsUser();
            user.setEmail(registration.getEmail());
            user.setPassword(registration.getPassword());
            user.setStatus(true);
            user.setAdAuthentication(false);
            user.setUniqueID(UUID.randomUUID());
            user.setCreatedAt(currentTime);
            userRepo.saveAndFlush(user);

            //save user role entity
            Role role=roleRepo.getOne(applicantRole);
            UserRole user_role=new UserRole();
            user_role.setRoleID(role.getId());
            user_role.setUserID(user.getId());
            user.setCreatedAt(currentTime);
            userRoleRepo.saveAndFlush(user_role);

        }catch(ForbiddenException e){

            log.error(e.toString());
            throw new ForbiddenException(e.getMessage());
        }catch(Exception e){
            
            e.printStackTrace();
            log.error(e.toString());
            throw new GeneralException("internal server error");
        }

        return response;
    }

    @Override
    public SaveResponseDto savePasswordReset(PasswordResetRequestDto data) {
        SaveResponseDto response=new SaveResponseDto();
        response.setStatus(HttpStatus.CREATED.value());
        response.setDescription(HttpStatus.CREATED.toString());
        response.setTimestamp(System.currentTimeMillis());
        try{

            //check token availability
            UserAccessToken access_token=userAccessTokenRepo.findByToken(data.getToken());

            if(access_token == null){

                throw new ForbiddenException("token not valid!");
            }

            //check if token has already been used
            if(access_token.isStatus()){

                throw new ForbiddenException("token has already been used!");
            }

            LocalDateTime currentTime=LocalDateTime.now(ZoneId.of(zone));

            //check if token has expired
            if(access_token.getExpireAt().isBefore(currentTime)){

                throw new ForbiddenException("token has expired!");
            }

            //update access token entity
            access_token.setStatus(true);
            access_token.setUpdatedAt(currentTime);
            access_token=userAccessTokenRepo.saveAndFlush(access_token);

            //update password reset entity
            UserPasswordReset reset=userPasswordResetRepo.findByToken(data.getToken());
            reset.setStatus(true);
            reset.setUpdatedAt(currentTime);
            userPasswordResetRepo.saveAndFlush(reset);

            //reset user password
            LimsUser user=userRepo.getOne(reset.getUser().getId());
            user.setPassword(appUtility.passwordEncode(data.getPassword()));
            user.setUpdatedAt(currentTime);
            userRepo.saveAndFlush(user);

        }catch(EntityNotFoundException e){

            log.error(e.toString());
            throw new DataNotFoundException("user not found");
        }catch(ForbiddenException e){

            log.error(e.toString());
            throw new ForbiddenException(e.getMessage());
        }catch(Exception e){

            log.error(e.toString());
            throw new GeneralException("internal server error");
        }
        return response;
    }

    @Override
    public SaveResponseDto forgotPassword(String email) {
        SaveResponseDto response=new SaveResponseDto();
        response.setStatus(HttpStatus.CREATED.value());
        response.setDescription(HttpStatus.CREATED.toString());
        response.setTimestamp(System.currentTimeMillis());
        try{
            //check user email
            LimsUser user=userRepo.findByEmail(email);
            if(user == null){

                throw new DataNotFoundException("email address does not exist!");
            }

            if(user.isAdAuthentication()){

                throw new BadRequestException("account authentification configured on AD!");
            }

            LocalDateTime currentTime=LocalDateTime.now(ZoneId.of(zone));
            LocalDateTime expireAt=currentTime.plusHours(tokenExpiryHours);
            String token=appUtility.generateToken();

            //save access token entity            
            UserAccessToken access_token=new UserAccessToken();
            access_token.setToken(token);
            access_token.setExpireAt(expireAt);
            access_token.setStatus(false);
            access_token.setCreatedAt(currentTime);
            access_token=userAccessTokenRepo.saveAndFlush(access_token);

            //save user password reset entity
            UserPasswordReset reset=new UserPasswordReset();
            reset.setUser(user);
            reset.setToken(token);
            reset.setStatus(false);
            reset.setCreatedAt(currentTime);
            userPasswordResetRepo.saveAndFlush(reset);

            String msg=String.format(passwordResetMailMessage,passwordResetBaseUrl+"?token="+token);
            NotificationsIn notification=new NotificationsIn();
            notification.setContact(email);
            notification.setSubject(passwordResetMailSubject);
            notification.setMessage(msg);
            notification.setChannel(CommunicationChannel.EMAIL);
            notificationService.saveNotificationsIn(notification);

        }catch(BadRequestException e){

            log.error(e.toString());
            throw new BadRequestException(e.getMessage());

        }catch(DataNotFoundException e){

            log.error(e.toString());
            throw new DataNotFoundException(e.getMessage());

        }catch(Exception e){

            log.error(e.toString());
            throw new GeneralException("internal server error");
        }

        return response;
    }
    
    @Override
    public List<RegistrationDto> listRegistrations() {
        List<RegistrationDto> response=new ArrayList();
        try{

            List<UserRegistration> registrations=userRegistrationRepo.findAll();

            for(UserRegistration data : registrations){
                RegistrationDto rg=new RegistrationDto();
                rg.setEmail(data.getEmail());
                rg.setId(data.getId());
                rg.setToken(data.getToken().getToken());
                rg.setVerified(data.getVerified());

                response.add(rg);
            }
        }catch(Exception e){

            log.error(e.toString());
        }

        return response;
    }
}
