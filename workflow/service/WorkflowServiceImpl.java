/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tz.go.tcra.lims.attachments.repository.AttachmentTypeRepository;
import tz.go.tcra.lims.entity.AttachmentType;
import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.miscellaneous.entity.Form;
import tz.go.tcra.lims.miscellaneous.repository.FormRepository;
import tz.go.tcra.lims.miscellaneous.repository.NotificationTemplateRepository;
import tz.go.tcra.lims.miscellaneous.repository.StatusRepository;
import tz.go.tcra.lims.uaa.entity.Role;
import tz.go.tcra.lims.uaa.repository.RoleRepository;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.utils.ResponseCode;
import tz.go.tcra.lims.utils.exception.DataNotFoundException;
import tz.go.tcra.lims.utils.exception.DuplicateException;
import tz.go.tcra.lims.utils.exception.GeneralException;
import tz.go.tcra.lims.workflow.dto.WorkflowDto;
import tz.go.tcra.lims.workflow.dto.WorkflowMinDto;
import tz.go.tcra.lims.workflow.dto.WorkflowStepDecisionDto;
import tz.go.tcra.lims.workflow.dto.WorkflowStepDto;
import tz.go.tcra.lims.workflow.entity.Workflow;
import tz.go.tcra.lims.workflow.entity.WorkflowStep;
import tz.go.tcra.lims.workflow.entity.WorkflowStepDecision;
import tz.go.tcra.lims.workflow.entity.WorkflowStepNotifyRole;
import tz.go.tcra.lims.workflow.entity.WorkflowType;
import tz.go.tcra.lims.workflow.repository.WorkflowRepository;
import tz.go.tcra.lims.workflow.repository.WorkflowStepDecisionRepository;
import tz.go.tcra.lims.workflow.repository.WorkflowStepNotifyRoleRepository;
import tz.go.tcra.lims.workflow.repository.WorkflowStepRepository;
import tz.go.tcra.lims.workflow.repository.WorkflowTypeRepository;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private WorkflowRepository workflowRepo;

	@Autowired
	private WorkflowStepRepository workflowStepRepo;

	@Autowired
	private WorkflowStepDecisionRepository workflowStepDecisionRepo;

	@Autowired
	private WorkflowStepNotifyRoleRepository workflowStepNotifyRoleRepo;

	@Autowired
	private WorkflowTypeRepository worklowTypeRepo;

	@Autowired
	private StatusRepository statusRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private NotificationTemplateRepository templateRepo;

	@Autowired
	private AttachmentTypeRepository attachmentTypeRepo;

	@Autowired
	private FormRepository formRepo;

	@Autowired
	private PagedResourcesAssembler<Workflow> pagedResourcesAssembler;

	@Autowired
	private WorkflowServiceModelAssembler assembler;

	@Override
	public Response<CollectionModel<EntityModel<Workflow>>> getAllWorkflows(String keyword, Pageable pageable) {
		Response<CollectionModel<EntityModel<Workflow>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LIST OF ALL WORKFLOWS", null);
		try {

			Page<Workflow> pagedData = null;
			if (keyword.equalsIgnoreCase("All")) {
				pagedData = workflowRepo.findAll(pageable);
			} else {
				pagedData = workflowRepo.findAllByKeyword(keyword, pageable);

			}

			if (pagedData.hasContent()) {

				response.setData(pagedResourcesAssembler.toModel(pagedData));

			}

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response = new Response<>(ResponseCode.FAILURE, false, "FAILURE TO RETRIEVE DATA ", null);
		}

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<Workflow>>> getWorkflowsByType(Long typeId, int page, int size,
			String sortName, String sortType) {
		Response<CollectionModel<EntityModel<Workflow>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"LIST OF ATTACHABLE WORKFLOWS", null);
		try {

			Pageable pageable = PageRequest.of(page, size, Sort.by(sortName).descending());
			if (sortType.toUpperCase() == "ASC") {

				pageable = PageRequest.of(page, size, Sort.by(sortName).ascending());
			}

			Page<Workflow> pagedData = workflowRepo.findAll(pageable);

			if (pagedData.hasContent()) {

				response.setData(pagedResourcesAssembler.toModel(pagedData));
				response.setMessage("LIST OF ATTACHABLE WORKFLOWS [ PAGE : " + page + ", SIZE : " + size
						+ ", SORT NAME : " + sortName + ", SORT TYPE : " + sortType);
			}

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response = new Response<>(ResponseCode.FAILURE, false, "FAILURE TO RETRIEVE DATA ", null);
		}

		return response;
	}

	@Override
	@Transactional
	public Response<EntityModel<Workflow>> saveWorkflow(WorkflowDto data, Long id) {
		Response response = new Response<>(ResponseCode.SUCCESS, true, "WORKFLOW DETAILS SAVED SUCCESSFULLY", null);
		try {
			Workflow workflow = new Workflow();
			if (id > 0L) {

				workflow = workflowRepo.getOne(id);
				workflow.setUpdatedAt(LocalDateTime.now());
			}

			Optional<WorkflowType> type = worklowTypeRepo.findById(data.getWorkflowType());

			if (!type.isPresent()) {

				throw new DataNotFoundException("WORKFLOW TYPE NOT FOUND [" + data.getWorkflowType() + "]");
			}

			workflow.setCode(data.getCode());
			workflow.setName(data.getName());
			workflow.setDescription(data.getDescription());
			workflow.setWorkflowType(type.get());

			workflow = workflowRepo.saveAndFlush(workflow);

			if (data.getSteps().size() > 0) {

				int i = 0;
				int stepSize = data.getSteps().size();
				Role previousRole = null;
				Role nextRole = null;
				Role currentRole = null;
				Optional<Role> r2 = null;
				for (WorkflowStepDto step : data.getSteps()) {

					Set<WorkflowStepDecision> stepDecisions = new HashSet<WorkflowStepDecision>();
					Set<WorkflowStepNotifyRole> stepNotifyRoles = new HashSet<WorkflowStepNotifyRole>();
					WorkflowStep dt = new WorkflowStep();

					if (step.getId() > 0) {

						Optional<WorkflowStep> existing = workflowStepRepo.findById(step.getId());
						if (!existing.isPresent()) {

							throw new DataNotFoundException(" WORKFLOW STEP NOT FOUND [" + step.getId() + "]");
						}
						dt = existing.get();
					}

					if (i < (stepSize - 1)) {

						Optional<Role> rl = roleRepo.findById(data.getSteps().get(i + 1).getCurrentRoleID());
						if (rl.isPresent()) {

							nextRole = rl.get();
						}
					}

					if (step.getCurrentRoleID() != 0) {

						r2 = roleRepo.findById(step.getCurrentRoleID());
						if (!r2.isPresent()) {

							throw new DataNotFoundException("CURRENT ROLE NOT FOUND ");
						}
						currentRole = r2.get();
					} else {

						dt.setApplicableState(step.getApplicableState());
					}

					dt.setCode(step.getCode());
					dt.setName(step.getName());
					dt.setPreviousRole(previousRole);
					dt.setCurrentRole(currentRole);
					dt.setNextRole(nextRole);
					dt.setStepNumber(i + 1);
                                        dt.setDueDays(step.getDueDays());
					dt.setWorkflow(workflow.getId());
					dt.setApplicantNotify(step.getApplicantNotify());
					dt.setStepType(step.getStepType());

					if (step.getAttachmentType() > 0) {

						Optional<AttachmentType> existingAttachmentType = attachmentTypeRepo
								.findById(step.getAttachmentType());

						if (!existingAttachmentType.isPresent()) {

							throw new DataNotFoundException("ATTACHMENT TYPE NOT FOUND");
						}

						dt.setAttachmentType(existingAttachmentType.get());
					}

					if (step.getFormId() > 0) {

						Optional<Form> existingForm = formRepo.findById(step.getFormId());

						if (!existingForm.isPresent()) {

							throw new DataNotFoundException("FORM NOT FOUND");
						}

						dt.setForm(existingForm.get());
					}

					dt = workflowStepRepo.saveAndFlush(dt);

					// set up step decisions
					if (step.getDecisions().size() > 0) {
						workflowStepDecisionRepo.deleteByStep(dt.getId());
						for (WorkflowStepDecisionDto decision : step.getDecisions()) {

							WorkflowStepDecision stepDecision = new WorkflowStepDecision();
							stepDecision.setDecision(decision.getDecision());

							if (decision.getStatusId() > 0) {

								Optional<Status> existingStatus = statusRepo.findById(decision.getStatusId());
								if (!existingStatus.isPresent()) {

									throw new DataNotFoundException("STATUS NOT FOUND");
								}
								stepDecision.setLicenceStatus(existingStatus.get());
							}

							stepDecision.setStep(dt.getId());
							if (decision.getNotificationTemplateId() > 0) {

								stepDecision.setNotificationTemplate(
										templateRepo.getOne(decision.getNotificationTemplateId()));
							}

							stepDecisions.add(stepDecision);
						}
					}

					// check if notifyRoles is on
//                    if(step.getNotifyRoles().size() > 0){
//                        workflowStepNotifyRoleRepo.deleteByStep(dt.getId());
//                        for(Long roleId : step.getNotifyRoles()){                            
//                            
//                            Optional<Role> role=roleRepo.findById(roleId);
//                        
//                            if(!role.isPresent()){
//                            
//                                throw new DataNotFoundException(" ROLE NOT FOUND ["+roleId+"]");
//                            }
//                            
//                            WorkflowStepNotifyRole stepNotifyRole=new WorkflowStepNotifyRole();
//                            stepNotifyRole.setRole(role.get());
//                            stepNotifyRole.setStep(dt.getId());
//                            stepNotifyRoles.add(stepNotifyRole);
//                        }
//                    }

					dt.setDecisions(stepDecisions);
					dt.setNotifyRoles(stepNotifyRoles);
					workflowStepRepo.saveAndFlush(dt);

					previousRole = currentRole;
					currentRole = null;
					nextRole = null;
					i++;
				}
			}

			response.setData(assembler.toModel(workflow));

		} catch (ConstraintViolationException e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw new DuplicateException("DUPLICATE RECORD");

		} catch (DataNotFoundException e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new DataNotFoundException(e.getLocalizedMessage());

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<Workflow>> activateDeactivateWorkflow(Long id) {
		Response response = new Response<>(ResponseCode.SUCCESS, true, "WORKFLOW DETAILS UPDATE SUCCESSFULLY", null);
		try {
			Workflow workflow = workflowRepo.getOne(id);
			boolean active = workflow.isActive() ? false : true;
			workflow.setActive(active);
			workflow = workflowRepo.saveAndFlush(workflow);

			response.setData(assembler.toModel(workflow));

		} catch (EntityNotFoundException e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response.setMessage("WORKFLOW NOT FOUND");
			throw new DataNotFoundException("WORKFLOW NOT FOUND");

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<EntityModel<Workflow>> getWorkflowById(Long id) {
		Response<EntityModel<Workflow>> response = new Response<>(ResponseCode.SUCCESS, true,
				"WORKFLOW DETAILS RETRIEVED SUCCESSFULLY", null);
		try {

			Optional<Workflow> workflow = workflowRepo.findById(id);

			if (!workflow.isPresent()) {

				response.setMessage("WORKFLOW DETAILS NOT FOUND");
				return response;
			}

			response.setData(assembler.toModel(workflow.get()));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public Response<CollectionModel<EntityModel<Workflow>>> getWorkflowsByActive(String keyword, Pageable pageable) {
		Response<CollectionModel<EntityModel<Workflow>>> response = new Response<>(ResponseCode.SUCCESS, true,
				"WORKFLOWS RETRIEVED SUCCESSFULLY", null);
		try {

			Page<Workflow> data = null;
			if (keyword.equalsIgnoreCase("All")) {

				data = workflowRepo.findByActive(true, pageable);
			} else {
				data = workflowRepo.findByKeywordAndActive(keyword, true, pageable);
			}

			if (data == null) {

				response.setMessage("WORKFLOWS NOT FOUND");
				return response;
			}

			response.setData(pagedResourcesAssembler.toModel(data));

		} catch (Exception e) {

			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GeneralException("INTERNAL SERVER ERROR");
		}

		return response;
	}

	@Override
	public WorkflowMinDto composeWorkflowMinDto(Workflow data) throws Exception {
		WorkflowMinDto response = new WorkflowMinDto();
		response.setId(data.getId());
		response.setName(data.getName());
		response.setCode(data.getCode());
		response.setDescription(data.getDescription());

		return response;
	}

	@Override
	public WorkflowStep getNextWorkflowStep(Long workflowId, Integer stepNumber)
			throws Exception, DataNotFoundException {

		if (stepNumber == 0) {

			Optional<WorkflowStep> existing = workflowStepRepo
					.findFirstByWorkflowAndActiveOrderByStepNumberAsc(workflowId, Boolean.TRUE);
			if (!existing.isPresent()) {

				throw new DataNotFoundException("WORKFLOW STEP NOT FOUND ==> WORKFLOW ID : [" + workflowId + "]");
			}

			return existing.get();
		} else {

			Optional<WorkflowStep> existing = workflowStepRepo
					.findFirstByWorkflowAndActiveAndStepNumberGreaterThanOrderByStepNumberAsc(workflowId, Boolean.TRUE,
							stepNumber);
			if (!existing.isPresent()) {

				return null;
			}

			return existing.get();
		}
	}

	@Override
	public WorkflowStep getPreviousWorkflowStep(Long workflowId, Integer stepNumber)
			throws Exception, DataNotFoundException {
		Optional<WorkflowStep> existing = workflowStepRepo
				.findFirstByWorkflowAndActiveAndStepNumberLessThanOrderByStepNumberDesc(workflowId, Boolean.TRUE,
						stepNumber);
		if (!existing.isPresent()) {

			return null;
		}

		return existing.get();
	}

    @Override
    public Workflow getWorkflow(Long id) {
        Workflow response=new Workflow();
        try {
            Optional<Workflow> workflow = workflowRepo.findById(id);

            if (!workflow.isPresent()) {

                return null;
            }

            response=workflow.get();

        } catch (Exception e) {

            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }

        return response;
    }

    @Override
    public Response<WorkflowStep> activateDeactivateWorkflowStep(Long workflowStepId) {
        Response response = new Response<>(ResponseCode.SUCCESS, true, "WORKFLOW STEP DETAILS UPDATE SUCCESSFULLY", null);
        try {
                Optional<WorkflowStep> workflowStepExistance = workflowStepRepo.findById(workflowStepId);
                
                if(!workflowStepExistance.isPresent()){
                
                    throw new DataNotFoundException("WORKFLOW STEP NOT FOUND");
                }
                
                WorkflowStep workflowStep=workflowStepExistance.get();
                boolean active = workflowStep.isActive() ? false : true;
                workflowStep.setActive(active);
                workflowStep = workflowStepRepo.saveAndFlush(workflowStep);

                response.setData(workflowStep);

        } catch (DataNotFoundException e) {

                log.error(e.getLocalizedMessage());
                e.printStackTrace();
                response.setMessage("WORKFLOW STEP NOT FOUND");
                throw new DataNotFoundException("WORKFLOW STEP NOT FOUND");

        } catch (Exception e) {

                log.error(e.getLocalizedMessage());
                e.printStackTrace();
                throw new GeneralException("INTERNAL SERVER ERROR");
        }

        return response;
    }
}
