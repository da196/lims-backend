/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tz.go.tcra.lims.task.entity.TaskStatusHistory;
import tz.go.tcra.lims.miscellaneous.entity.Status;
import tz.go.tcra.lims.task.dto.TaskStatusHistoryDto;
import tz.go.tcra.lims.task.entity.Trackable;
import tz.go.tcra.lims.task.repository.TaskStatusHistoryRepository;

/**
 *
 * @author emmanuel.mfikwa
 */
@Service
@Slf4j
public class TaskStatusHistoryServiceImpl implements TaskStatusHistroyService{

    @Autowired
    private TaskStatusHistoryRepository statusHistoryRepo;
    
    @Override
    public List<TaskStatusHistoryDto> getStatusHistoryByTrackable(Trackable trackable) {
        List<TaskStatusHistoryDto> response=new ArrayList();
        try{
        
            response=statusHistoryRepo.findByTrackable(trackable);
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return response;
    }

    @Override
    @Transactional
    public boolean saveStatusHistory(Trackable trackable, Status status) {
        boolean response=true;
        try{        
            Optional<TaskStatusHistory> lastStatus=statusHistoryRepo.findFirstByTrackableIdAndTrackableTypeOrderByCreatedAtDesc(trackable.getId(),trackable.getTrackableType());
            if(lastStatus.isPresent()){
                
                if(lastStatus.get().getGroup().equalsIgnoreCase(status.getGroup()) && 
                        lastStatus.get().getPhase().equalsIgnoreCase(status.getPhase()))
                    
                return response;
            }
            
            TaskStatusHistory statusHistory=new TaskStatusHistory();
            statusHistory.setTrackable(trackable);
            statusHistory.setGroup(status.getGroup());
            statusHistory.setPhase(status.getPhase());
            statusHistoryRepo.save(statusHistory);            
            
        }catch(Exception e){
        
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            response=false;
        }
        
        return response;
    }    
}
