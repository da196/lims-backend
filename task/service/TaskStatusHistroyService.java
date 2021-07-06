/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.service;

import java.util.List;
import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.task.dto.TaskStatusHistoryDto;
import tz.go.tcra.lims.task.entity.Trackable;

/**
 *
 * @author emmanuel.mfikwa
 */
public interface TaskStatusHistroyService {
    
    List<TaskStatusHistoryDto> getStatusHistoryByTrackable(Trackable trackable);
    boolean saveStatusHistory(Trackable trackable,Status status);
}
