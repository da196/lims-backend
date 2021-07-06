/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.utils;

import static com.google.common.base.Preconditions.*;
import static org.springframework.beans.MethodInvocationException.ERROR_CODE;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.entity.Audit;
import tz.go.tcra.lims.miscellaneous.dto.DocumentDto;
import tz.go.tcra.lims.miscellaneous.enums.WorkflowDecisionEnum;
import tz.go.tcra.lims.miscellaneous.service.DocumentService;
import tz.go.tcra.lims.task.entity.TaskTrack;
import tz.go.tcra.lims.task.repository.TaskTrackRepository;
import tz.go.tcra.lims.task.service.TaskService;
import tz.go.tcra.lims.uaa.entity.LimsUser;
import tz.go.tcra.lims.uaa.entity.Permission;
import tz.go.tcra.lims.uaa.entity.Role;
import tz.go.tcra.lims.uaa.entity.RolePermission;
import tz.go.tcra.lims.uaa.entity.UserAccessToken;
import tz.go.tcra.lims.uaa.repository.PermissionRepository;
import tz.go.tcra.lims.uaa.repository.RolePermissionRepository;
import tz.go.tcra.lims.uaa.repository.RoleRepository;
import tz.go.tcra.lims.uaa.repository.UserAccessTokenRepository;
import tz.go.tcra.lims.uaa.repository.UserRepository;
import tz.go.tcra.lims.utils.repository.AuditRepository;
import tz.go.tcra.lims.utils.service.DateService;
import tz.go.tcra.lims.workflow.entity.WorkflowType;
import tz.go.tcra.lims.workflow.repository.WorkflowTypeRepository;

