/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.workflow.service;

import java.util.List;
import tz.go.tcra.lims.utils.Response;
import tz.go.tcra.lims.workflow.entity.WorkflowType;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface WorkflowTypeService {
    
    public Response<List<WorkflowType>> getAllWorkflowTypes();
    public Response<List<WorkflowType>> getWorkflowTypesByActive(boolean active);
}