/**
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class AppUtility {

	private static final Logger logger = LogManager.getLogger(AppUtility.class.getName());

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserAccessTokenRepository userAccessTokenRepo;

	@Autowired
	private RolePermissionRepository rolePermissionRepo;

	@Autowired
	private PermissionRepository permissionRepo;

	@Autowired
	private WorkflowTypeRepository workflowTypeRepo;

	@Autowired
	private AuditRepository auditRepo;

        @Autowired
	private TaskTrackRepository trackRepo;
        
	@Autowired
	private JwtUtility jwt;

	@Autowired
	private DateService dateService;

        @Autowired
	private TaskService taskService;
        
        @Autowired
	private DocumentService documentService;
        
	@Value("${lims.licence.track.advance.flag.int}")
	private Integer advanceFlag;

	@Value("${lims.licence.track.return.flag.int}")
	private Integer returnFlag;

	@Value("${lims.licence.track.reject.flag.int}")
	private Integer rejectFlag;

	@Value("${lims.licence.track.resubmit.flag.int}")
	private Integer resubmitFlag;

	public String passwordEncode(String password) {

		PasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(password);
	}

	public LimsUser getUser() {
		LimsUser response = null;
		try {

			String token = httpServletRequest.getHeader("Authorization").substring(7);
			String email = jwt.extractUsername(token);

			response = userRepo.findByEmail(email);

		} catch (Exception e) {

			logger.error(e);
			response = null;
		}

		return response;
	}

	public String generateToken() {
		String response = null;
		try {
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			UserAccessToken token = null;
			String tempToken = null;
			while (true) {

				String randomString = UUID.randomUUID().toString();
				tempToken = Base64.encodeBase64String(encoder.encode(randomString).getBytes());
				token = userAccessTokenRepo.findByToken(tempToken);

				if (token == null) {

					response = tempToken;
					break;
				}
			}

		} catch (Exception e) {

			logger.error(e);
		}

		return response;
	}

	public String generatePassayPassword() {
		PasswordGenerator gen = new PasswordGenerator();
		CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
		CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
		lowerCaseRule.setNumberOfCharacters(2);

		CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
		CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
		upperCaseRule.setNumberOfCharacters(2);

		CharacterData digitChars = EnglishCharacterData.Digit;
		CharacterRule digitRule = new CharacterRule(digitChars);
		digitRule.setNumberOfCharacters(2);

		CharacterData specialChars = new CharacterData() {
			@Override
			public String getErrorCode() {
				return ERROR_CODE;
			}

			@Override
			public String getCharacters() {
				return "!@#$%^&*()_+";
			}

		};

		CharacterRule splCharRule = new CharacterRule(specialChars);
		splCharRule.setNumberOfCharacters(2);

		String password = gen.generatePassword(10, splCharRule, lowerCaseRule, upperCaseRule, digitRule);

		return password;
	}

	public void seeder() {
		try {

			Role sa = this.seedRoles();
			this.seedUsers(sa);
			this.seedPermissions(sa);
			this.seedWorkflowTypes();
			this.dateServiceTester();

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private void seedWorkflowTypes() {
            try {
                    // seeding workflow types
                List<WorkflowType> workflowTypes = new ArrayList<WorkflowType>() {
                    {
                        add(new WorkflowType("LN", "LICENCE_NEW", "NEW LICENCE","Workflow Type for new licence applications"));
                        add(new WorkflowType("LR", "LICENCE_RENEWAL", "LICENCE RENEWAL","Workflow Type for licence renewals"));
                        add(new WorkflowType("LU", "LICENCE_UPDGRADE", "LICENCE UPGRADE","Workflow Type for licence upgrades"));
                        add(new WorkflowType("LTO", "LICENCE_TRANSFER_OF_OWNERSHIP", "LICENCE TRANSFER OF OWNERSHIP","Workflow Type for licence change of ownership"));
                        add(new WorkflowType("LC", "LICENCE_CANCELLATION", "LICENCE CANCELLATION","Workflow Type for licence cancellations"));
                        add(new WorkflowType("RP", "ROYALTY_PAYMENT", "ROYALTY PAYMENT","Workflow Type for licence royalt fees payments"));
                        add(new WorkflowType("FP", "FEE_PAYMENT", "FEE PAYMENT", "Workflow Type for licence fee payment"));
                        add(new WorkflowType("CS", "CHANGE_OF_SHAREHOLDERS", "CHANGE OF SHAREHOLDERS","Workflow Type for change of shareholders"));
                        add(new WorkflowType("CN", "CHANGE_OF_NAME", "CHANGE OF NAME","Workflow Type for change of name"));
                    }
                };

                for (WorkflowType workflowType : workflowTypes) {

                    if (!workflowTypeRepo.existsByName(workflowType.getName())) {

                        workflowTypeRepo.saveAndFlush(workflowType);
                    }
                }
            } catch (Exception e) {

                    log.error(e.getMessage());
            }
	}

	public void saveAudit(String activity, LimsUser actor) {
		try {

			Audit audit = new Audit();
			audit.setActivity(activity);
			audit.setActor(actor);
			auditRepo.save(audit);

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private void seedUsers(Role sa) {
		String password = this.passwordEncode("Moro@sua1");
		List<LimsUser> users = new ArrayList<LimsUser>() {
			{
				add(new LimsUser("dev@tcra.go.tz", password, true, true));
				add(new LimsUser("dg@tcra.go.tz", password, true, true));
				add(new LimsUser("accountant1@tcra.go.tz", password, true, true));
				add(new LimsUser("accountant2@tcra.go.tz", password, true, true));
				add(new LimsUser("lo1@tcra.go.tz", password, true, true));
				add(new LimsUser("lo2@tcra.go.tz", password, true, true));
				add(new LimsUser("lo3@tcra.go.tz", password, true, true));
				add(new LimsUser("hl@tcra.go.tz", password, true, true));
				add(new LimsUser("dle@tcra.go.tz", password, true, true));
				add(new LimsUser("hccu@tcra.go.tz", password, true, true));
				add(new LimsUser("minister1@tcra.go.tz", password, true, true));
			}
		};

		List<Long> ids = new ArrayList();
		ids.add(sa.getId());
		for (LimsUser user : users) {
			try {

				LimsUser existing = userRepo.findByEmail(user.getEmail());
				if (existing == null) {

					existing = userRepo.saveAndFlush(user);

					log.info("USER SEEDED ----- " + user.getEmail());
				}

//                userService.saveUserRole(ids, existing.getId());
			} catch (Exception e) {

				log.error(e.getLocalizedMessage());
			}

		}
	}

	private Role seedRoles() {
		Role response = new Role();
		List<Role> roles = new ArrayList<Role>() {
			{
				add(new Role("SUPER ADMINISTRATOR", "SA", "SUPER ADMINISTRATOR"));
				add(new Role("APPLICANT", "AP", "APPLICANT"));
			}
		};

		for (Role role : roles) {
			Role rl = null;
			Optional<Role> existingRole = roleRepo.findByName(role.getName());
			if (!existingRole.isPresent()) {

				rl = roleRepo.saveAndFlush(role);

				log.info("ROLE SEEDED ----- " + rl.getName());
			} else {

				rl = existingRole.get();
			}

			if (role.getCode().compareToIgnoreCase("SA") == 0) {
				response = rl;
			}
		}

		return response;
	}

	private void seedPermissions(Role superAdmin) {

		List<Permission> permissions = new ArrayList<Permission>() {
			{
				// USER MANAGEMENT
				add(new Permission("ROLE_USER_VIEW_ALL", "View List of All Users", "USER MANAGEMENT"));
				add(new Permission("ROLE_USER_EDIT", "Edit User Details", "USER MANAGEMENT"));
				add(new Permission("ROLE_USER_CREATE", "Add New User Details", "USER MANAGEMENT"));
				add(new Permission("ROLE_USER_VIEW_DETAILS", "View User Details", "USER MANAGEMENT"));
				add(new Permission("ROLE_USER_ROLE_ASSIGNMENT", "Assign User Roles", "USER MANAGEMENT"));

				// PERMISSIONS MANAGEMENT
				add(new Permission("ROLE_ROLE_VIEW_ALL", "View List of All Roles", "USER MANAGEMENT"));
				add(new Permission("ROLE_ROLE_EDIT", "Edit Role Details", "USER MANAGEMENT"));
				add(new Permission("ROLE_ROLE_CREATE", "Add New Role Details", "USER MANAGEMENT"));
				add(new Permission("ROLE_ROLE_VIEW_DETAILS", "View Role Details", "USER MANAGEMENT"));
				add(new Permission("ROLE_ROLE_ASSIGN_PERMISSIONS", "Assign Permissions To Role", "USER MANAGEMENT"));

				// ATTACHMENT TYPES PERMISSIONS
				add(new Permission("ROLE_ATTACHMENTS_TYPE_VIEW", "Can view attachment types", "ATTACHMENT TYPES"));
				add(new Permission("ROLE_ATTACHMENTS_TYPE_EDIT", "Can update  attachment types", "ATTACHMENT TYPES"));
				add(new Permission("ROLE_ATTACHMENTS_TYPE_DELETE", "Can delete attachment types", "ATTACHMENT TYPES"));
				add(new Permission("ROLE_ATTACHMENTS_TYPE_SAVE", "Can save attachment types", "ATTACHMENT TYPES"));
				add(new Permission("ROLE_ATTACHMENTS_TYPE_VIEW_ALL", "Can view  list of attachment types",
						"ATTACHMENT TYPES"));

				// ATTACHMENT PERMISSIONS
				add(new Permission("ROLE_ATTACHMENTS_VIEW_ALL", "View List Of Attachments", "ATTACHMENTS"));
				add(new Permission("ROLE_ATTACHMENTS_VIEW_DETAILS", "View Attachment Details", "ATTACHMENTS"));

				// entity category
				add(new Permission("ROLE_ENTITY_CATEGORY_VIEW_ALL", "View List All Entity Categories",
						"ENTITY CATEGORY MANAGEMENT"));
				add(new Permission("ROLE_ENTITY_CATEGORY_VIEW_DETAILS", "View Entity Category Details",
						"ENTITY CATEGORY MANAGEMENT"));
				add(new Permission("ROLE_ENTITY_CATEGORY_ADD", "Add Entity Category Details",
						"ENTITY CATEGORY MANAGEMENT"));
				add(new Permission("ROLE_ENTITY_CATEGORY_EDIT", "Edit Entity Category Details",
						"ENTITY CATEGORY MANAGEMENT"));

				// LICENCE PERMISSIONS
				add(new Permission("ROLE_LICENSE_DETAIL_VIEW", "Can view  license services", "LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_DETAIL_SAVE", "Can create license services", "LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_DETAIL_EDIT", "Can edit license services", "LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_DETAIL_DELETE", "Can delete license services", "LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_DETAIL_VIEW_ALL", "Can view list of all license services",
						"LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_CATEGORY_SAVE", "Can create  license category", "LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_CATEGORY_VIEW", "Can view  license category", "LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_CATEGORY_DELETE", "Can delete  license category",
						"LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_CATEGORY_VIEW_ALL", "Can view list of all license category",
						"LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_CATEGORY_EDIT", "Can edit license category", "LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_VIEW", "Can view single license and applicants licenses",
						"LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_VIEW_ALL", "Can view all applied licenses", "LICENSE MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_VIEW_NON_DRAFT", "Can view all Non Draft licenses",
						"LICENSE MANAGEMENT"));
				add(new Permission("ROLE_VIEW_PRESENTATION_ATTACHMENT", "Can view presentation attachment",
						"LICENSE MANAGEMENT"));
                                add(new Permission("ROLE_LICENSE_VIEW_PRESENTATIONS", "Can view presentations",
						"LICENSE MANAGEMENT"));
                                add(new Permission("ROLE_LICENCE_CANCELLATION", "Can initiate licence cancellation",
						"LICENSE MANAGEMENT"));

				// LICENSE FEE STRUCTURE PERMISSIONS
				add(new Permission("ROLE_FEE_STRUCTURE_VIEW_ALL", "Can view all Fee Structures",
						"FEE STRUCTURE MANAGEMENT"));
				add(new Permission("ROLE_FEE_STRUCTURE_VIEW", "Can view all Fee Structure Details",
						"FEE STRUCTURE MANAGEMENT"));
				add(new Permission("ROLE_FEE_STRUCTURE_EDIT", "Can Update Fee Structure",
						"FEE STRUCTURE MANAGEMENT"));
				add(new Permission("ROLE_FEE_STRUCTURE_CREATE", "Can Create Fee Structure",
						"FEE STRUCTURE MANAGEMENT"));
				add(new Permission("ROLE_FEE_STRUCTURE_DEACTIVATE_ACTIVATE", "Can Activate/Deactivate Fee Structure",
						"FEE STRUCTURE MANAGEMENT"));

				// GEOLOCATION PERMISSIONS
				add(new Permission("ROLE_GEOLOCATION_VIEW", "Can view list of geolocation", "GEOLOCATION MANAGEMENT"));
				add(new Permission("ROLE_GEOLOCATION_SAVE", "Can add list of geolocation", "GEOLOCATION MANAGEMENT"));
				add(new Permission("ROLE_GEOLOCATION_EDIT", "Can edit of geolocation", "GEOLOCATION MANAGEMENT"));
				add(new Permission("ROLE_GEOLOCATION_DELETE", "Can delete of geolocation", "GEOLOCATION MANAGEMENT"));
				add(new Permission("ROLE_GEOLOCATION_VIEW_ALL", "Can view list of geolocation",
						"GEOLOCATION MANAGEMENT"));

				// WORKFLOW PERMISSIONS
				add(new Permission("ROLE_WORKFLOW_DELETE", "Can Delete Workflow Details", "WORKFLOW MANAGEMENT"));
				add(new Permission("ROLE_WORKFLOW_VIEW_ALL", "Can View a List of All Workflows",
						"WORKFLOW MANAGEMENT"));
				add(new Permission("ROLE_WORKFLOW_VIEW", "Can View Workflow Details", "WORKFLOW MANAGEMENT"));
				add(new Permission("ROLE_WORKFLOW_EDIT", "Can Update Workflow Details", "WORKFLOW MANAGEMENT"));
				add(new Permission("ROLE_WORKFLOW_SAVE", "Can Save Workflow Details", "WORKFLOW MANAGEMENT"));
				add(new Permission("ROLE_WORKFLOW_TYPE_VIEW_ALL", "Can View All Workflow Types",
						"WORKFLOW MANAGEMENT"));

				// PRODUCT PERMISSIONS
				add(new Permission("ROLE_PRODUCT_DELETE", "Can Delete Product Details", "PRODUCT MANAGEMENT"));
				add(new Permission("ROLE_PRODUCT_VIEW_ALL", "Can View a List of All Products", "PRODUCT MANAGEMENT"));
				add(new Permission("ROLE_PRODUCT_VIEW", "Can View Product Details", "PRODUCT MANAGEMENT"));
				add(new Permission("ROLE_PRODUCT_EDIT", "Can Update Product Details", "PRODUCT MANAGEMENT"));
				add(new Permission("ROLE_PRODUCT_SAVE", "Can Save Product Details", "PRODUCT MANAGEMENT"));
				add(new Permission("ROLE_PRODUCT_VIEW_FEE_ONLY", "Can View Product Fees Details",
						"PRODUCT MANAGEMENT"));

				// FORMS PERMISSIONS
				add(new Permission("ROLE_FORM_ACTIVATION_DEACTIVATION", "Can Activate or Deactivate Form Details",
						"FORM MANAGEMENT"));
				add(new Permission("ROLE_FORM_VIEW_ALL", "Can View a List of All Forms", "FORM MANAGEMENT"));
				add(new Permission("ROLE_FORM_VIEW", "Can View Form Details", "FORM MANAGEMENT"));
				add(new Permission("ROLE_FORM_EDIT", "Can Update Form Details", "FORM MANAGEMENT"));
				add(new Permission("ROLE_FORM_SAVE", "Can Save Form Details", "FORM MANAGEMENT"));
				add(new Permission("ROLE_FORM_ITEM_SAVE", "Can Save Form Item Details", "FORM MANAGEMENT"));
				add(new Permission("ROLE_FORM_ITEM_VIEW", "Can View Active Form Items", "FORM MANAGEMENT"));

				// NOTIFICATION TEMPLATE PERMISSIONS
				add(new Permission("ROLE_NOTIFICATION_TEMPLATE_ACTIVATION_DEACTIVATION",
						"Can Activate or Deactivate Notification Template Details",
						"NOTIFICATION TEMPLATE MANAGEMENT"));
				add(new Permission("ROLE_NOTIFICATION_TEMPLATE_VIEW_ALL",
						"Can View a List of All Notification Templates", "NOTIFICATION TEMPLATE MANAGEMENT"));
				add(new Permission("ROLE_NOTIFICATION_TEMPLATE_VIEW", "Can View Notification Template Details",
						"NOTIFICATION TEMPLATE MANAGEMENT"));
				add(new Permission("ROLE_NOTIFICATION_TEMPLATE_EDIT", "Can Update Notification Template Details",
						"NOTIFICATION TEMPLATE MANAGEMENT"));
				add(new Permission("ROLE_NOTIFICATION_TEMPLATE_SAVE", "Can Save Notification Template Details",
						"NOTIFICATION TEMPLATE MANAGEMENT"));
				add(new Permission("ROLE_NOTIFICATION_TEMPLATE_VIEW_ACTIVE",
						"Can View a List of All Active Notification Templates", "NOTIFICATION TEMPLATE MANAGEMENT"));

				// TASK PERMISSIONS
				add(new Permission("ROLE_LICENSE_TASK_VIEW_DETAILS", "Can Task Details", "TASK MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_TASK_VIEW_ALL", "Can View a List of All Tasks", "TASK MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_TASK_VIEW_MY", "Can View Assigned Tasks", "TASK MANAGEMENT"));
				add(new Permission("ROLE_LICENSE_TASK_SAVE_ACTIVITY", "Can Save Task Activity Details",
						"TASK MANAGEMENT"));

				// STATUS PERMISSIONS
				add(new Permission("ROLE_STATUS_ACTIVATION_DEACTIVATION", "Can Activate or Deactivate Status Details",
						"STATUS MANAGEMENT"));
				add(new Permission("ROLE_STATUS_VIEW_ALL", "Can View a List of All Status", "STATUS MANAGEMENT"));
				add(new Permission("ROLE_STATUS_VIEW", "Can View Status Details", "STATUS MANAGEMENT"));
				add(new Permission("ROLE_STATUS_EDIT", "Can Update Status Details", "STATUS MANAGEMENT"));
				add(new Permission("ROLE_STATUS_SAVE", "Can Save Status Details", "STATUS MANAGEMENT"));
				add(new Permission("ROLE_STATUS_VIEW_ACTIVE", "Can View a List of All Active Status",
						"STATUS MANAGEMENT"));

				// REPORTS PERMISSIONS
				add(new Permission("ROLE_VIEW_GENERAL_LICENCE_REPORT", "Can view general all licence report",
						"LICENCE REPORTS"));
				add(new Permission("ROLE_VIEW_GENERAL_APPLICATION_REPORT",
						"Can view general all licence applications report", "LICENCE REPORTS"));
				add(new Permission("ROLE_VIEW_LICENCE_STATS_REPORT", "Can view overall licence statistics",
						"LICENCE REPORTS"));
				add(new Permission("ROLE_VIEW_LICENCE_APPL_STATS_REPORT", "Can view licence application statistics",
						"LICENCE REPORTS"));
				add(new Permission("ROLE_ALL_APPLICATIONS_LICENCES", "Can view all licence and applications list",
						"LICENCE REPORTS"));

				// BILLING MANAGEMENT PERMISSIONS
				add(new Permission("ROLE_VIEW_BILLING_REPORT", "Can view general all billing report",
						"BILLING MANAGEMENT"));
				add(new Permission("ROLE_BILLING_REQUEST_CONTROL_NUMBER", "Can generate control number ",
						"BILLING MANAGEMENT"));
				add(new Permission("ROLE_BILLING_INITIATION_INTERNAL", "Can initiate Bill internally",
						"BILLING MANAGEMENT"));

				// EXCHANGE RATE PERMISSIONS
				add(new Permission("ROLE_EXCHANGE_RATE_VIEW", "Can view Exchange rate", "EXCHANGE RATE"));
				add(new Permission("ROLE_EXCHANGE_RATE_EDIT", "Can update  Exchange rate", "EXCHANGE RATE"));
				add(new Permission("ROLE_EXCHANGE_RATE_DELETE", "Can delete Exchange rate", "EXCHANGE RATE"));
				add(new Permission("ROLE_EXCHANGE_RATE_SAVE", "Can save Exchange rate", "EXCHANGE RATE"));
				add(new Permission("ROLE_EXCHANGE_RATE_VIEW_ALL", "Can view  list of Exchange rates", "EXCHANGE RATE"));
				add(new Permission("ROLE_EXCHANGE_RATE_MAKE_EXCHANGE", "Can exchange currency", "EXCHANGE RATE"));

				// LIST OF VALUES PERMISSIONS
				add(new Permission("ROLE_LIST_OF_VALUES_VIEW", "Can view List of Values", "LIST OF VALUES"));
				add(new Permission("ROLE_LIST_OF_VALUES_EDIT", "Can update  List of Values", "LIST OF VALUES"));
				add(new Permission("ROLE_LIST_OF_VALUES_DELETE", "Can delete List of Values", "LIST OF VALUES"));
				add(new Permission("ROLE_LIST_OF_VALUES_SAVE", "Can save List of Values", "LIST OF VALUES"));
				add(new Permission("ROLE_LIST_OF_VALUES_VIEW_ALL", "Can view  list of List of Values", "LIST OF VALUESE"));
                                
                                // ENTITY APPLICATION PERMISSIONS
				add(new Permission("ROLE_ENTITY_APPLICATION_VIEW_DETAILS", "Can view Entity Application Details", "ENTITY APPLICATION MANAGEMENT"));
				add(new Permission("ROLE_ENTITY_APPLICATION_VIEW_ALL", "Can view all Entity Applications", "ENTITY APPLICATION MANAGEMENT"));
                                
                                // ENTITY PERMISSIONS
				add(new Permission("ROLE_ENTITY_VIEW_DETAILS", "Can view Entity Details", "ENTITY MANAGEMENT"));
				add(new Permission("ROLE_ENTITY_VIEW_ALL", "Can view all entities", "ENTITY MANAGEMENT"));
                                
                                // TASK PERMISSIONS
				add(new Permission("ROLE_TASK_VIEW_ALL", "Can view list of all Tasks", "TASK MANAGEMENT"));
				add(new Permission("ROLE_TASK_VIEW_MY", "Can view list of my Tasks", "TASK MANAGEMENT"));
				add(new Permission("ROLE_TASK_VIEW_DETAILS", "Can view task details", "TASK MANAGEMENT"));
				add(new Permission("ROLE_TASK_SAVE_ACTIVITY", "Can save Activity", "TASK MANAGEMENT"));
                                
                                //NOTIFICATION PERMISSIONS
                                add(new Permission("ROLE_ENTITY_NOTIFICATION_VIEW_ALL", "Can view list of all Notifications", "AUTHORITY NOTIFICATIONS MANAGEMENT"));
				add(new Permission("ROLE_ENTITY_NOTIFICATION_VIEW", "Can view Notification Details", "AUTHORITY NOTIFICATIONS MANAGEMENT"));
				
			}
		};

		for (Permission permission : permissions) {

			Optional<Permission> permOpt = permissionRepo.findPermissionByName(permission.getName());
			Permission perm = null;
			if (!permOpt.isPresent()) {

				perm = permissionRepo.saveAndFlush(permission);
				log.info("PERMISSION SEEDED ----- " + perm.getName());
			} else {

				perm = permOpt.get();
			}
			Optional<RolePermission> existing = rolePermissionRepo.findByRoleIDAndPermissionID(superAdmin.getId(),
					perm.getId());
			RolePermission rolePermission = null;
			if (!existing.isPresent()) {

				rolePermission = new RolePermission();
			} else {

				rolePermission = existing.get();
			}

			rolePermission.setRoleID(superAdmin.getId());
			rolePermission.setPermissionID(perm.getId());
			rolePermissionRepo.saveAndFlush(rolePermission);
		}
	}

	public void dateServiceTester() throws ParseException {

		System.out.println("CURRENT DATE: " + dateService.currentDate());
		System.out.println("THIS WEEK START DATE: " + dateService.thisWeekStartDate());
		System.out.println("THIS WEEK END DATE: " + dateService.thisWeekEndDate());
		System.out.println("THIS MONTH START DATE: " + dateService.thisMonthStartDate());
		System.out.println("DATE OF LAST 2 DAYS: " + dateService.dateOfLastnDays(2));
		System.out.println("DATE OF NEXT 5 DAYS: " + dateService.dateOfNextnDays(5));
	}

	public Integer interpreteDecision(WorkflowDecisionEnum decision) {
		Integer response = advanceFlag;

		if (decision.equals(WorkflowDecisionEnum.APPROVE)) {

			response = advanceFlag;
		} else if (decision.equals(WorkflowDecisionEnum.RECOMMEND)) {

			response = advanceFlag;
		} else if (decision.equals(WorkflowDecisionEnum.COMPLETE)) {

			response = advanceFlag;
		} else if (decision.equals(WorkflowDecisionEnum.INVITE)) {

			response = advanceFlag;
		} else if (decision.equals(WorkflowDecisionEnum.ASSIGN)) {

			response = advanceFlag;
		} else if (decision.equals(WorkflowDecisionEnum.REJECT)) {

			response = rejectFlag;

		} else if (decision.equals(WorkflowDecisionEnum.REVISE)) {

			response = returnFlag;

		} else if (decision.equals(WorkflowDecisionEnum.RESUBMIT)) {

			response = resubmitFlag;

		}else if (decision.equals(WorkflowDecisionEnum.GENERATE)) {

			response = advanceFlag;

		}else if (decision.equals(WorkflowDecisionEnum.ACKNOWLEDGE)) {

			response = advanceFlag;

		}else if (decision.equals(WorkflowDecisionEnum.CONSULT_MINISTRY)) {

			response = advanceFlag;

		}else if (decision.equals(WorkflowDecisionEnum.NO_OBJECTION)) {

			response = advanceFlag;

		}else if (decision.equals(WorkflowDecisionEnum.OBJECTION)) {

			response = returnFlag;

		}else if (decision.equals(WorkflowDecisionEnum.AGREE)) {

			response = advanceFlag;

		} else {

			response = 0;
		}

		return response;
	}
        
        public String getDayOfMonthSuffix(final int n) {
            checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
            if (n >= 11 && n <= 13) {
                return "th";
            }
            switch (n % 10) {
                case 1:  return "st";
                case 2:  return "nd";
                case 3:  return "rd";
                default: return "th";
            }
        }
        
        public void testCertificate(){
            try{
                Optional<TaskTrack> track=trackRepo.findById(281L);
                if(track.isPresent()){

                    taskService.setTrackingData(track.get());
                    DocumentDto document=taskService.composeDocumentDto(track.get(), 30L);
                    
                    documentService.generateDocument(document, 30L);
                    log.info(document.toString());
                }
            }catch(Exception e){
            
                e.printStackTrace();
            }
            
        }
}
